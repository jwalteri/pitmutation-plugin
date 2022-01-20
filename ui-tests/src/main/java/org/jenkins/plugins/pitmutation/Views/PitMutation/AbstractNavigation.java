package org.jenkins.plugins.pitmutation.Views.PitMutation;

import org.openqa.selenium.WebElement;

import java.util.List;

import static org.jenkinsci.test.acceptance.Matchers.by;

/**
 * Represents an abstract navigation.
 * @param <T> The type of navigation point.
 */
public class AbstractNavigation<T> {
    protected final static String H1_TAG = "h1";
    protected final static String SEPARATOR = ":";
    protected List<T> navigationPoints;
    protected String currentLevel;

    /**
     * Returns the current navigation level.
     *
     * @return The current navigation level.
     */
    public String getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Returns the navigation points.
     * @return The navigation points.
     */
    public List<T> getNavigationPoints() {
        return navigationPoints;
    }

    /**
     * Checeks if the level exists in the navigation.
     *
     * @param level The level.
     * @return Returns true if level exists otherwise false.
     */
    public boolean containsLevel(int level) {
        return level < navigationPoints.size() - 1;
    }

    /**
     * Reads the hierarchy level.
     *
     * @param mainPanel The WebElement where to find the hierarchy level.
     * @return
     */
    protected String readHierarchyLevel(WebElement mainPanel) {
        String hierarchyLevel = mainPanel.findElement(by.tagName(H1_TAG)).getText();
        if (hierarchyLevel.contains(SEPARATOR)) {
            hierarchyLevel = hierarchyLevel.substring(0, hierarchyLevel.indexOf(SEPARATOR));
        }
        return hierarchyLevel.toUpperCase();
    }

    /**
     * Returns the navigation point corresponding to the level.
     * @param level The level.
     * @return Returns the corresponding navigation point. Otherwise, null!
     */
    public T getNavigationPoint(int level) {
        return navigationPoints.size() > 0 ? navigationPoints.get(level) : null;
    }

    /**
     * Returns the navigation point of the previous level.
     * @return The navigation point of the previous level.
     */
    public T getPrevious() {
        return getNavigationPoint(navigationPoints.size() - 1);
    }




}
