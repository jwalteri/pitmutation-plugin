package org.jenkins.plugins.pitmutation.tables.ClassComponents;

import org.jenkins.plugins.pitmutation.tables.Components.ComponentTableSorting;
import org.openqa.selenium.WebElement;

/**
 * Represents the table sorting of a component table on class level.
 */
public class ClassComponentTableSorting extends ComponentTableSorting {
    private final WebElement mutationInformation;

    /**
     * Ctor for a ClassComponentTableSorting.
     * @param name The clickable element for column name.
     * @param mutations The clickable element for column mutations.
     * @param mutationsDelta The clickable element for column mutationsDelta.
     * @param undetected The clickable element for column undetected.
     * @param undetectedDelta The clickable element for column undetectedDelta.
     * @param coverage The clickable element for column coverage.
     * @param coverageDelta The clickable element for column coverageDelta.
     * @param mutationInformation The clickable element for column mutation.
     */
    public ClassComponentTableSorting(WebElement name, WebElement mutations, WebElement mutationsDelta, WebElement undetected, WebElement undetectedDelta, WebElement coverage, WebElement coverageDelta, WebElement mutationInformation) {
        super(name, mutations, mutationsDelta, undetected, undetectedDelta, coverage, coverageDelta);
        this.mutationInformation = mutationInformation;
        headers.add(mutationInformation);
    }

    /**
     * Returns the clickable element of column mutation.
     *
     * @return The clickable element of column mutation.
     */
    public WebElement getMutationInformation() {
        return mutationInformation;
    }
}
