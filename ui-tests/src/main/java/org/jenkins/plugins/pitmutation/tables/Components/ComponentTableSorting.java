package org.jenkins.plugins.pitmutation.tables.Components;

import org.jenkins.plugins.pitmutation.tables.AbstractTableSorting;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

/**
 * Represents the table sorting of a component table.
 */
public class ComponentTableSorting extends AbstractTableSorting {
    private final WebElement name;
    private final WebElement mutations;
    private final WebElement mutationsDelta;
    private final WebElement undetected;
    private final WebElement undetectedDelta;
    private final WebElement coverage;
    private final WebElement coverageDelta;


    /**
     * Ctor for a ComponentTableSorting.
     * @param name The clickable element for column name.
     * @param mutations The clickable element for column mutations.
     * @param mutationsDelta The clickable element for column mutationsDelta.
     * @param undetected The clickable element for column undetected.
     * @param undetectedDelta The clickable element for column undetectedDelta.
     * @param coverage The clickable element for column coverage.
     * @param coverageDelta The clickable element for column coverageDelta.
     */
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

    /**
     * Returns the clickable WebElement of column name.
     * @return The clickable WebElement of column name.
     */
    public WebElement getName() {
        return name;
    }

    /**
     * Returns the clickable WebElement of column mutations.
     * @return The clickable WebElement of column mutations.
     */
    public WebElement getMutations() {
        return mutations;
    }

    /**
     * Returns the clickable WebElement of column mutationsDelta.
     * @return The clickable WebElement of column mutationsDelta.
     */
    public WebElement getMutationsDelta() {
        return mutationsDelta;
    }

    /**
     * Returns the clickable WebElement of column undetected.
     * @return The clickable WebElement of column undetected.
     */
    public WebElement getUndetected() {
        return undetected;
    }

    /**
     * Returns the clickable WebElement of column undetectedDelta.
     * @return The clickable WebElement of column undetectedDelta.
     */
    public WebElement getUndetectedDelta() {
        return undetectedDelta;
    }

    /**
     * Returns the clickable WebElement of column coverage.
     * @return The clickable WebElement of column coverage.
     */
    public WebElement getCoverage() {
        return coverage;
    }

    /**
     * Returns the clickable WebElement of column coverageDelta.
     * @return The clickable WebElement of column coverageDelta.
     */
    public WebElement getCoverageDelta() {
        return coverageDelta;
    }
}
