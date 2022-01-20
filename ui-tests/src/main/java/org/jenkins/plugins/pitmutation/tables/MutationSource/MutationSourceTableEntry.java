package org.jenkins.plugins.pitmutation.tables.MutationSource;

import org.jenkins.plugins.pitmutation.tables.AbstractMapEntry;
import org.openqa.selenium.WebElement;

public class MutationSourceTableEntry extends AbstractMapEntry<String> {
    private final String mutationDetail;
    private final String sourceCode;

    public MutationSourceTableEntry(String codeLine, String mutationDetail,
                                    String sourceCode, WebElement clickable) {
        super(clickable, codeLine);
        this.mutationDetail = mutationDetail;
        this.sourceCode = sourceCode;
    }

    public String getMutationDetail() {
        return mutationDetail;
    }

    public String getSourceCode() {
        return sourceCode;
    }
}
