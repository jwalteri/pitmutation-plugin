package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ComponentTableSorting {
    private final WebElement name;
    private final WebElement mutations;
    private final WebElement mutationsDelta;
    private final WebElement undetected;
    private final WebElement undetectedDelta;
    private final WebElement coverage;
    private final WebElement coverageDelta;
    private List<WebElement> headers;

    public ComponentTableSorting(WebElement name, WebElement mutations,
                                 WebElement mutationsDelta, WebElement undetected,
                                 WebElement undetectedDelta, WebElement coverage,
                                 WebElement coverageDelta) {
        this.name = name;
        this.mutations = mutations;
        this.mutationsDelta = mutationsDelta;
        this.undetected = undetected;
        this.undetectedDelta = undetectedDelta;
        this.coverage = coverage;
        this.coverageDelta = coverageDelta;
        headers = new ArrayList<>();
        headers.add(name);
        headers.add(mutations);
        headers.add(mutationsDelta);
        headers.add(undetected);
        headers.add(undetectedDelta);
        headers.add(coverage);
        headers.add(coverageDelta);
    }

    public void setHeaders(List<WebElement> headers) {
        this.headers = headers;
    }

    public List<WebElement> getHeaders() {
        return headers;
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
