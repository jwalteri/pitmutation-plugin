package org.jenkins.plugins.pitmutation;

import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.Job;
import org.jenkinsci.test.acceptance.po.WorkflowJob;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void PitMutationReport() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "pitmutation");
        dashboardView.openPitMutationView();

        System.out.println("asd");
    }

    private Build createAndBuildWorkflowJob() {
        return buildJob(createWorkflowJob());
    }

    private WorkflowJob createWorkflowJob() {
        WorkflowJob job = jenkins.jobs.create(WorkflowJob.class);
        job.script.set("node {\n" +
            job.copyResourceStep("/PitMutationTest/mutations.xml") + "\n" +
            "pitmutation killRatioMustImprove: false, minimumKillRatio: 50.0, mutationStatsFile: '**.xml'\n" +
            "}");

        job.save();
        return job;
    }

    protected Build buildJob(final Job job) {
        return job.startBuild().waitUntilFinished();
    }
}

