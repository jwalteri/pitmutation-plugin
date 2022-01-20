package org.jenkins.plugins.pitmutation.tables.MutationSource;

import org.jenkins.plugins.pitmutation.tables.AbstractMapTable;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MutationSourceTable extends AbstractMapTable<String, MutationSourceTableEntry> {

    public MutationSourceTable(List<WebElement> mutationSourceRows) {
        readData(mutationSourceRows);
    }

    protected MutationSourceTableEntry rowToEntry(WebElement row) {
        List<WebElement> tds = getTableCells(row);

        return new MutationSourceTableEntry(
            tds.get(0).getText(),
            tds.get(1).getText(),
            tds.get(2).getText(),
            getLink(tds.get(1))
        );
    }

}