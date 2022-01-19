package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static org.jenkinsci.test.acceptance.Matchers.by;

public class MutationNavigation {
    private final List<MutationNavigationPoint> navigationPoints;
    private final String currentLevel;

    public MutationNavigation(WebElement body) {
        WebElement mainPanel = body.findElement(by.id("main-panel"));

        currentLevel = getHierarchyLevel(mainPanel);
        NavigationHierarchy hierarchy = NavigationHierarchy.valueOf(currentLevel);

        List<WebElement> links = mainPanel.findElements(by.tagName("a"));

        List<WebElement> hierarchyLevels = links.subList(1, hierarchy.getDepth() + 1);

        navigationPoints = hierarchyLevels.stream()
            .map(MutationNavigationPoint::new)
            .collect(Collectors.toList());
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public List<MutationNavigationPoint> getNavigationPoints() {
        return navigationPoints;
    }

    public boolean containsLevel(int level) {
        return level < navigationPoints.size() - 1;
    }

    public MutationNavigationPoint getNavigationPoint(int level) {
        return navigationPoints.size() > 0 ? navigationPoints.get(level) : null;
    }

    public MutationNavigationPoint getPrevious() {
        return getNavigationPoint(navigationPoints.size() - 1);
    }

    private String getHierarchyLevel(WebElement mainPanel) {
        String hierarchyLevel = mainPanel.findElement(by.tagName("h1")).getText();
        if (hierarchyLevel.contains(":")) {
            hierarchyLevel = hierarchyLevel.substring(0, hierarchyLevel.indexOf(":"));
        }
        return hierarchyLevel.toUpperCase();
    }

    public static class MutationNavigationPoint {
        private final WebElement clickable;
        private final String name;

        public MutationNavigationPoint(WebElement clickable) {
            this.clickable = clickable;
            this.name = this.clickable.getText();
        }

        public WebElement getClickable() {
            return clickable;
        }

        public String getName() {
            return name;
        }
    }

    public enum NavigationHierarchy {
        MODULES("Modules", 0),
        MODULE("Module", 1),
        PACKAGE("Package", 2),
        CLASS("Class", 3);

        private final String value;
        private final int depth;

        NavigationHierarchy(String value, int depth) {
            this.value = value;
            this.depth = depth;
        }

        public int getDepth() {
            return depth;
        }

        public String getValue() {
            return value;
        }
    }
}
