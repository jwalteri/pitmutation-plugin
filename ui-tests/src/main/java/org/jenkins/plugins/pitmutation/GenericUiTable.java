package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericUiTable<K, V extends GenericUiTableEntry<K>> {
    private final Map<K, V> entries = new HashMap<>();

    public Map<K, V> getEntries() {
        return entries;
    }

    public abstract void readData(List<WebElement> dataRows);
}
