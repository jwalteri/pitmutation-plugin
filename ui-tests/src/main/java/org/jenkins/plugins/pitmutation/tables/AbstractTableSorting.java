package org.jenkins.plugins.pitmutation.tables;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract table sorting.
 */
public abstract class AbstractTableSorting {
    protected List<WebElement> headers;

    /**
     * Ctor for AbstractTableSorting.
     * @param headers The sorting headers.
     */
    public AbstractTableSorting(List<WebElement> headers) {
        this.headers = new ArrayList<>(headers);
    }

    /**
     * Returns the sorting headers.
     *
     * @return The sorting headers.
     */
    public List<WebElement> getHeaders() {
        return headers;
    }
}
