package org.jenkinsci.plugins.pitmutation;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.*;
import hudson.remoting.VirtualChannel;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.pitmutation.targets.MutationStats;
import org.jenkinsci.remoting.RoleChecker;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Pit publisher.
 *
 * @author edward
 */
public class PitPublisher extends Recorder implements SimpleBuildStep {

    private List<Condition> buildConditions;
    private String mutationStatsFile;
    private boolean killRatioMustImprove;
    private float minimumKillRatio;
    private transient TaskListener listener;
    private Run<?, ?> build;

    /**
     * Instantiates a new Pit publisher.
     *
     * @param mutationStatsFile    the mutation stats file
     * @param minimumKillRatio     the minimum kill ratio
     * @param killRatioMustImprove the kill ratio must improve
     */
    protected PitPublisher(String mutationStatsFile, float minimumKillRatio, boolean killRatioMustImprove) {
        this.mutationStatsFile = mutationStatsFile;
        this.killRatioMustImprove = killRatioMustImprove;
        this.minimumKillRatio = minimumKillRatio;
        this.buildConditions = new ArrayList<>();
        this.buildConditions.add(percentageThreshold(minimumKillRatio));
        if (killRatioMustImprove) {
            this.buildConditions.add(mustImprove());
        }
    }

    /**
     * Instantiates a new Pit publisher with Default-Values.
     * <p>
     * {@link #mutationStatsFile} is set to {@code **{@literal /}target/pit-reports/**{@literal /}mutations.xml},
     * {@link #minimumKillRatio} is set to {@code 0.0},
     * {@link #killRatioMustImprove} is set to {@code false},
     */
    @DataBoundConstructor
    public PitPublisher() {
        this("**/target/pit-reports/**/mutations.xml", 0, false);
    }

    @DataBoundSetter
    public void setMutationStatsFile(final String mutationStatsFile) {
        this.mutationStatsFile = mutationStatsFile;
    }

    @DataBoundSetter
    public void setMinimumKillRatio(final float minimumKillRatio) {
        this.minimumKillRatio = minimumKillRatio;
    }

    @DataBoundSetter
    public void setKillRatioMustImprove(final boolean killRatioMustImprove) {
        this.killRatioMustImprove = killRatioMustImprove;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> build, @Nonnull FilePath workspace, @Nonnull Launcher launcher, @Nonnull TaskListener listener) throws InterruptedException, IOException {
        this.listener = listener;
        this.build = build;

        Result result = build.getResult();
        if (build instanceof AbstractBuild<?, ?> && result != null && result.isBetterOrEqualTo(Result.UNSTABLE)) {
            AbstractBuild<?, ?> abstractBuild = (AbstractBuild<?, ?>) build;
            this.listener.getLogger().println("Looking for PIT reports in " + abstractBuild.getModuleRoot().getRemote());

            final FilePath[] moduleRoots = abstractBuild.getModuleRoots();
            final boolean multipleModuleRoots = moduleRoots != null && moduleRoots.length > 1;
            final FilePath moduleRoot = multipleModuleRoots ? abstractBuild.getWorkspace() : abstractBuild.getModuleRoot();
            if (moduleRoot == null) {
                this.listener.getLogger().println("Module root was returned as null");
                return;
            }

            ParseReportCallable fileCallable = new ParseReportCallable(mutationStatsFile);
            FilePath[] reports = moduleRoot.act(fileCallable);
            publishReports(reports, new FilePath(abstractBuild.getRootDir()), abstractBuild.getModuleRoot().getRemote());

            //publish latest reports
            PitBuildAction action = new PitBuildAction(abstractBuild);
            abstractBuild.addAction(action);
            abstractBuild.setResult(decideBuildResult(action));
        } else {
            this.listener.getLogger().println("Looking for PIT reports in " + workspace.getRemote());

            ParseReportCallable fileCallable = new ParseReportCallable(mutationStatsFile);
            FilePath[] reports = workspace.act(fileCallable);
            publishReports(reports, new FilePath(build.getRootDir()), null);

            PitBuildAction action = new PitBuildAction(build);
            build.addAction(action);
            build.setResult(decideBuildResult(action));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new PitProjectAction(project);
    }

    /**
     * Publish reports.
     *
     * @param reports     the reports
     * @param buildTarget the build target
     * @param base        the base path of the report location
     */
    void publishReports(FilePath[] reports, FilePath buildTarget, final String base) {
        for (int i = 0; i < reports.length; i++) {
            FilePath report = reports[i];
            listener.getLogger().println("Publishing mutation report: " + report.getRemote());

            final String moduleName;
            if (StringUtils.isBlank(base)) {
                moduleName = String.valueOf(i == 0 ? null : i);
            } else {
                moduleName = report.getRemote().replace(base, "").split("/")[1];
            }

            final FilePath targetPath = new FilePath(buildTarget, "mutation-report-" + moduleName);
            try {
                reports[i].getParent().copyRecursiveTo(targetPath);
            } catch (IOException e) {
                Util.displayIOException(e, listener);
                e.printStackTrace(listener.fatalError("Unable to copy coverage from " + reports[i] + " to " + buildTarget));
                build.setResult(Result.FAILURE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mutations report exists boolean.
     *
     * @param reportDir the report dir
     * @return the boolean
     */
    boolean mutationsReportExists(FilePath reportDir) {
        if (reportDir == null) {
            return false;
        }
        try {
            FilePath[] search = reportDir.list("**/mutations.xml");
            return search.length > 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Decide build result result.
     *
     * @param action the action
     * @return the worst result from all conditions
     */
    public Result decideBuildResult(PitBuildAction action) {
        Result result = Result.SUCCESS;
        for (Condition condition : buildConditions) {
            Result conditionResult = condition.decideResult(action);
            result = conditionResult.isWorseThan(result) ? conditionResult : result;
        }
        return result;
    }


    /**
     * Required by plugin config
     *
     * @return the minimum kill ratio
     */
    public float getMinimumKillRatio() {
        return minimumKillRatio;
    }

    /**
     * Required by plugin config
     *
     * @return the kill ratio must improve
     */
    public boolean getKillRatioMustImprove() {
        return killRatioMustImprove;
    }

    /**
     * Required by plugin config
     *
     * @return the mutation stats file
     */
    public String getMutationStatsFile() {
        return mutationStatsFile;
    }

    Condition percentageThreshold(final float percentage) {
        return new PercentageThresholdCondition(percentage);
    }

    class PercentageThresholdCondition implements Condition {
        private final float percentage;

        PercentageThresholdCondition(float percentage) {
            super();
            this.percentage = percentage;
        }

        @Override
        public Result decideResult(PitBuildAction action) {
            MutationStats stats = action.getReport().getMutationStats();
            dologging(stats);
            return stats.getKillPercent() >= percentage ? Result.SUCCESS : Result.FAILURE;
        }

        void dologging(MutationStats stats) {
            listener.getLogger().println("Kill ratio is " + stats.getKillPercent() + "% (" + stats.getKillCount()
                + "  " + stats.getTotalMutations() + ")");
        }
    }

    class MustImproveCondition implements Condition {
        @Override
        public Result decideResult(final PitBuildAction action) {
            PitBuildAction previousAction = action.getPreviousAction();
            if (previousAction != null) {
                MutationStats previousStats = previousAction.getReport().getMutationStats();
                logInfo(action, previousStats);
                return action.getReport().getMutationStats().getKillPercent() >= previousStats.getKillPercent() ? Result.SUCCESS
                    : Result.UNSTABLE;
            } else {
                return Result.SUCCESS;
            }
        }

        void logInfo(final PitBuildAction action, MutationStats stats) {
            listener.getLogger().println("Previous kill ratio was " + stats.getKillPercent() + "%");
            listener.getLogger()
                .println("This kill ration is " + action.getReport().getMutationStats().getKillPercent() + "%");
        }
    }

    Condition mustImprove() {
        return new MustImproveCondition();
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    private FilePath getReportDir(FilePath root) throws IOException, InterruptedException {
        FilePath reportsDir = new FilePath(root, mutationStatsFile);
        if (reportsDir.isDirectory()) {
            return reportsDir;
        } else {
            return reportsDir.getParent();
        }
    }

    /**
     * The type Descriptor.
     */
    @Extension
    @Symbol("pitmutation")
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        /**
         * Instantiates a new Descriptor.
         */
        public DescriptorImpl() {
            super(PitPublisher.class);
        }

        @Override
        public String getDisplayName() {
            return Messages.PitPublisher_DisplayName();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            req.bindParameters(this, "pitmutation");
            save();
            return super.configure(req, formData);
        }

//        /**
//         * Creates a new instance of {@link PitPublisher} from a submitted form.
//         */
//        @Override
//        public PitPublisher newInstance(@Nullable StaplerRequest req, @Nonnull JSONObject formData) {
//            if (req == null) {
//                throw new IllegalArgumentException("");
//            }
//            super.newInstance(req, formData);
//            return req.bindJSON(PitPublisher.class, formData);
//        }
    }

    /**
     * The type Parse report callable.
     */
    public static class ParseReportCallable implements FilePath.FileCallable<FilePath[]> {

        private static final long serialVersionUID = 1L;

        private final String reportFilePath;

        /**
         * Instantiates a new Parse report callable.
         *
         * @param reportFilePath the report file path
         */
        public ParseReportCallable(String reportFilePath) {
            this.reportFilePath = reportFilePath;
        }

        @Override
        public FilePath[] invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
            FilePath[] r = new FilePath(f).list(reportFilePath);
            if (r.length < 1) {
                throw new IOException("No reports found at location:" + reportFilePath);
            }
            return r;
        }

        @Override
        public void checkRoles(RoleChecker roleChecker) throws SecurityException {

        }
    }
}
