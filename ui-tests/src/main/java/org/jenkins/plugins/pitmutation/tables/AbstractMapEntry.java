package org.jenkins.plugins.pitmutation.tables;

import org.openqa.selenium.WebElement;

/**
 * Represents an abstract map entry;
 * @param <K> The key.
 */
public class AbstractMapEntry<K> extends AbstractTableEntry {
    private final K key;

    /**
     * Ctor for AbstractMapEntry.
     * @param clickable The clickable element.
     * @param key The key.
     */
    public AbstractMapEntry(WebElement clickable, K key) {
        super(clickable);
        this.key = key;
    }

    /**
     * Returns the key.
     *
     * @return The key.
     */
    public K getKey() {
        return key;
    }
}
