package org.jenkins.plugins.pitmutation.tables;

import org.openqa.selenium.WebElement;

public abstract class AbstractTableEntry {
    private final WebElement clickable;
    private final static String LINK_ATTRIBUTE = "href";

    public AbstractTableEntry(WebElement clickable) {
        this.clickable = clickable;
    }

    public WebElement getClickable() {
        return clickable;
    }

    public String getLink() {
        return clickable.getAttribute(LINK_ATTRIBUTE);
    }
}
