package org.jenkins.plugins.pitmutation.Views.PitMutation;

import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the navigation of the PitMutation Plugin site.
 */
public class MutationNavigation extends AbstractNavigation<MutationNavigation.MutationNavigationPoint> {

    /**
     * Ctor for MutationNavigation.
     *
     * @param body The body of the PitMutation Plugin.
     */
    public MutationNavigation(WebElement body) {
        WebElement mainPanel = WebElementUtils.getById(body, WebElementUtils.MAIN_PANEL_ID);

        currentLevel = readHierarchyLevel(mainPanel);
        NavigationHierarchy hierarchy = NavigationHierarchy.valueOf(currentLevel);

        List<WebElement> links = WebElementUtils.getByTagName(mainPanel, WebElementUtils.LINK_TAG);

        List<WebElement> hierarchyLevels = links.subList(1, hierarchy.getDepth() + 1);

        navigationPoints = hierarchyLevels.stream()
            .map(MutationNavigationPoint::new)
            .collect(Collectors.toList());
    }

    /**
     * Represents navigation points of the PitMutation Plugin site.
     */
    public static class MutationNavigationPoint {
        private final WebElement clickable;
        private final String name;

        /**
         * Ctor for MutationNavigationPoint.
         *
         * @param clickable The clickable element of the navigation point.
         */
        public MutationNavigationPoint(WebElement clickable) {
            this.clickable = clickable;
            this.name = this.clickable.getText();
        }

        /**
         * Returns the clickable element of the navigation point.
         *
         * @return The clickable element of the navigation point.
         */
        public WebElement getClickable() {
            return clickable;
        }

        /**
         * Returns the name of the navigation point.
         * @return The name of the navigation point.
         */
        public String getName() {
            return name;
        }
    }

    /**
     * Represents the hierarchy levels of the PitMutation Plugin site.
     */
    public enum NavigationHierarchy {
        MODULES("Modules", 0),
        MODULE("Module", 1),
        PACKAGE("Package", 2),
        CLASS("Class", 3);

        private final String value;
        private final int depth;

        /**
         * Ctor for NavigationHierarchy.
         * @param value The value.
         * @param depth The navigation depth.
         */
        NavigationHierarchy(String value, int depth) {
            this.value = value;
            this.depth = depth;
        }

        /**
         * Returns the depth.
         *
         * @return The depth.
         */
        public int getDepth() {
            return depth;
        }

        /**
         * Returns the value.
         *
         * @return The value.
         */
        public String getValue() {
            return value;
        }
    }

}
