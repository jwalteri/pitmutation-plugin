package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jenkinsci.test.acceptance.Matchers.by;

public class MutationInformationTable extends GenericUiTable<String, MutationInformationTableEntry> {
    private final Map<String, MutationInformationTableEntry> entries = new HashMap<>();

    public MutationInformationTable(List<WebElement> mutationInformationRows) {
        readData(mutationInformationRows);
    }

    @Override
    public Map<String, MutationInformationTableEntry> getEntries() {
        return entries;
    }

    @Override
    public void readData(List<WebElement> dataRows) {
        for (WebElement row : dataRows) {
            MutationInformationTableEntry entry = rowToEntry(row);
            entries.put(entry.getKey(), entry);
        }
    }

    private MutationInformationTableEntry rowToEntry(WebElement row) {
        List<WebElement> tds = row.findElements(by.tagName("td"));

        return new MutationInformationTableEntry(
            tds.get(0).getText(),
            tds.get(0),
            tds.get(2).getText()
        );
    }
}
