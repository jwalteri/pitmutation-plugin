package org.jenkins.plugins.pitmutation.tables.MutationInformation;

import org.jenkins.plugins.pitmutation.tables.AbstractMapEntry;
import org.openqa.selenium.WebElement;

/**
 * Represents a table entry of a mutation information table.
 */
public class MutationInformationTableEntry extends AbstractMapEntry<String> {
    private final String mutationInformation;

    /**
     * Ctor for MutationInformationTableEntry.
     *
     * @param key The key of the row.
     * @param clickable The clickable element of the row.
     * @param mutationInformation The mutation information of the row.
     */
    public MutationInformationTableEntry(String key, WebElement clickable, String mutationInformation) {
        super(clickable,key);
        this.mutationInformation = mutationInformation;
    }

    /**
     * Returns the mutation information row.
     *
     * @return The mutation information.
     */
    public String getMutationInformation() {
        return mutationInformation;
    }
}
