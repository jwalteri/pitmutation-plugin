package org.jenkins.plugins.pitmutation.tables.ClassComponents;

import org.jenkins.plugins.pitmutation.tables.Components.ComponentTableEntry;
import org.openqa.selenium.WebElement;

/**
 * Represents a table entry of the component table on class level.
 */
public class ClassComponentTableEntry extends ComponentTableEntry {
    private final String mutationDetail;

    /**
     * Ctor for a ClassComponentTableEntry.
     *
     * @param name Content of column name.
     * @param link Content of column link.
     * @param clickable The clickable link element.
     * @param mutations Content of column mutations.
     * @param mutationsDelta Content of column mutationsDelta.
     * @param undetected Content of column undetected.
     * @param undetectedDelta Content of column undetectedDelta.
     * @param coverage Content of column coverage.
     * @param coverageDelta Content of column coverageDelta.
     * @param mutationDetail Content of column mutations.
     */
    public ClassComponentTableEntry(String name, String link, WebElement clickable,
                                    String mutations, String mutationsDelta, String undetected,
                                    String undetectedDelta, String coverage,
                                    String coverageDelta, String mutationDetail) {
        super(name, link, clickable, mutations, mutationsDelta, undetected, undetectedDelta,
            coverage, coverageDelta);
        this.mutationDetail = mutationDetail;
    }

    /**
     * Returns the content of the column mutations.
     *
     * @return The mutation.
     */
    public String getMutationDetail() {
        return mutationDetail;
    }
}
