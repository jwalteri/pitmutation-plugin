package org.jenkins.plugins.pitmutation.Views;

import com.google.inject.Injector;
import org.jenkins.plugins.pitmutation.Views.PitMutation.MutationNavigation;
import org.jenkins.plugins.pitmutation.Views.PitMutation.MutationStatistics;
import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkins.plugins.pitmutation.tables.ClassComponents.ClassComponentTable;
import org.jenkins.plugins.pitmutation.tables.MutationInformation.MutationInformationTable;
import org.jenkins.plugins.pitmutation.tables.MutationSource.MutationSourceTable;
import org.jenkinsci.test.acceptance.po.Build;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MutationDetailView extends AbstractView {
    private WebElement mutationDetailTableView;
    private WebElement statisticsTable;
    private WebElement componentsDetailTable;
    private MutationInformationTable mutationInformation;
    private MutationSourceTable mutationSource;
    private MutationStatistics mutationStatistics;
    private ClassComponentTable componentDetail;
    private List<String> activeMutators;
    private List<String> testsExamined;
    private MutationNavigation navigation;

    public MutationDetailView(Build parent, String id) {
        super(parent, id);
    }

    public MutationDetailView(final Injector injector, final URL url, final String id) {
        super(injector, url, id);
    }

    public MutationInformationTable getMutationInformation() {
        return mutationInformation;
    }

    public MutationSourceTable getMutationSource() {
        return mutationSource;
    }

    @Override
    public void initialize() {
        this.mutationDetailTableView = getBody();
        initializeTables();
        mutationStatistics = new MutationStatistics(this.statisticsTable);
        componentDetail = new ClassComponentTable(componentsDetailTable);

        List<WebElement> uls = WebElementUtils.getByTagName(mutationDetailTableView, WebElementUtils.UL_TAG);
        activeMutators = extractValuesFromUnorderedList(uls.get(2));
        testsExamined = extractValuesFromUnorderedList(uls.get(3));
        navigation = new MutationNavigation(mutationDetailTableView);
    }

    private List<String> extractValuesFromUnorderedList(WebElement unorderedList) {
        List<WebElement> listEntry = WebElementUtils.getByTagName(unorderedList, WebElementUtils.LI_TAG);

        return listEntry.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public MutationNavigation getNavigation() {
        return navigation;
    }

    public List<String> getActiveMutators() {
        return activeMutators;
    }

    public List<String> getTestsExamined() {
        return testsExamined;
    }

    public ClassComponentTable getComponentDetailTable() {
        return componentDetail;
    }

    public MutationStatistics getMutationStatistics() {
        return mutationStatistics;
    }

    public MutationTableView navigatePreviousPage() {
        return openPage(navigation.getPrevious().getClickable(), MutationTableView.class);
    }

    public MutationTableView navigateHierarchyLevel(int level) {
        if (!navigation.containsLevel(level)) {
            throw new IllegalArgumentException("Navigation hierarchy level " + level + " not found!");
        }
        return openPage(navigation.getNavigationPoint(level).getClickable(), MutationTableView.class);
    }

    public MutationDetailView clickSorting(int colIndex) {
        return openPage(componentDetail.getSorting().getHeaders().get(colIndex), MutationDetailView.class);
    }

    private void initializeTables() {
        List<WebElement> tables = WebElementUtils.getByTagName(mutationDetailTableView, WebElementUtils.TABLE_TAG);

        OptionalInt componentsIndexOpt = IntStream.range(0, tables.size())
            .filter(x -> tables.get(x).getAttribute("class").equals("pane sortable"))
            .findFirst();

        OptionalInt sourceIndexOpt = IntStream.range(0, tables.size())
            .filter(x -> tables.get(x).getAttribute("class").equals("src"))
            .findFirst();

        if (!componentsIndexOpt.isPresent() || !sourceIndexOpt.isPresent()) {
            throw new NullPointerException("Components Table not found!");
        }

        int componentsIndex = componentsIndexOpt.getAsInt();
        int sourceIndex = sourceIndexOpt.getAsInt();

        this.componentsDetailTable = tables.get(componentsIndex);
        this.statisticsTable = tables.get(3 - componentsIndex - sourceIndex);
        WebElement mutationSourceTable = tables.get(sourceIndex);

        // Splitte mutationSourceTable

        List<WebElement> trs = WebElementUtils.getByTagName(mutationSourceTable, WebElementUtils.ROW_TAG);
        OptionalInt splitterCell = IntStream.range(0, trs.size())
            .filter(x -> WebElementUtils.getByTagName(trs.get(x), WebElementUtils.TD_TAG).get(0).getText()
                .isEmpty())
            .findFirst();

        if (!splitterCell.isPresent()) {
            throw new NullPointerException("Splitter cell not found!");
        }

        int splitterCellIndex = splitterCell.getAsInt();

        List<WebElement> rowsForSource = trs.subList(0,  splitterCellIndex- 1);
        List<WebElement> rowsForInformation = trs.subList(splitterCellIndex + 1,  trs.size());

        mutationInformation = new MutationInformationTable(rowsForInformation);
        mutationSource = new MutationSourceTable(rowsForSource);
    }
}
