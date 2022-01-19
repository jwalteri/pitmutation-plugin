package org.jenkins.plugins.pitmutation;

import com.google.inject.Injector;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MutationDetailView extends PageObject {
    private WebElement mutationDetailTableView;
    private WebElement statisticsTable;
    private WebElement componentsDetailTable;
    private MutationInformationTable mutationInformation;
    private MutationSourceTable mutationSource;
    private MutationStatistics mutationStatistics;
    private ComponentDetailTable componentDetail;
    private List<String> activeMutators;
    private List<String> testsExamined;
    private MutationNavigation navigation;

    public MutationDetailView(Injector injector, URL url) {
        super(injector, url);
        this.open();
    }

    public MutationDetailView(final Injector injector, final URL url, final String id) {
        super(injector, url);
    }

    public MutationInformationTable getMutationInformation() {
        return mutationInformation;
    }

    public MutationSourceTable getMutationSource() {
        return mutationSource;
    }

    public void initialize() {
        this.mutationDetailTableView = this.getElement(by.tagName("body"));
        initializeTables();
        mutationStatistics = new MutationStatistics(this.statisticsTable);
        componentDetail = new ComponentDetailTable(componentsDetailTable);

        List<WebElement> uls = mutationDetailTableView.findElements(by.tagName("ul"));
        activeMutators = extractValuesFromUnorderedList(uls.get(2));
        testsExamined = extractValuesFromUnorderedList(uls.get(3));
        navigation = new MutationNavigation(mutationDetailTableView);
    }

    private List<String> extractValuesFromUnorderedList(WebElement unorderedList) {
        List<WebElement> listEntry = unorderedList.findElements(by.tagName("li"));

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

    public ComponentDetailTable getComponentDetailTable() {
        return componentDetail;
    }

    public MutationStatistics getMutationStatistics() {
        return mutationStatistics;
    }

    public PageObject clickRowLink(int rowIndex) {
        // TODO: todo
        return null;
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
        List<WebElement> tables = this.mutationDetailTableView.findElements(by.tagName("table"));

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

        List<WebElement> trs = mutationSourceTable.findElements(by.tagName("tr"));
        OptionalInt splitterCell = IntStream.range(0, trs.size())
            .filter(x -> trs.get(x).findElements(by.tagName("td")).get(0).getText()
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

    private <T extends PageObject> T openPage(final WebElement link, final Class<T> type) {
        String href = link.getAttribute("href");
        T result = newInstance(type, injector, url(href), "");
        link.click();

        return result;
    }
}
