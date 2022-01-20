package org.jenkins.plugins.pitmutation.tables.MutationSource;

import org.jenkins.plugins.pitmutation.tables.AbstractMapEntry;
import org.openqa.selenium.WebElement;

/**
 * Represtens a table entry for a mutation source table.
 */
public class MutationSourceTableEntry extends AbstractMapEntry<String> {
    private final String mutationDetail;
    private final String sourceCode;

    /**
     * Ctor for MutationSourceTableEntry.
     * @param codeLine The code line (key) of the row.
     * @param mutationDetail The mutation detail of the row.
     * @param sourceCode The source code of the row.
     * @param clickable The cliclable element of the row.
     */
    public MutationSourceTableEntry(String codeLine, String mutationDetail,
                                    String sourceCode, WebElement clickable) {
        super(clickable, codeLine);
        this.mutationDetail = mutationDetail;
        this.sourceCode = sourceCode;
    }

    /**
     * Returns the mutation detail.
     *
     * @return The mutation detail.
     */
    public String getMutationDetail() {
        return mutationDetail;
    }

    /**
     * Returns the source code.
     *
     * @return The source code.
     */
    public String getSourceCode() {
        return sourceCode;
    }
}
