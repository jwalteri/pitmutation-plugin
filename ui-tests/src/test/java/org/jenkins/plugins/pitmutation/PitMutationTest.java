package org.jenkins.plugins.pitmutation;

import org.jenkins.plugins.pitmutation.Views.ConsoleView;
import org.jenkins.plugins.pitmutation.Views.DashboardView;
import org.jenkins.plugins.pitmutation.Views.MutationDetailView;
import org.jenkins.plugins.pitmutation.Views.MutationTableView;
import org.jenkins.plugins.pitmutation.Views.PitMutation.MutationStatistics;
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
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WithPlugins({"pitmutation", "pipeline-stage-step", "workflow-durable-task-step", "workflow-basic-steps",
    "workflow-job", "workflow-scm-step", "workflow-cps"})
public class PitMutationTest extends UiTest {
    private final static int FIRST_ROW_INDEX = 0;
    private final static int LAST_ROW_INDEX = 15;
    private final static String ROW_KEY = "42";
    private final static String EMPTY_ID = "";
    private final static String FIRST_LEVEL = "Aggregated Reports";
    private final static String SECOND_LEVEL = "null";
    private final static String THIRD_LEVEL = "edu.hm.hafner.util.PathUtil";

    @Test
    public void BuildSuccessful() {
        Build build = createAndBuildWorkflowJob();
        ConsoleView consoleView = new ConsoleView(build, "console");

        assertThat(consoleView.getConsoleOutput()).contains("[Pipeline] pitmutation");
        assertThat(consoleView.getConsoleOutput()).contains("Finished: SUCCESS");
    }

    @Test
    public void testExamined() {
        MutationDetailView classView = getClassView();

        List<String> tests = classView.getTestsExamined();

        assertThat(tests.size()).isEqualTo(21);
        assertThat(tests.get(0)).contains("edu.hm.hafner.util.PathUtilTest", "shouldReturnFallbackOnError",
            "[test-template-invocation:#3] (8 ms)");
    }

    @Test
    public void verifyMutators() {
        MutationDetailView classView = getClassView();

        List<String> mutators = classView.getActiveMutators();

        assertThat(mutators.size()).isEqualTo(11);
        assertThat(mutators.get(0)).isEqualTo("CONDITIONALS_BOUNDARY");
    }

    @Test
    public void verifyMutationInformation() {
        MutationDetailView classView = getClassView();

        MutationInformationTable informationTable = classView.getMutationInformation();
        MutationInformationTableEntry entry = informationTable.getDataEntries().get(ROW_KEY);

        assertThat(entry.getKey()).isEqualTo(ROW_KEY);
        assertThat(entry.getMutationInformation()).contains("1.1", "Location : exists", "edu.hm.hafner.util.PathUtilTest",
            "PathUtil::exists", "KILLED");
    }

    @Test
    public void verifySourceLink() {
        MutationDetailView classView = getClassView();

        MutationSourceTable sourceTable = classView.getMutationSource();
        MutationSourceTableEntry sourceEntry = sourceTable.getDataEntries().get(ROW_KEY);

        MutationInformationTable informationTable = classView.getMutationInformation();
        MutationInformationTableEntry informationEntry = informationTable.getDataEntries().get(ROW_KEY);

        String sourceLink = extractPageAnker(sourceEntry.getLink());
        String mutationLink = extractPageAnker(WebElementUtils.getAttribute(informationEntry.getClickable(),
            WebElementUtils.LINK_ATTRIBUTE));

        assertThat(sourceLink).isEqualTo(mutationLink);
    }

    @Test
    public void verifySource() {
        MutationDetailView classView = getClassView();

        MutationSourceTable sourceTable = classView.getMutationSource();
        MutationSourceTableEntry entry = sourceTable.getDataEntries().get(ROW_KEY);

        assertThat(entry.getKey()).isEqualTo(ROW_KEY);
        assertThat(entry.getSourceCode()).contains("return Files.exists(Paths.get(fileName));");
        assertThat(entry.getMutationDetail()).contains("edu/hm/hafner/util/PathUtil::exists");
        assertThat(entry.getClickable().getText()).isEqualTo("2");
        assertThat(extractPageAnker(entry.getLink())).isEqualTo("def930cd_42");
    }

    @Test
    public void verifyClassComponentLink() {
        MutationDetailView classView = getClassView();

        ClassComponentTable classTable = classView.getComponentDetailTable();
        ClassComponentTableEntry entry = classTable.getDataEntries().get(0);
        MutationSourceTable sourceTable = classView.getMutationSource();

        String componentLink = extractPageAnker(entry.getLink());
        String sourceLink = extractPageAnker(sourceTable.getDataEntries().get(entry.getName()).getLink());

        assertThat(componentLink).isEqualTo(sourceLink);
    }

    @Test
    public void verifySorting() {
        MutationDetailView classView = getClassView();

        ClassComponentTable classTable = classView.getComponentDetailTable();

        assertFirstClassComponentTableEntry(classTable.getDataEntries().get(0));

        classView = classView.clickSorting(0);
        classView.initialize();
        classTable = classView.getComponentDetailTable();

        assertLastClassComponentTableEntry(classTable.getDataEntries().get(0));
    }

    @Test
    public void verifyComponents() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, EMPTY_ID);

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();
        ComponentTable baseTable = baseView.getComponentTable();

        assertThat(baseTable.getDataEntries().size()).isEqualTo(1);
        assertFirstComponentTableEntry(baseTable.getDataEntries().get(0), "Module: null");

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(FIRST_ROW_INDEX);
        moduleView.initialize();
        ComponentTable moduleTable = moduleView.getComponentTable();

        assertThat(moduleTable.getDataEntries().size()).isEqualTo(16);
        assertFirstComponentTableEntry(moduleTable.getDataEntries().get(0), "Package: edu.hm.hafner.util");

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(LAST_ROW_INDEX);
        packageView.initialize();
        ComponentTable packageTable = packageView.getComponentTable();

        assertThat(packageTable.getDataEntries().size()).isEqualTo(1);
        assertPackageComponentTableEntry(packageTable.getDataEntries().get(0));

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(FIRST_ROW_INDEX);
        classView.initialize();
        ClassComponentTable classTable = classView.getComponentDetailTable();

        assertThat(classTable.getDataEntries().size()).isEqualTo(27);
        assertFirstClassComponentTableEntry(classTable.getDataEntries().get(0));
    }


    @Test
    public void verifyMutationStatistics() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, EMPTY_ID);

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();
        MutationStatistics baseStatistics = baseView.getMutationStatistics();
        assertOverallStatistics(baseStatistics);

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(FIRST_ROW_INDEX);
        moduleView.initialize();
        MutationStatistics moduleStatistics = moduleView.getMutationStatistics();
        assertOverallStatistics(moduleStatistics);

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(LAST_ROW_INDEX);
        packageView.initialize();
        MutationStatistics packageStatistics = packageView.getMutationStatistics();
        assertSingleStatistics(packageStatistics);

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(FIRST_ROW_INDEX);
        classView.initialize();
        MutationStatistics classStatistics = classView.getMutationStatistics();
        assertSingleStatistics(classStatistics);
    }


    @Test
    public void verifyMutationHierarchy() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, EMPTY_ID);
        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(FIRST_ROW_INDEX);
        moduleView.initialize();

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(LAST_ROW_INDEX);
        packageView.initialize();

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(FIRST_ROW_INDEX);
        classView.initialize();

        assertThat(classView.getNavigation().getCurrentLevel()).isEqualTo("CLASS");
        assertThat(classView.getNavigation().getNavigationPoint(0).getName())
            .isEqualTo(FIRST_LEVEL);
        assertThat(classView.getNavigation().getNavigationPoint(1).getName())
            .isEqualTo(SECOND_LEVEL);
        assertThat(classView.getNavigation().getNavigationPoint(2).getName())
            .isEqualTo(THIRD_LEVEL);

        packageView = classView.navigatePreviousPage();
        packageView.initialize();

        assertThat(packageView.getNavigation().getCurrentLevel()).isEqualTo("PACKAGE");
        assertThat(packageView.getNavigation().getNavigationPoint(0).getName())
            .isEqualTo(FIRST_LEVEL);
        assertThat(packageView.getNavigation().getNavigationPoint(1).getName())
            .isEqualTo(SECOND_LEVEL);

        moduleView = packageView.navigatePreviousPage();
        moduleView.initialize();

        assertThat(moduleView.getNavigation().getCurrentLevel()).isEqualTo("MODULE");
        assertThat(moduleView.getNavigation().getNavigationPoint(0).getName())
            .isEqualTo(FIRST_LEVEL);

        baseView = moduleView.navigatePreviousPage();
        baseView.initialize();

        assertThat(baseView.getNavigation().getCurrentLevel()).isEqualTo("MODULES");
        assertThat(baseView.getNavigation().getNavigationPoints()).isEmpty();
    }


    private MutationDetailView getClassView() {
        Build build = createAndBuildWorkflowJob();

        DashboardView dashboardView = new DashboardView(build, EMPTY_ID);

        MutationTableView baseView = dashboardView.openPitMutationView();
        baseView.initialize();

        MutationTableView moduleView = (MutationTableView) baseView.clickRowLink(FIRST_ROW_INDEX);
        moduleView.initialize();

        MutationTableView packageView = (MutationTableView) moduleView.clickRowLink(LAST_ROW_INDEX);
        packageView.initialize();

        MutationDetailView classView = (MutationDetailView) packageView.clickRowLink(FIRST_ROW_INDEX);
        classView.initialize();

        return classView;
    }

    private String extractPageAnker(String link) {
        return link.substring(link.indexOf("@") + 1);
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
        assertThat(entry.getName()).isEqualTo(ROW_KEY);
        assertThat(entry.getMutations()).isEqualTo("2");
        assertThat(entry.getMutationsDelta()).isEqualTo("+ 2");
        assertThat(entry.getUndetected()).isEqualTo("0");
        assertThat(entry.getUndetectedDelta()).isEqualTo("0");
        assertThat(entry.getCoverage()).isEqualTo("100.0%");
        assertThat(entry.getCoverageDelta()).isEqualTo("+100.0%");
        assertThat(entry.getMutationDetail()).contains("BooleanTrueReturnVals", "BooleanFalseReturnVals");
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
}

