package org.jenkins.plugins.pitmutation.tables.MutationInformation;

import org.jenkins.plugins.pitmutation.tables.AbstractMapEntry;
import org.openqa.selenium.WebElement;

public class MutationInformationTableEntry extends AbstractMapEntry<String> {
    private final String mutationInformation;

    public MutationInformationTableEntry(String key, WebElement clickable, String mutationInformation) {
        super(clickable,key);
        this.mutationInformation = mutationInformation;
    }

    public String getMutationInformation() {
        return mutationInformation;
    }
}
