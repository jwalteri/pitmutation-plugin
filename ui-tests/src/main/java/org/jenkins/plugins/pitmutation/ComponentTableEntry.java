package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

public class ComponentTableEntry {
    private final String name;
    private final String link;
    private final WebElement clickable;
    private final String mutations;
    private final String mutationsDelta;
    private final String undetected;
    private final String undetectedDelta;
    private final String coverage;
    private final String coverageDelta;

    public ComponentTableEntry(String name, String link, WebElement clickable, String mutations, String mutationsDelta,
                               String undetected, String undetectedDelta, String coverage,
                               String coverageDelta) {
        this.name = name;
        this.link = link;
        this.clickable = clickable;
        this.mutations = mutations;
        this.mutationsDelta = mutationsDelta;
        this.undetected = undetected;
        this.undetectedDelta = undetectedDelta;
        this.coverage = coverage;
        this.coverageDelta = coverageDelta;
    }

    public WebElement getClickable() {
        return clickable;
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
