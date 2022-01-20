package org.jenkins.plugins.pitmutation.tables.Components;

import org.jenkins.plugins.pitmutation.tables.AbstractTableEntry;
import org.openqa.selenium.WebElement;

/**
 * Represents a table entry of a component table.
 */
public class ComponentTableEntry extends AbstractTableEntry {
    private final String name;
    private final String link;
    private final String mutations;
    private final String mutationsDelta;
    private final String undetected;
    private final String undetectedDelta;
    private final String coverage;
    private final String coverageDelta;

    /**
     * Ctor for a ComponentTableEntry.
     *
     * @param name Content of column name.
     * @param link Content of column link.
     * @param clickable The clickable link element.
     * @param mutations Content of column mutations.
     * @param mutationsDelta Content of column mutationsDelta.
     * @param undetected Content of column undetected.
     * @param undetectedDelta Content of column undetectedDelta.
     * @param coverage Content of column coverage.
     * @param coverageDelta Content of column coverageDelta
     */
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

    /**
     * Returns the link as String.
     *
     * @return The link.
     */
    public String getLink() {
        return link;
    }

    /**
     * Returns the name of the entry.
     *
     * @return The name of the entry.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the amount of mutations.
     *
     * @return The amount of mutations.
     */
    public String getMutations() {
        return mutations;
    }

    /**
     * Returns the delta of mutations.
     *
     * @return The delta of mutations.
     */
    public String getMutationsDelta() {
        return mutationsDelta;
    }

    /**
     * Returns the amount of undetected.
     *
     * @return The amount of undetected.
     */
    public String getUndetected() {
        return undetected;
    }

    /**
     * Returns the delta of undetected.
     *
     * @return The delta of undetected.
     */
    public String getUndetectedDelta() {
        return undetectedDelta;
    }

    /**
     * Returns the coverage.
     *
     * @return The coverage.
     */
    public String getCoverage() {
        return coverage;
    }

    /**
     * Returns the delta of coverage.
     *
     * @return The delta of coverage.
     */
    public String getCoverageDelta() {
        return coverageDelta;
    }
}
