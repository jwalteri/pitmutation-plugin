package org.jenkins.plugins.pitmutation.tables;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTableSorting {
    protected List<WebElement> headers;

    public AbstractTableSorting(List<WebElement> headers) {
        this.headers = new ArrayList<>(headers);
    }

    public List<WebElement> getHeaders() {
        return headers;
    }
}
