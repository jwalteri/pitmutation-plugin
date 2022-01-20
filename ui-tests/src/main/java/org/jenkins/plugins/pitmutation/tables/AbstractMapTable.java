package org.jenkins.plugins.pitmutation.tables;

import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapTable<K, V extends AbstractMapEntry<K>> extends AbstractTable {
    protected Map<K, V> dataEntries = new HashMap<>();

    public Map<K, V> getDataEntries() {
        return dataEntries;
    }

    protected void readData(List<WebElement> dataRows) {
        for (WebElement row : dataRows) {
            V entry = rowToEntry(row);
            dataEntries.put(entry.getKey(), entry);
        }
    }

    protected abstract V rowToEntry(WebElement row);
}
