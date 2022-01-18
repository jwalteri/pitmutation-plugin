package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

public class MutationInformationTableEntry extends GenericUiTableEntry<String> {
    private final WebElement clickable;
    private final String mutationInformation;

    public MutationInformationTableEntry(String key, WebElement clickable, String mutationInformation) {
        super(key);
        this.clickable = clickable;
        this.mutationInformation = mutationInformation;
    }

    public WebElement getClickable() {
        return clickable;
    }

    public String getMutationInformation() {
        return mutationInformation;
    }
}
