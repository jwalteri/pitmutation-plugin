package org.jenkins.plugins.pitmutation.tables.MutationInformation;

import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkins.plugins.pitmutation.tables.AbstractMapTable;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Represents a mutation information table.
 */
public class MutationInformationTable extends AbstractMapTable<String, MutationInformationTableEntry> {

    /**
     * Ctor for MutationInformationTable.
     *
     * @param mutationInformationRows
     */
    public MutationInformationTable(List<WebElement> mutationInformationRows) {
        readData(mutationInformationRows);
    }

    @Override
    protected MutationInformationTableEntry rowToEntry(WebElement row) {
        List<WebElement> tds = WebElementUtils.getByTagName(row,WebElementUtils.TD_TAG);

        return new MutationInformationTableEntry(
            tds.get(0).getText(),
            WebElementUtils.getLink(tds.get(0)),
            tds.get(2).getText()
        );
    }
}
