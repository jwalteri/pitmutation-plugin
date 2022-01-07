package org.jenkins.plugins.pitmutation;

import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.Job;
import org.jenkinsci.test.acceptance.po.WorkflowJob;
import org.junit.Test;

public class PitMutationTest extends UiTest {


    @Test
    public void simpleTest() {
        WorkflowJob job = jenkins.jobs.create(WorkflowJob.class);
        job.copyResource("/PitMutationTest/mutations.xml");
        job.sandbox.check();
        job.script.set("node('java11-agent') {\n" +
            "    stage ('Checkout') {\n" +
            "        checkout scm\n" +
            "    }\n" +
            "\n" +
            "    stage ('Git mining') {\n" +
            "        discoverGitReferenceBuild()\n" +
            "        mineRepository()\n" +
            "    }\n" +
            "\n" +
            "    stage ('Build, Test, and Static Analysis') {\n" +
            "        withMaven(mavenLocalRepo: '/var/data/m2repository', mavenOpts: '-Xmx768m -Xms512m') {\n" +
            "            sh 'mvn -V -e clean verify -Dmaven.test.failure.ignore -Dgpg.skip'\n" +
            "        }\n" +
            "\n" +
            "        recordIssues tools: [java(), javaDoc()], aggregatingResults: 'true', id: 'java', name: 'Java'\n" +
            "        recordIssues tool: errorProne(), healthy: 1, unhealthy: 20\n" +
            "\n" +
            "        junit testResults: '**/target/*-reports/TEST-*.xml'\n" +
            "        publishCoverage adapters: [jacocoAdapter('**/*/jacoco.xml')], sourceFileResolver: sourceFiles('STORE_ALL_BUILD')\n" +
            "\n" +
            "        recordIssues tools: [checkStyle(pattern: 'target/checkstyle-result.xml'),\n" +
            "            spotBugs(pattern: 'target/spotbugsXml.xml'),\n" +
            "            pmdParser(pattern: 'target/pmd.xml'),\n" +
            "            cpd(pattern: 'target/cpd.xml'),\n" +
            "            taskScanner(highTags:'FIXME', normalTags:'TODO', includePattern: '**/*.java', excludePattern: 'target/**/*')],\n" +
            "            qualityGates: [[threshold: 1, type: 'TOTAL', unstable: true]]\n" +
            "    }\n" +
            "\n" +
            "    stage ('Mutation Coverage') {\n" +
            "        withMaven(mavenLocalRepo: '/var/data/m2repository', mavenOpts: '-Xmx768m -Xms512m') {\n" +
            "            sh \"mvn org.pitest:pitest-maven:mutationCoverage\"\n" +
            "        }\n" +
            "        step([$class: 'PitPublisher', mutationStatsFile: 'target/pit-reports/**/mutations.xml'])\n" +
            "    }\n" +
            "\n" +
            "    stage ('Collect Maven Warnings') {\n" +
            "        recordIssues tool: mavenConsole()\n" +
            "    }\n" +
            "}");

        job.save();

        Build build = buildJob(job);

        UiTestObject sut = new UiTestObject(build, "pitmutation");

        System.out.println("test");
    }

    protected Build buildJob(final Job job) {
        return job.startBuild().waitUntilFinished();
    }
}
