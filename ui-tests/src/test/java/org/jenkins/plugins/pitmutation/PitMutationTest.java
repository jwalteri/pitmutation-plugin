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

@WithPlugins({"pitmutation", "pipeline-stage-step", "workflow-durable-task-step", "workflow-basic-steps",
    "workflow-job", "workflow-scm-step", "workflow-cps"})
public class PitMutationTest extends UiTest {

    //@Test
    public void BuildSuccessful() throws MalformedURLException {
        Build build = createAndBuildWorkflowJob();
        ConsoleView consoleView = new ConsoleView(build, "console");

        assertThat(consoleView.getConsoleOutput()).contains("Finished: SUCCESS");
    }

    @Test
    public void PitMutationReport() throws MalformedURLException {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, "pitmutation");
        dashboardView.openPitMutationView();

        System.out.println("asd");
    }

    private Build createAndBuildWorkflowJob() throws MalformedURLException {
        return buildJob(createWorkflowJob());
    }

    private WorkflowJob createWorkflowJob() throws MalformedURLException {
        WorkflowJob job = jenkins.jobs.create(WorkflowJob.class);

        //String a = job.copyResourceStep("/PitMutationTest/edu.hm.hafner.util/PathUtil.java.html");
        //a = a.replace("mkdir -p PathUtil.java.html", "mkdir -p edu.hm.hafner.util/PathUtil.java.html");
        //a = a.replace("rm -r PathUtil.java.html", "rm -r edu.hm.hafner.util/PathUtil.java.html");
       // a = a.replace("&& base64", "&& mkdir edu.hm.hafner.util && base64");
       // a = a.replace("> PathUtil.java.html ", "> edu.hm.hafner.util/PathUtil.java.html");
        String zipFile = job.copyResourceStep("/PitMutationTest/testdata.zip");
        zipFile += "\n sh '''unzip testdata.zip'''";

        job.script.set("node {\n" +
            zipFile + "\n" +
            job.copyResourceStep("/PitMutationTest/mutations.xml") + "\n" +
            //a + "\n" +
            job.copyResourceStep("/PitMutationTest/edu.hm.hafner.util/PathUtil.java.html") + "\n" +
            "pitmutation killRatioMustImprove: false, minimumKillRatio: 50.0, mutationStatsFile: '**.xml'\n" +
            "}");

        /*
        * ZIP datei probieren und unpacken
        * Plugin: Resource Plugin
        * */

        job.save();
        return job;
    }

    protected Build buildJob(final Job job) {
        return job.startBuild().waitUntilFinished();
    }
}

