package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.jenkinsci.test.acceptance.Matchers.by;

public class ComponentDetailTable {
    private ComponentTableSorting sorting;
    private final List<ComponentDetailTableEntry> componentDetailTableEntries = new ArrayList<>();

    public ComponentDetailTable(WebElement componentsTable) {
        List<WebElement> rows = componentsTable.findElements(by.tagName("tr"));
        readComponentTableHeader(rows);
        readComponentTableData(rows);
    }

    public ComponentTableSorting getSorting() {
        return sorting;
    }

    public List<ComponentDetailTableEntry> getComponentDetailTableEntries() {
        return componentDetailTableEntries;
    }

    private void readComponentTableHeader(List<WebElement> rows) {
        if (!rows.isEmpty()) {
            List<WebElement> headers = rows.get(0).findElements(by.tagName("th"));
            this.sorting = new ComponentDetailTableSorting(
                headers.get(0).findElement(by.tagName("a")),
                headers.get(1).findElement(by.tagName("a")),
                headers.get(2).findElement(by.tagName("a")),
                headers.get(3).findElement(by.tagName("a")),
                headers.get(4).findElement(by.tagName("a")),
                headers.get(5).findElement(by.tagName("a")),
                headers.get(6).findElement(by.tagName("a")),
                headers.get(6).findElement(by.tagName("a"))
            );
        }
    }

    private void readComponentTableData(List<WebElement> rows)  {
        rows.remove(0);
        for (WebElement row : rows) {
            List<WebElement> tds = row.findElements(by.tagName("td"));

            if (!tds.isEmpty()) {
                componentDetailTableEntries.add(new ComponentDetailTableEntry(
                    tds.get(0).getText(),
                    row.findElement(by.tagName("a")).getAttribute("href"),
                    row.findElement(by.tagName("a")),
                    tds.get(1).getText(),
                    tds.get(2).getText(),
                    tds.get(3).getText(),
                    tds.get(4).getText(),
                    tds.get(5).getText(),
                    tds.get(6).getText(),
                    tds.get(7).getText()
                ));
            }
        }
    }
}
