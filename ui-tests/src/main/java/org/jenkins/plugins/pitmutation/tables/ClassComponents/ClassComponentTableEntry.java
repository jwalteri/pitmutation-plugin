package org.jenkins.plugins.pitmutation.tables.ClassComponents;

import org.jenkins.plugins.pitmutation.tables.Components.ComponentTableEntry;
import org.openqa.selenium.WebElement;

public class ClassComponentTableEntry extends ComponentTableEntry {
    private final String mutationDetail;

    public ClassComponentTableEntry(String name, String link, WebElement clickable,
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
