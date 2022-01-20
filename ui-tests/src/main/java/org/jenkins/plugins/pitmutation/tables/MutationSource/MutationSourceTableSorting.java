package org.jenkins.plugins.pitmutation.tables.MutationSource;

import org.jenkins.plugins.pitmutation.tables.Components.ComponentTableSorting;
import org.openqa.selenium.WebElement;

public class MutationSourceTableSorting extends ComponentTableSorting {
    private final WebElement mutationDetail;

    public MutationSourceTableSorting(WebElement name, WebElement mutations,
                                      WebElement mutationsDelta, WebElement undetected,
                                      WebElement undetectedDelta, WebElement coverage,
                                      WebElement coverageDelta, WebElement mutationDetail) {
        super(name, mutations, mutationsDelta, undetected, undetectedDelta,
            coverage, coverageDelta);
        this.mutationDetail = mutationDetail;
    }

    public WebElement getMutationDetail() {
        return mutationDetail;
    }
}
