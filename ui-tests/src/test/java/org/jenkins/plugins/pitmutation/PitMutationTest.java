package org.jenkins.plugins.pitmutation;

import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.Job;
import org.jenkinsci.test.acceptance.po.WorkflowJob;
import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@WithPlugins({"pitmutation", "pipeline-stage-step", "workflow-durable-task-step", "workflow-basic-steps",
    "workflow-job", "workflow-scm-step", "workflow-cps"})
// "pipeline-stage-step", "workflow-durable-task-step", "workflow-basic-steps"
public class PitMutationTest extends UiTest {


    @Test
    //@WithPlugins({"pitmutation", "forensics-api"})
    public void simpleTest() {
        WorkflowJob job = jenkins.jobs.create(WorkflowJob.class);
        //job.sandbox.check();
        job.script.set("node {\n" +
            job.copyResourceStep("/PitMutationTest/mutations.xml") + "\n" +
            "pitmutation killRatioMustImprove: false, minimumKillRatio: 50.0, mutationStatsFile: '**.xml'\n" +
            "}");

//        job.script.set("node('pitmutation') {\n" +
//            "    stage ('Mutation Coverage') {\n" +
//            "        withMaven(mavenLocalRepo: '/var/data/m2repository', mavenOpts: '-Xmx768m -Xms512m') {\n" +
//            "            sh \"mvn org.pitest:pitest-maven:mutationCoverage\"\n" +
//            "        }\n" +
//            "        step([$class: 'PitPublisher', mutationStatsFile: 'mutations.xml'])\n" +
//            "    }\n" +
//            "}");

        job.save();

        Build build = buildJob(job);

        UiTestObject sut = new UiTestObject(build, "pitmutation");

        assertThat(sut.getBuildStatusSVG().getText()).contains("successful");
        assertThat(sut.getAll().getText()).contains("PIT Mutation Report");

        System.out.println("test");
    }

    protected Build buildJob(final Job job) {
        return job.startBuild().waitUntilFinished();
    }
}

