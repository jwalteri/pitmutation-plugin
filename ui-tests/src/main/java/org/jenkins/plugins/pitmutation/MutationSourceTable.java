package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jenkinsci.test.acceptance.Matchers.by;

public class MutationSourceTable extends GenericUiTable<String, MutationSourceTableEntry> {
    private final Map<String, MutationSourceTableEntry> entries = new HashMap<>();

    public MutationSourceTable(List<WebElement> mutationSourceRows) {
        readData(mutationSourceRows);
    }

    @Override
    public void readData(List<WebElement> dataRows) {
        for (WebElement row : dataRows) {
            MutationSourceTableEntry entry = rowToEntry(row);
            entries.put(entry.getKey(), entry);
        }
    }

    public Map<String, MutationSourceTableEntry> getEntries() {
        return entries;
    }


    private MutationSourceTableEntry rowToEntry(WebElement row) {
        List<WebElement> tds = row.findElements(by.tagName("td"));

        return new MutationSourceTableEntry(
            tds.get(0).getText(),
            tds.get(1).getText(),
            tds.get(2).getText(),
            tds.get(1).findElement(by.tagName("a"))
        );
    }

}
