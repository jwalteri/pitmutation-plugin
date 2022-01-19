package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

public abstract class GenericUiTableEntry<K> {
    private final K key;

    public GenericUiTableEntry(K key) {
        this.key = key;
    }

    public K getKey() {
        return key;
    }
}
