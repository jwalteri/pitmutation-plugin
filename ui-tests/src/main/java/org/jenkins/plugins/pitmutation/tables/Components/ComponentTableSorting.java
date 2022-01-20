package org.jenkins.plugins.pitmutation.tables.Components;

import org.jenkins.plugins.pitmutation.tables.AbstractTableSorting;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

public class ComponentTableSorting extends AbstractTableSorting {
    private final WebElement name;
    private final WebElement mutations;
    private final WebElement mutationsDelta;
    private final WebElement undetected;
    private final WebElement undetectedDelta;
    private final WebElement coverage;
    private final WebElement coverageDelta;

    public ComponentTableSorting(WebElement name, WebElement mutations,
                                 WebElement mutationsDelta, WebElement undetected,
                                 WebElement undetectedDelta, WebElement coverage,
                                 WebElement coverageDelta) {
        super(Arrays.asList(name, mutations, mutationsDelta, undetected, undetectedDelta, coverage, coverageDelta));
        this.name = name;
        this.mutations = mutations;
        this.mutationsDelta = mutationsDelta;
        this.undetected = undetected;
        this.undetectedDelta = undetectedDelta;
        this.coverage = coverage;
        this.coverageDelta = coverageDelta;

    }

    public WebElement getName() {
        return name;
    }

    public WebElement getMutations() {
        return mutations;
    }

    public WebElement getMutationsDelta() {
        return mutationsDelta;
    }

    public WebElement getUndetected() {
        return undetected;
    }

    public WebElement getUndetectedDelta() {
        return undetectedDelta;
    }

    public WebElement getCoverage() {
        return coverage;
    }

    public WebElement getCoverageDelta() {
        return coverageDelta;
    }
}
