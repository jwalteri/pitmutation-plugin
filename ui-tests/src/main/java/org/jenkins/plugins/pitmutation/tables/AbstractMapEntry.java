package org.jenkins.plugins.pitmutation.tables;

import org.openqa.selenium.WebElement;

public class AbstractMapEntry<K> extends AbstractTableEntry {
    private final K key;

    public AbstractMapEntry(WebElement clickable, K key) {
        super(clickable);
        this.key = key;
    }

    public K getKey() {
        return key;
    }
}
