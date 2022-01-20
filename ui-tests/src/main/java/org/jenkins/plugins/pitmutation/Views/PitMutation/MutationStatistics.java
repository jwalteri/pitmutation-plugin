package org.jenkins.plugins.pitmutation.Views.PitMutation;

import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.jenkinsci.test.acceptance.Matchers.by;

/**
 * Represents the mutation statistics of the Pit Mutation Plugin site.
 */
public class MutationStatistics {
    private StatisticsEntry mutations;
    private StatisticsEntry undetected;
    private StatisticsEntry coverage;
    private final WebElement statisticsView;

    /**
     * Ctor for MutationStatistics.
     *
     * @param statisticsView The WebElement containing the statistics.
     */
    public MutationStatistics(WebElement statisticsView) {
        this.statisticsView = statisticsView;
        readStatistics();
    }

    /**
     * Reads the data of the mutation statistics.
     */
    private void readStatistics() {
        List<WebElement> row = WebElementUtils.getByTagName(statisticsView, WebElementUtils.TR_TAG);

        if (row.size() != 2) {
            throw new IllegalArgumentException("Only two row expected in statistics table!");
        }

        List<WebElement> names = row.get(0).findElements(by.tagName("th"));
        List<WebElement> values = row.get(1).findElements(by.tagName("td"));

        mutations = new StatisticsEntry(names.get(0).getText(), values.get(0).getText());
        undetected = new StatisticsEntry(names.get(1).getText(), values.get(1).getText());
        coverage = new StatisticsEntry(names.get(2).getText(), values.get(2).getText());
    }

    /**
     * Returns the mutations.
     *
     * @return The mutations.
     */
    public StatisticsEntry getMutations() {
        return mutations;
    }

    /**
     * Returns the undetected.
     *
     * @return The undetected.
     */
    public StatisticsEntry getUndetected() {
        return undetected;
    }

    /**
     * Returns the coverage.
     *
     * @return The coverage.
     */
    public StatisticsEntry getCoverage() {
        return coverage;
    }

    /**
     * Represents entries of the statistic.
     */
    public static class StatisticsEntry {
        private final String name;
        private final String value;

        /**
         * Ctor for StatisticsEntry.
         *
         * @param name The name.
         * @param value The value.
         */
        public StatisticsEntry(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Returns the name.
         *
         * @return The name.
         */
        public String getName() {
            return name;
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
