package org.jenkins.plugins.pitmutation.tables.Components;

import org.jenkins.plugins.pitmutation.tables.AbstractTableEntry;
import org.openqa.selenium.WebElement;

public class ComponentTableEntry extends AbstractTableEntry {
    private final String name;
    private final String link;
    private final String mutations;
    private final String mutationsDelta;
    private final String undetected;
    private final String undetectedDelta;
    private final String coverage;
    private final String coverageDelta;

    public ComponentTableEntry(String name, String link, WebElement clickable, String mutations, String mutationsDelta,
                               String undetected, String undetectedDelta, String coverage,
                               String coverageDelta) {
        super(clickable);
        this.name = name;
        this.link = link;
        this.mutations = mutations;
        this.mutationsDelta = mutationsDelta;
        this.undetected = undetected;
        this.undetectedDelta = undetectedDelta;
        this.coverage = coverage;
        this.coverageDelta = coverageDelta;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getMutations() {
        return mutations;
    }

    public String getMutationsDelta() {
        return mutationsDelta;
    }

    public String getUndetected() {
        return undetected;
    }

    public String getUndetectedDelta() {
        return undetectedDelta;
    }

    public String getCoverage() {
        return coverage;
    }

    public String getCoverageDelta() {
        return coverageDelta;
    }
}
