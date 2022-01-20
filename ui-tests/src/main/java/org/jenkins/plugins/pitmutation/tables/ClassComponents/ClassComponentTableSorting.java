package org.jenkins.plugins.pitmutation.tables.ClassComponents;

import org.jenkins.plugins.pitmutation.tables.Components.ComponentTableSorting;
import org.openqa.selenium.WebElement;

public class ClassComponentTableSorting extends ComponentTableSorting {
    private final WebElement mutationInformation;

    public ClassComponentTableSorting(WebElement name, WebElement mutations, WebElement mutationsDelta, WebElement undetected, WebElement undetectedDelta, WebElement coverage, WebElement coverageDelta, WebElement mutationInformation) {
        super(name, mutations, mutationsDelta, undetected, undetectedDelta, coverage, coverageDelta);
        this.mutationInformation = mutationInformation;
        headers.add(mutationInformation);
    }

    public WebElement getMutationInformation() {
        return mutationInformation;
    }
}
