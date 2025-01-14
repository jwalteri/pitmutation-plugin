package org.jenkins.plugins.pitmutation.tables.MutationSource;

import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkins.plugins.pitmutation.tables.AbstractMapTable;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Represents a mutation source table.
 */
public class MutationSourceTable extends AbstractMapTable<String, MutationSourceTableEntry> {

    /**
     * Ctor for MutationSourceTable.
     * @param mutationSourceRows The data rows.
     */
    public MutationSourceTable(List<WebElement> mutationSourceRows) {
        readData(mutationSourceRows);
    }

    @Override
    protected MutationSourceTableEntry rowToEntry(WebElement row) {
        List<WebElement> tds = WebElementUtils.getByTagName(row,WebElementUtils.TD_TAG);

        return new MutationSourceTableEntry(
            tds.get(0).getText(),
            tds.get(1).getText(),
            tds.get(2).getText(),
            WebElementUtils.getLink(tds.get(1))
        );
    }

}
