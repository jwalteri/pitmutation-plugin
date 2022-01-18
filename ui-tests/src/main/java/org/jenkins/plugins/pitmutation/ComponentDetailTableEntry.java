package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

public class ComponentDetailTableEntry extends ComponentTableEntry {
    private final String mutationDetail;

    public ComponentDetailTableEntry(String name, String link, WebElement clickable,
                                     String mutations, String mutationsDelta, String undetected,
                                     String undetectedDelta, String coverage,
                                     String coverageDelta, String mutationDetail) {
        super(name, link, clickable, mutations, mutationsDelta, undetected, undetectedDelta,
            coverage, coverageDelta);
        this.mutationDetail = mutationDetail;
    }

    public String getMutationDetail() {
        return mutationDetail;
    }
}
