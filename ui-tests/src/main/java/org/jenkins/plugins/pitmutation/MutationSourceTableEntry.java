package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

public class MutationSourceTableEntry extends GenericUiTableEntry<String> {
    private final String mutationDetail;
    private final String sourceCode;
    private final WebElement mutationInformationId;

    public MutationSourceTableEntry(String codeLine, String mutationDetail,
                                    String sourceCode, WebElement mutationInformationId) {
        super(codeLine);
        this.mutationDetail = mutationDetail;
        this.sourceCode = sourceCode;
        this.mutationInformationId = mutationInformationId;
    }

    public String getMutationDetail() {
        return mutationDetail;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public WebElement getMutationInformationId() {
        return mutationInformationId;
    }
}
