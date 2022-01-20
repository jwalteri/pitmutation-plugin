package org.jenkins.plugins.pitmutation.Views.PitMutation;

import org.openqa.selenium.WebElement;

import java.util.List;

import static org.jenkinsci.test.acceptance.Matchers.by;

public class AbstractNavigation<T> {
    protected final static String H1_TAG = "h1";
    protected final static String SEPARATOR = ":";
    protected List<T> navigationPoints;
    protected String currentLevel;


    public String getCurrentLevel() {
        return currentLevel;
    }

    public List<T> getNavigationPoints() {
        return navigationPoints;
    }

    public boolean containsLevel(int level) {
        return level < navigationPoints.size() - 1;
    }

    protected String getHierarchyLevel(WebElement mainPanel) {
        String hierarchyLevel = mainPanel.findElement(by.tagName(H1_TAG)).getText();
        if (hierarchyLevel.contains(SEPARATOR)) {
            hierarchyLevel = hierarchyLevel.substring(0, hierarchyLevel.indexOf(SEPARATOR));
        }
        return hierarchyLevel.toUpperCase();
    }

    public T getNavigationPoint(int level) {
        return navigationPoints.size() > 0 ? navigationPoints.get(level) : null;
    }

    public T getPrevious() {
        return getNavigationPoint(navigationPoints.size() - 1);
    }




}
