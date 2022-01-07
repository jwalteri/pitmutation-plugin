package org.jenkins.plugins.pitmutation;

import org.jenkinsci.test.acceptance.junit.AbstractJUnitTest;
import org.jenkinsci.test.acceptance.po.FreeStyleJob;


/**
 * Base class for all UI tests. Provides several helper methods that can be used by all tests.
 */
abstract class UiTest extends AbstractJUnitTest {
    static final String WARNINGS_PLUGIN_PREFIX = "/";

    protected FreeStyleJob createFreeStyleJob(final String... resourcesToCopy) {
        FreeStyleJob job = jenkins.getJobs().create(FreeStyleJob.class);
        for (String resource : resourcesToCopy) {
            job.copyResource(WARNINGS_PLUGIN_PREFIX + resource);
        }
        return job;
    }
}
