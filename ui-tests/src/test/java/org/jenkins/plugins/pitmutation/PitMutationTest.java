package org.jenkins.plugins.pitmutation;

import org.jenkins.plugins.pitmutation.Views.ConsoleView;
import org.jenkins.plugins.pitmutation.Views.DashboardView;
import org.jenkins.plugins.pitmutation.Views.MutationDetailView;
import org.jenkins.plugins.pitmutation.Views.PitMutation.MutationStatistics;
import org.jenkins.plugins.pitmutation.Views.PitMutation.MutationTableView;
import org.jenkins.plugins.pitmutation.tables.ClassComponents.ClassComponentTable;
import org.jenkins.plugins.pitmutation.tables.ClassComponents.ClassComponentTableEntry;
import org.jenkins.plugins.pitmutation.tables.Components.ComponentTable;
import org.jenkins.plugins.pitmutation.tables.Components.ComponentTableEntry;
import org.jenkins.plugins.pitmutation.tables.MutationInformation.MutationInformationTable;
import org.jenkins.plugins.pitmutation.tables.MutationInformation.MutationInformationTableEntry;
import org.jenkins.plugins.pitmutation.tables.MutationSource.MutationSourceTable;
import org.jenkins.plugins.pitmutation.tables.MutationSource.MutationSourceTableEntry;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.Job;
import org.jenkinsci.test.acceptance.po.WorkflowJob;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@WithPlugins({"pitmutation", "pipeline-stage-step", "workflow-durable-task-step", "workflow-basic-steps",
    "workflow-job", "workflow-scm-step", "workflow-cps"})
public class PitMutationTest extends UiTest {

    //@Test
    public void BuildSuccessful() {
        Build build = createAndBuildWorkflowJob();
        ConsoleView consoleView = new ConsoleView(build, "console");

        assertThat(consoleView.getConsoleOutput()).contains("[Pipeline] pitmutation");
        assertThat(consoleView.getConsoleOutput()).contains("Finished: SUCCESS");
    }


    @Test
    public void testExamined() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(0);
        moduleView.initialize();

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(15);
        packageView.initialize();

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(0);
        classView.initialize();

        List<String> tests = classView.getTestsExamined();

        assertThat(tests.size()).isEqualTo(21);
        assertThat(tests.get(0)).contains("edu.hm.hafner.util.PathUtilTest", "shouldReturnFallbackOnError",
            "[test-template-invocation:#3] (8 ms)");
    }

    //@Test
    public void verifyMutators() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(0);
        moduleView.initialize();

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(15);
        packageView.initialize();

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(0);
        classView.initialize();

        List<String> mutators = classView.getActiveMutators();

        assertThat(mutators.size()).isEqualTo(11);
        assertThat(mutators.get(0)).isEqualTo("CONDITIONALS_BOUNDARY");
    }

    //@Test
    public void verifyMutationInformation() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(0);
        moduleView.initialize();

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(15);
        packageView.initialize();

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(0);
        classView.initialize();

        MutationInformationTable informationTable = classView.getMutationInformation();
        MutationInformationTableEntry entry = informationTable.getDataEntries().get("42");

        assertThat(entry.getKey()).isEqualTo("42");
        assertThat(entry.getMutationInformation()).contains("1.1", "Location : exists", "edu.hm.hafner.util.PathUtilTest",
            "PathUtil::exists", "KILLED");
    }

    //@Test
    public void verifySourceLink() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(0);
        moduleView.initialize();

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(15);
        packageView.initialize();

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(0);
        classView.initialize();

        MutationSourceTable sourceTable = classView.getMutationSource();
        MutationSourceTableEntry sourceEntry = sourceTable.getDataEntries().get("42");

        MutationInformationTable informationTable = classView.getMutationInformation();
        MutationInformationTableEntry informationEntry = informationTable.getDataEntries().get("42");

        String sourceLink = extractPageAnker(sourceEntry.getLink());
        String mutationLink = extractPageAnker(informationEntry.getClickable().getAttribute("href"));

        assertThat(sourceLink).isEqualTo(mutationLink);
    }

    //@Test
    public void verifySource() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(0);
        moduleView.initialize();

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(15);
        packageView.initialize();

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(0);
        classView.initialize();

        MutationSourceTable sourceTable = classView.getMutationSource();
        MutationSourceTableEntry entry = sourceTable.getDataEntries().get("42");

        assertThat(entry.getKey()).isEqualTo("42");
        assertThat(entry.getSourceCode()).contains("return Files.exists(Paths.get(fileName));");
        assertThat(entry.getMutationDetail()).contains("edu/hm/hafner/util/PathUtil::exists");
        assertThat(entry.getClickable().getText()).isEqualTo("2");
        assertThat(extractPageAnker(entry.getLink())).isEqualTo("def930cd_42");
    }

    //@Test
    public void verifyClassComponentLink() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(0);
        moduleView.initialize();

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(15);
        packageView.initialize();

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(0);
        classView.initialize();
        ClassComponentTable classTable = classView.getComponentDetailTable();
        ClassComponentTableEntry entry = classTable.getDataEntries().get(0);
        MutationSourceTable sourceTable = classView.getMutationSource();

        String componentLink = extractPageAnker(entry.getLink());
        String sourceLink = extractPageAnker(sourceTable.getDataEntries().get(entry.getName()).getLink());

        assertThat(componentLink).isEqualTo(sourceLink);
    }

    private String extractPageAnker(String link) {
        return link.substring(link.indexOf("@") + 1);
    }

    //@Test
    public void verifySorting() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(0);
        moduleView.initialize();

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(15);
        packageView.initialize();

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(0);
        classView.initialize();
        ClassComponentTable classTable = classView.getComponentDetailTable();

        assertFirstClassComponentTableEntry(classTable.getDataEntries().get(0));

        classView = classView.clickSorting(0);
        classView.initialize();
        classTable = classView.getComponentDetailTable();

        assertLastClassComponentTableEntry(classTable.getDataEntries().get(0));
    }

    //@Test
    public void verifyComponents() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();
        ComponentTable baseTable = baseView.getComponentTable();

        assertThat(baseTable.getDataEntries().size()).isEqualTo(1);
        assertFirstComponentTableEntry(baseTable.getDataEntries().get(0), "Module: null");

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(0);
        moduleView.initialize();
        ComponentTable moduleTable = moduleView.getComponentTable();

        assertThat(moduleTable.getDataEntries().size()).isEqualTo(16);
        assertFirstComponentTableEntry(moduleTable.getDataEntries().get(0), "Package: edu.hm.hafner.util");

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(15);
        packageView.initialize();
        ComponentTable packageTable = packageView.getComponentTable();

        assertThat(packageTable.getDataEntries().size()).isEqualTo(1);
        assertPackageComponentTableEntry(packageTable.getDataEntries().get(0));

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(0);
        classView.initialize();
        ClassComponentTable classTable = classView.getComponentDetailTable();

        assertThat(classTable.getDataEntries().size()).isEqualTo(27);
        assertFirstClassComponentTableEntry(classTable.getDataEntries().get(0));
    }

    private void assertLastClassComponentTableEntry(ClassComponentTableEntry entry) {
        assertThat(entry.getName()).isEqualTo("275");
        assertThat(entry.getMutations()).isEqualTo("1");
        assertThat(entry.getMutationsDelta()).isEqualTo("+ 1");
        assertThat(entry.getUndetected()).isEqualTo("0");
        assertThat(entry.getUndetectedDelta()).isEqualTo("0");
        assertThat(entry.getCoverage()).isEqualTo("100.0%");
        assertThat(entry.getCoverageDelta()).isEqualTo("+100.0%");
        assertThat(entry.getMutationDetail()).contains("EmptyObjectReturnVals");
    }

    private void assertFirstClassComponentTableEntry(ClassComponentTableEntry entry) {
        assertThat(entry.getName()).isEqualTo("42");
        assertThat(entry.getMutations()).isEqualTo("2");
        assertThat(entry.getMutationsDelta()).isEqualTo("+ 2");
        assertThat(entry.getUndetected()).isEqualTo("0");
        assertThat(entry.getUndetectedDelta()).isEqualTo("0");
        assertThat(entry.getCoverage()).isEqualTo("100.0%");
        assertThat(entry.getCoverageDelta()).isEqualTo("+100.0%");
        assertThat(entry.getMutationDetail()).contains("BooleanTrueReturnVals BooleanFalseReturnVals ");
    }


    private void assertPackageComponentTableEntry(ComponentTableEntry entry) {
        assertThat(entry.getName()).isEqualTo("Class: edu.hm.hafner.util.PathUtil");
        assertThat(entry.getMutations()).isEqualTo("33");
        assertThat(entry.getMutationsDelta()).isEqualTo("+ 33");
        assertThat(entry.getUndetected()).isEqualTo("0");
        assertThat(entry.getUndetectedDelta()).isEqualTo("0");
        assertThat(entry.getCoverage()).isEqualTo("100.0%");
        assertThat(entry.getCoverageDelta()).isEqualTo("+100.0%");
    }

    private void assertFirstComponentTableEntry(ComponentTableEntry entry, String name) {
        assertThat(entry.getName()).isEqualTo(name);
        assertThat(entry.getMutations()).isEqualTo("189");
        assertThat(entry.getMutationsDelta()).isEqualTo("+ 189");
        assertThat(entry.getUndetected()).isEqualTo("18");
        assertThat(entry.getUndetectedDelta()).isEqualTo("+18");
        assertThat(entry.getCoverage()).isEqualTo("90.476%");
        assertThat(entry.getCoverageDelta()).isEqualTo("+90.476%");
    }

    //@Test
    public void verifyMutationStatistics() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();
        MutationStatistics baseStatistics = baseView.getMutationStatistics();
        assertOverallStatistics(baseStatistics);

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(0);
        moduleView.initialize();
        MutationStatistics moduleStatistics = moduleView.getMutationStatistics();
        assertOverallStatistics(moduleStatistics);

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(15);
        packageView.initialize();
        MutationStatistics packageStatistics = packageView.getMutationStatistics();
        assertSingleStatistics(packageStatistics);

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(0);
        classView.initialize();
        MutationStatistics classStatistics = classView.getMutationStatistics();
        assertSingleStatistics(classStatistics);
    }

    private void assertSingleStatistics(MutationStatistics statistics) {
        assertThat(statistics.getMutations().getName()).isEqualTo("Mutations");
        assertThat(statistics.getMutations().getValue()).isEqualTo("33 (+33)");
        assertThat(statistics.getUndetected().getName()).isEqualTo("Undetected");
        assertThat(statistics.getUndetected().getValue()).isEqualTo("0 (0)");
        assertThat(statistics.getCoverage().getName()).isEqualTo("Coverage");
        assertThat(statistics.getCoverage().getValue()).isEqualTo("100.0% (+100.0%)");
    }

    private void assertOverallStatistics(MutationStatistics statistics) {
        assertThat(statistics.getMutations().getName()).isEqualTo("Mutations");
        assertThat(statistics.getMutations().getValue()).isEqualTo("189 (+189)");
        assertThat(statistics.getUndetected().getName()).isEqualTo("Undetected");
        assertThat(statistics.getUndetected().getValue()).isEqualTo("18 (+18)");
        assertThat(statistics.getCoverage().getName()).isEqualTo("Coverage");
        assertThat(statistics.getCoverage().getValue()).isEqualTo("90.476% (+90.476%)");
    }

    //@Test
    public void verifyMutationHierarchy() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");
        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(0);
        moduleView.initialize();

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(15);
        packageView.initialize();

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(0);
        classView.initialize();

        assertThat(classView.getNavigation().getCurrentLevel()).isEqualTo("CLASS");
        assertThat(classView.getNavigation().getNavigationPoint(0).getName())
            .isEqualTo("Aggregated Reports");
        assertThat(classView.getNavigation().getNavigationPoint(1).getName())
            .isEqualTo("null");
        assertThat(classView.getNavigation().getNavigationPoint(2).getName())
            .isEqualTo("edu.hm.hafner.util.PathUtil");

        packageView = classView.navigatePreviousPage();
        packageView.initialize();

        assertThat(packageView.getNavigation().getCurrentLevel()).isEqualTo("PACKAGE");
        assertThat(packageView.getNavigation().getNavigationPoint(0).getName())
            .isEqualTo("Aggregated Reports");
        assertThat(packageView.getNavigation().getNavigationPoint(1).getName())
            .isEqualTo("null");

        moduleView = packageView.navigatePreviousPage();
        moduleView.initialize();

        assertThat(moduleView.getNavigation().getCurrentLevel()).isEqualTo("MODULE");
        assertThat(moduleView.getNavigation().getNavigationPoint(0).getName())
            .isEqualTo("Aggregated Reports");

        baseView = moduleView.navigatePreviousPage();
        baseView.initialize();

        assertThat(baseView.getNavigation().getCurrentLevel()).isEqualTo("MODULES");
        assertThat(baseView.getNavigation().getNavigationPoints()).isEmpty();
    }

    private Build createAndBuildWorkflowJob() {
        return buildJob(createWorkflowJob());
    }

    private WorkflowJob createWorkflowJob() {
        WorkflowJob job = jenkins.jobs.create(WorkflowJob.class);
        String zipFile = job.copyResourceStep("/PitMutationTest/testdata.zip");
        zipFile += "\n sh '''unzip testdata.zip'''";

        job.script.set("node {\n" +
            zipFile + "\n" +
            job.copyResourceStep("/PitMutationTest/mutations.xml") + "\n" +
            job.copyResourceStep("/PitMutationTest/edu.hm.hafner.util/PathUtil.java.html") + "\n" +
            "pitmutation killRatioMustImprove: false, minimumKillRatio: 50.0, mutationStatsFile: '**.xml'\n" +
            "}");

        job.save();
        return job;
    }

    protected Build buildJob(final Job job) {
        return job.startBuild().waitUntilFinished();
    }
}

