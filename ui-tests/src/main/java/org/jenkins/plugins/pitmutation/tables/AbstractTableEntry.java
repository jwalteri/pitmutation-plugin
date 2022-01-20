package org.jenkins.plugins.pitmutation.tables;

import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.openqa.selenium.WebElement;

/**
 * Represents an abstract table entry.
 */
public abstract class AbstractTableEntry {
    private final WebElement clickable;

    /**
     * Ctor for AbstractTableEntry.
     * @param clickable The clickable element.
     */
    public AbstractTableEntry(WebElement clickable) {
        this.clickable = clickable;
    }

    /**
     * Returns the clickable element.
     * @return The clickable element.
     */
    public WebElement getClickable() {
        return clickable;
    }

    /**
     * Returns the Link of the clickable element.
     * @return The link of the clickable element.
     */
    public String getLink() {
        return WebElementUtils.getAttribute(clickable, WebElementUtils.LINK_ATTRIBUTE);
    }
}
