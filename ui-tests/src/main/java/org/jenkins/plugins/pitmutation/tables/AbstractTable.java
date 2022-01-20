package org.jenkins.plugins.pitmutation.tables;

import org.openqa.selenium.WebElement;

import java.util.List;

import static org.jenkinsci.test.acceptance.Matchers.by;

public abstract class AbstractTable {
    private final static String LINK_TAG = "a";
    private final static String TABLE_ROW_TAG = "tr";
    private final static String TABLE_HEADER_TAG = "th";
    private final static String TABLE_DATA_TAG = "td";
    private final static String LINK_TARGET_ATTRIBUTE = "href";

    protected AbstractTableSorting sorting;

    public AbstractTableSorting getSorting() {
        return sorting;
    }

    protected List<WebElement> getRows(WebElement input) {
        return getByTagName(input, TABLE_ROW_TAG);
    }

    protected WebElement getLink(WebElement tableCell) {
        return tableCell.findElement(by.tagName(LINK_TAG));
    }

    private List<WebElement> getByTagName(WebElement row, String tagName) {
        return row.findElements(by.tagName(tagName));
    }

    protected String getLinkTarget(WebElement container) {
        return getLink(container).getAttribute(LINK_TARGET_ATTRIBUTE);
    }

    protected List<WebElement> getTableHeaders(WebElement headerRow) {
        return getByTagName(headerRow, TABLE_HEADER_TAG);
    }

    protected List<WebElement> getTableCells(WebElement dataRow) {
        return getByTagName(dataRow, TABLE_DATA_TAG);
    }


}
