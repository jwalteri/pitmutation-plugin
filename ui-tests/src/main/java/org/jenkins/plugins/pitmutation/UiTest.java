package org.jenkins.plugins.pitmutation;

import org.jenkinsci.test.acceptance.junit.AbstractJUnitTest;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.FreeStyleJob;
import org.jenkinsci.test.acceptance.po.Job;

/**
 * Base class for all UI tests. Provides several helper methods that can be used by all tests.
 */
abstract class UiTest extends AbstractJUnitTest {

    protected FreeStyleJob createFreeStyleJob(final String... resourcesToCopy) {
        FreeStyleJob job = jenkins.getJobs().create(FreeStyleJob.class);
        for (String resource : resourcesToCopy) {
            job.copyResource(resource);
        }
        return job;
    }


}
