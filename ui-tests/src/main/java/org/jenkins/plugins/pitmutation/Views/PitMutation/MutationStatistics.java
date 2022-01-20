package org.jenkins.plugins.pitmutation.Views.PitMutation;

import org.openqa.selenium.WebElement;

import java.util.List;

import static org.jenkinsci.test.acceptance.Matchers.by;

public class MutationStatistics {
    private StatisticsEntry mutations;
    private StatisticsEntry undetected;
    private StatisticsEntry coverage;
    private final WebElement statisticsView;

    public MutationStatistics(WebElement statisticsView) {
        this.statisticsView = statisticsView;
        readStatistics();
    }

    private void readStatistics() {
        List<WebElement> row = this.statisticsView.findElements(by.tagName("tr"));

        if (row.size() != 2) {
            throw new IllegalArgumentException("Only two row expected in statistics table!");
        }

        List<WebElement> names = row.get(0).findElements(by.tagName("th"));
        List<WebElement> values = row.get(1).findElements(by.tagName("td"));

        mutations = new StatisticsEntry(names.get(0).getText(), values.get(0).getText());
        undetected = new StatisticsEntry(names.get(1).getText(), values.get(1).getText());
        coverage = new StatisticsEntry(names.get(2).getText(), values.get(2).getText());
    }

    public StatisticsEntry getMutations() {
        return mutations;
    }

    public StatisticsEntry getUndetected() {
        return undetected;
    }

    public StatisticsEntry getCoverage() {
        return coverage;
    }

    public static class StatisticsEntry {
        private final String name;
        private final String value;

        public StatisticsEntry(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
