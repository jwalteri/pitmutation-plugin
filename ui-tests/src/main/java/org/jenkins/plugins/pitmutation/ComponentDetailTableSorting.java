package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

import java.util.List;

public class ComponentDetailTableSorting extends ComponentTableSorting {
    private final WebElement mutationInformation;

    public ComponentDetailTableSorting(WebElement name, WebElement mutations, WebElement mutationsDelta, WebElement undetected, WebElement undetectedDelta, WebElement coverage, WebElement coverageDelta, WebElement mutationInformation) {
        super(name, mutations, mutationsDelta, undetected, undetectedDelta, coverage, coverageDelta);
        this.mutationInformation = mutationInformation;

        List<WebElement> headers = this.getHeaders();
        headers.add(mutationInformation);
        super.setHeaders(headers);
    }

    public WebElement getMutationInformation() {
        return mutationInformation;
    }
}
