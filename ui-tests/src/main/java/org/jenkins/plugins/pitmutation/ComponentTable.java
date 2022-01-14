package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.jenkinsci.test.acceptance.Matchers.by;

public class ComponentTable {
    private ComponentTableSorting sorting;
    private final List<ComponentTableEntry> componentTableEntries = new ArrayList<>();

    public ComponentTable(WebElement componentsTable) {
        List<WebElement> rows = componentsTable.findElements(by.tagName("tr"));
        readComponentTableHeader(rows);
        readComponentTableData(rows);
    }

    public ComponentTableSorting getSorting() {
        return sorting;
    }

    public List<ComponentTableEntry> getComponentTableEntries() {
        return componentTableEntries;
    }

    private void readComponentTableHeader(List<WebElement> rows) {
        if (!rows.isEmpty()) {
            List<WebElement> headers = rows.get(0).findElements(by.tagName("th"));
            this.sorting = new ComponentTableSorting(
                headers.get(0).findElement(by.tagName("a")),
                headers.get(1).findElement(by.tagName("a")),
                headers.get(2).findElement(by.tagName("a")),
                headers.get(3).findElement(by.tagName("a")),
                headers.get(4).findElement(by.tagName("a")),
                headers.get(5).findElement(by.tagName("a")),
                headers.get(6).findElement(by.tagName("a"))
            );
        }
    }

    private void readComponentTableData(List<WebElement> rows)  {
        rows.remove(0);
        for (WebElement row : rows) {
            List<WebElement> tds = row.findElements(by.tagName("td"));

            if (!tds.isEmpty()) {
                componentTableEntries.add(new ComponentTableEntry(
                    tds.get(0).getText(),
                    row.findElement(by.tagName("a")).getAttribute("href"),
                    row.findElement(by.tagName("a")),
                    tds.get(1).getText(),
                    tds.get(2).getText(),
                    tds.get(3).getText(),
                    tds.get(4).getText(),
                    tds.get(5).getText(),
                    tds.get(6).getText()
                ));
            }
        }
    }

    public static class ComponentTableSorting {
        private final WebElement name;
        private final WebElement mutations;
        private final WebElement mutationsDelta;
        private final WebElement undetected;
        private final WebElement undetectedDelta;
        private final WebElement coverage;
        private final WebElement coverageDelta;
        private final List<WebElement> headers;

        public ComponentTableSorting(WebElement name, WebElement mutations,
                                     WebElement mutationsDelta, WebElement undetected,
                                     WebElement undetectedDelta, WebElement coverage,
                                     WebElement coverageDelta) {
            this.name = name;
            this.mutations = mutations;
            this.mutationsDelta = mutationsDelta;
            this.undetected = undetected;
            this.undetectedDelta = undetectedDelta;
            this.coverage = coverage;
            this.coverageDelta = coverageDelta;
            headers = new ArrayList<>();
            headers.add(name);
            headers.add(mutations);
            headers.add(mutationsDelta);
            headers.add(undetected);
            headers.add(undetectedDelta);
            headers.add(coverage);
            headers.add(coverageDelta);
        }

        public List<WebElement> getHeaders() {
            return headers;
        }

        public WebElement getName() {
            return name;
        }

        public WebElement getMutations() {
            return mutations;
        }

        public WebElement getMutationsDelta() {
            return mutationsDelta;
        }

        public WebElement getUndetected() {
            return undetected;
        }

        public WebElement getUndetectedDelta() {
            return undetectedDelta;
        }

        public WebElement getCoverage() {
            return coverage;
        }

        public WebElement getCoverageDelta() {
            return coverageDelta;
        }
    }

    public static class ComponentTableEntry {
        private final String name;
        private final String link;
        private final WebElement clickable;
        private final String mutations;
        private final String mutationsDelta;
        private final String undetected;
        private final String undetectedDelta;
        private final String coverage;
        private final String coverageDelta;

        public ComponentTableEntry(String name, String link, WebElement clickable, String mutations, String mutationsDelta,
                                   String undetected, String undetectedDelta, String coverage,
                                   String coverageDelta) {
            this.name = name;
            this.link = link;
            this.clickable = clickable;
            this.mutations = mutations;
            this.mutationsDelta = mutationsDelta;
            this.undetected = undetected;
            this.undetectedDelta = undetectedDelta;
            this.coverage = coverage;
            this.coverageDelta = coverageDelta;
        }

        public WebElement getClickable() {
            return clickable;
        }

        public String getLink() {
            return link;
        }

        public String getName() {
            return name;
        }

        public String getMutations() {
            return mutations;
        }

        public String getMutationsDelta() {
            return mutationsDelta;
        }

        public String getUndetected() {
            return undetected;
        }

        public String getUndetectedDelta() {
            return undetectedDelta;
        }

        public String getCoverage() {
            return coverage;
        }

        public String getCoverageDelta() {
            return coverageDelta;
        }
    }
}
