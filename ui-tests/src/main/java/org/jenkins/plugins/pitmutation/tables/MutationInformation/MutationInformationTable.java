package org.jenkins.plugins.pitmutation.tables.MutationInformation;

import org.jenkins.plugins.pitmutation.tables.AbstractMapTable;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MutationInformationTable extends AbstractMapTable<String, MutationInformationTableEntry> {

    public MutationInformationTable(List<WebElement> mutationInformationRows) {
        readData(mutationInformationRows);
    }

    protected MutationInformationTableEntry rowToEntry(WebElement row) {
        List<WebElement> tds = getTableCells(row);

        return new MutationInformationTableEntry(
            tds.get(0).getText(),
            getLink(tds.get(0)),
            tds.get(2).getText()
        );
    }
}
