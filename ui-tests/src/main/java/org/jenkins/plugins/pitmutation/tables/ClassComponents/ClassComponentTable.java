package org.jenkins.plugins.pitmutation.tables.ClassComponents;

import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkins.plugins.pitmutation.tables.AbstractListTable;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Represents the component table on class level.
 */
public class ClassComponentTable extends AbstractListTable<ClassComponentTableEntry> {

    /**
     * Ctor for a ClassComponentTable.
     *
     * @param componentsTable WebElement of the corresponding component table.
     */
    public ClassComponentTable(WebElement componentsTable) {
        List<WebElement> rows = WebElementUtils.getByTagName(componentsTable, WebElementUtils.TR_TAG);
        readComponentTableHeader(rows);
        readComponentTableData(rows);
    }

    /**
     * Reads the table headers of the table. Interprets the first row as table header row.
     *
     * @param rows The table rows.
     */
    private void readComponentTableHeader(List<WebElement> rows) {
        if (!rows.isEmpty()) {
            List<WebElement> headers = WebElementUtils.getByTagName(rows.get(0), WebElementUtils.TH_TAG);
            sorting = new ClassComponentTableSorting(
                WebElementUtils.getLink(headers.get(0)),
                WebElementUtils.getLink(headers.get(1)),
                WebElementUtils.getLink(headers.get(2)),
                WebElementUtils.getLink(headers.get(3)),
                WebElementUtils.getLink(headers.get(4)),
                WebElementUtils.getLink(headers.get(5)),
                WebElementUtils.getLink(headers.get(6)),
                WebElementUtils.getLink(headers.get(7))
            );
        }
    }

    /**
     * Reads the data of the table. Interprets the first row as table header row and so removes it.
     *
     * @param rows The table rows.
     */
    private void readComponentTableData(List<WebElement> rows)  {
        rows.remove(0);
        for (WebElement row : rows) {
            List<WebElement> tds = WebElementUtils.getByTagName(row, WebElementUtils.TD_TAG);

            if (!tds.isEmpty()) {
                dataEntries.add(new ClassComponentTableEntry(
                    tds.get(0).getText(),
                    WebElementUtils.getAttribute(WebElementUtils.getLink(row), WebElementUtils.LINK_ATTRIBUTE),
                    WebElementUtils.getLink(row),
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
