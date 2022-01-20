package org.jenkins.plugins.pitmutation.Views.PitMutation;

import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class MutationNavigation extends AbstractNavigation<MutationNavigation.MutationNavigationPoint> {

    public MutationNavigation(WebElement body) {
        WebElement mainPanel = WebElementUtils.getById(body, WebElementUtils.MAIN_PANEL_ID);

        currentLevel = getHierarchyLevel(mainPanel);
        NavigationHierarchy hierarchy = NavigationHierarchy.valueOf(currentLevel);

        List<WebElement> links = WebElementUtils.getByTagName(mainPanel, WebElementUtils.LINK_TAG);

        List<WebElement> hierarchyLevels = links.subList(1, hierarchy.getDepth() + 1);

        navigationPoints = hierarchyLevels.stream()
            .map(MutationNavigationPoint::new)
            .collect(Collectors.toList());
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
