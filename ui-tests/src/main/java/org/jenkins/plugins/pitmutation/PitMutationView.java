package org.jenkins.plugins.pitmutation;

import com.google.inject.Injector;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.net.URL;

public class PitMutationView extends PageObject {
    private WebElement dashboard;

    protected PitMutationView(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
        this.dashboard = this.getElement(by.tagName("body"));
    }

    public PitMutationView(final Injector injector, final URL url, final String id) {
        super(injector, url);
    }

    public String getConsoleOutput() {
        return this.getElement(by.id("main-panel")).getText();
    }
}
