package org.jenkins.plugins.pitmutation.Views;

import com.google.inject.Injector;
import org.jenkins.plugins.pitmutation.Views.PitMutation.MutationNavigation;
import org.jenkins.plugins.pitmutation.Views.PitMutation.MutationStatistics;
import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkins.plugins.pitmutation.tables.Components.ComponentTable;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * Represents a page of the PitMutation Plugin.
 */
public class MutationTableView extends AbstractView {
    private WebElement statisticsTable;
    private WebElement componentsTable;
    private WebElement mutationTableView;
    private ComponentTable componentTable;
    private MutationStatistics mutationStatistics;
    private MutationNavigation navigation;

    /**
     * Ctor for MutationTableView.
     *
     * @param parent The build.
     * @param id The id.
     */
    protected MutationTableView(final Build parent, String id) {
        super(parent, id);
        this.open();
    }

    /**
     * Special Ctor for MutationTableView.
     * Needed for openPage Method.
     *
     * @param injector The injector.
     * @param url The url.
     * @param id The id.
     */
    public MutationTableView(final Injector injector, final URL url, final String id) {
        super(injector, url, id);
    }

    /**
     * Returns the component table.
     *
     * @return The component table.
     */
    public ComponentTable getComponentTable() {
        return componentTable;
    }

    /**
     * Returns the mutation statistics.
     *
     * @return The mutation statistics.
     */
    public MutationStatistics getMutationStatistics() {
        return mutationStatistics;
    }

    /**
     * Opens the view of a row entry.
     *
     * @param rowIndex The index of the row.
     * @return Returns a new page.
     */
    public AbstractView clickRowLink(int rowIndex) {
        if (navigation.getCurrentLevel().equals(MutationNavigation.NavigationHierarchy.PACKAGE.getValue().toUpperCase())) {
            return openPage(componentTable.getDataEntries()
                .get(rowIndex).getClickable(), MutationDetailView.class);

        } else {
            return openPage(componentTable.getDataEntries()
                .get(rowIndex).getClickable(), MutationTableView.class);
        }
    }

    /**
     * Returns the navigation.
     *
     * @return The navigation.
     */
    public MutationNavigation getNavigation() {
        return navigation;
    }

    /**
     * Opens the previous page.
     *
     * @return The previous page.
     */
    public MutationTableView navigatePreviousPage() {
        return openPage(navigation.getPrevious().getClickable(), MutationTableView.class);
    }

    /**
     * Opens a specific page by clicking in the hierarchy level.
     *
     * @param level The level.
     * @return The corresponding page.
     */
    public MutationTableView navigateHierarchyLevel(int level) {
        if (!navigation.containsLevel(level)) {
            throw new IllegalArgumentException("Navigation hierarchy level " + level + " not found!");
        }
        return openPage(navigation.getNavigationPoint(level).getClickable(), MutationTableView.class);
    }

    /**
     * Sorts the component table and returns the new page.
     *
     * @param colIndex The column to sort.
     * @return The new page.
     */
    public MutationTableView clickSorting(int colIndex) {
        return openPage(componentTable.getSorting().getHeaders().get(colIndex), MutationTableView.class);
    }

    @Override
    public void initialize() {
        this.mutationTableView = getBody();
        initializeTables();
        mutationStatistics = new MutationStatistics(this.statisticsTable);
        componentTable = new ComponentTable(componentsTable);
        navigation = new MutationNavigation(mutationTableView);
    }

    /**
     * Initializes the component and statistics table.
     */
    private void initializeTables() {
        List<WebElement> tables = WebElementUtils.getByTagName(mutationTableView, WebElementUtils.TABLE_TAG);

        OptionalInt componentsIndexOpt = IntStream.range(0, tables.size())
            .filter(x -> tables.get(x).getAttribute("class").equals("pane sortable"))
            .findFirst();

        if (!componentsIndexOpt.isPresent()) {
            throw new NullPointerException("Components Table not found!");
        }

        int componentsIndex = componentsIndexOpt.getAsInt();

        this.componentsTable = tables.get(componentsIndex);
        this.statisticsTable = tables.get(1 - componentsIndex);
    }
}
