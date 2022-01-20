package org.jenkins.plugins.pitmutation.tables;

import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an abstract key - value table.
 * @param <K> The key.
 * @param <V> The value.
 */
public abstract class AbstractMapTable<K, V extends AbstractMapEntry<K>> extends AbstractTable {
    protected Map<K, V> dataEntries = new HashMap<>();

    /**
     * Returns the data entries.
     *
     * @return The data entries.
     */
    public Map<K, V> getDataEntries() {
        return dataEntries;
    }

    /**
     * Transforms the data of table rows to table entries.
     *
     * @param dataRows The data rows.
     */
    protected void readData(List<WebElement> dataRows) {
        for (WebElement row : dataRows) {
            V entry = rowToEntry(row);
            dataEntries.put(entry.getKey(), entry);
        }
    }

    /**
     * Method to transform a row to a table entry.
     *
     * @param row The data row.
     * @return A table enty.
     */
    protected abstract V rowToEntry(WebElement row);
}
