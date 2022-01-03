package org.jenkins.plugins.pitmutation;

import org.jenkinsci.plugins.pitmutation.PitPublisher;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.FreeStyleJob;
import org.junit.Test;

import java.io.IOException;

public class FirstTest extends UiTest {

    @Test
    public void myUITest() {
        FreeStyleJob job = createFreeStyleJob("/test/mutations.xml");
        //job.addPublisher(IssuesRecorder.class, recorder -> recorder.setToolWithPattern(JAVA_COMPILER, "**/*.txt"));
        job.save();

        Build build = job.startBuild().waitUntilFinished();

        PitPublisher p = new PitPublisher();
        p.setMutationStatsFile("/test/mutations.xml");


        FirstClass resultPage = new FirstClass(build, "java");
        resultPage.open();

    }

    @Test
    public void UiTest2() throws IOException {
        FreeStyleJob job = createFreeStyleJob("/test/mutations.xml");
        PitPublisher p = new PitPublisher();


    }
}



