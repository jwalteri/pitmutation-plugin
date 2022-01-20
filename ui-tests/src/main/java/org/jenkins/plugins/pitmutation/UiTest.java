package org.jenkins.plugins.pitmutation;

import org.jenkinsci.test.acceptance.junit.AbstractJUnitTest;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.Job;
import org.jenkinsci.test.acceptance.po.WorkflowJob;


/**
 * Base class for all UI tests. Provides several helper methods that can be used by all tests.
 */
abstract class UiTest extends AbstractJUnitTest {

    /**
     * Creates and build a workflow job.
     *
     * @return The build.
     */
    protected Build createAndBuildWorkflowJob() {
        return buildJob(createWorkflowJob());
    }

    /**
     * Creates a workflow job using the PitMutation Plugin and test data.
     *
     * @return The workflow job.
     */
    protected WorkflowJob createWorkflowJob() {
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

    /**
     * Builds a workflow job.
     *
     * @param job The workflow job.
     * @return The build.
     */
    protected Build buildJob(final Job job) {
        return job.startBuild().waitUntilFinished();
    }
}
