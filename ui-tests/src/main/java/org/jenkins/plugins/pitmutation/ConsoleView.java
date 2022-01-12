package org.jenkins.plugins.pitmutation;

import com.google.inject.Injector;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.net.URL;

public class ConsoleView extends PageObject {
    private WebElement console;

    public ConsoleView(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
    }

    public ConsoleView(final Injector injector, final URL url, final String id) {
        super(injector, url);
    }

    public void initConsoleView() {
        this.console = this.getElement(by.id("main-panel"));
    }

    public String getConsoleOutput() {
        return this.getElement(by.id("main-panel")).getText();
    }
}
