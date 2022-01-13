package org.jenkins.plugins.pitmutation;

import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class PitMutationView extends PageObject {
    private WebElement dashboard;

    protected PitMutationView(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
        this.dashboard = this.getElement(by.tagName("body"));
    }

    public String getConsoleOutput() {
        return this.getElement(by.id("main-panel")).getText();
    }
}
