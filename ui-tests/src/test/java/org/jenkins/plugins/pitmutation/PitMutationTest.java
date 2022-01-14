package org.jenkins.plugins.pitmutation;

import org.jenkinsci.test.acceptance.junit.Resource;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.Job;
import org.jenkinsci.test.acceptance.po.WorkflowJob;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

@WithPlugins({"pitmutation", "pipeline-stage-step", "workflow-durable-task-step", "workflow-basic-steps",
    "workflow-job", "workflow-scm-step", "workflow-cps"})
public class PitMutationTest extends UiTest {

    //@Test
    public void BuildSuccessful() {
        Build build = createAndBuildWorkflowJob();
        ConsoleView consoleView = new ConsoleView(build, "console");

        assertThat(consoleView.getConsoleOutput()).contains("Finished: SUCCESS");
    }

    @Test
    public void verifyOverallMutationStatistics() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "pitmutation");
        MutationTableView mutationTableView = dashboardView.openPitMutationView();
        mutationTableView.initialize();
        MutationStatistics mutationStatistics = mutationTableView.getMutationStatistics();

        assertThat(mutationStatistics.getMutations().getName()).isEqualTo("Mutations");
        assertThat(mutationStatistics.getMutations().getValue()).isEqualTo("189 (+189)");
        assertThat(mutationStatistics.getUndetected().getName()).isEqualTo("Undetected");
        assertThat(mutationStatistics.getUndetected().getValue()).isEqualTo("18 (+18)");
        assertThat(mutationStatistics.getCoverage().getName()).isEqualTo("Coverage");
        assertThat(mutationStatistics.getCoverage().getValue()).isEqualTo("90.476% (+90.476%)");

        ComponentTable componentTable = mutationTableView.getComponentTable();

        //assertThat(componentTable.getComponentTableEntries().size()).isEqualTo(9);

        MutationTableView second = mutationTableView.clickRowLink(0);
        second.initialize();

        MutationStatistics secondMutationStatistics = second.getMutationStatistics();
        ComponentTable secondComponentTable = second.getComponentTable();

        assertThat(secondMutationStatistics.getMutations().getName()).isEqualTo("Mutations");
        assertThat(secondComponentTable.getComponentTableEntries().size()).isEqualTo(16);

    }

    //@Test
    public void verifyMutationHierarchy() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "");
        MutationTableView pitMutationView = dashboardView.openPitMutationView();

        System.out.println("asd");
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

