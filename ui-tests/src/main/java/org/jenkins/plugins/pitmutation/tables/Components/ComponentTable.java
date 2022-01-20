package org.jenkins.plugins.pitmutation.tables.Components;

import org.jenkins.plugins.pitmutation.tables.AbstractListTable;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ComponentTable extends AbstractListTable<ComponentTableEntry> {

    public ComponentTable(WebElement componentsTable) {

        List<WebElement> rows = getRows(componentsTable);
        readComponentTableHeader(rows);
        readComponentTableData(rows);
    }

    private void readComponentTableHeader(List<WebElement> rows) {
        if (!rows.isEmpty()) {
            List<WebElement> headers = getTableHeaders(rows.get(0));
            sorting = new ComponentTableSorting(
                getLink(headers.get(0)),
                getLink(headers.get(1)),
                getLink(headers.get(2)),
                getLink(headers.get(3)),
                getLink(headers.get(4)),
                getLink(headers.get(5)),
                getLink(headers.get(6))
            );
        }
    }

    private void readComponentTableData(List<WebElement> rows)  {
        rows.remove(0);
        for (WebElement row : rows) {
            List<WebElement> tds = getTableCells(row);

            if (!tds.isEmpty()) {
                dataEntries.add(new ComponentTableEntry(
                    tds.get(0).getText(),
                    getLinkTarget(row),
                    getLink(row),
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
}
