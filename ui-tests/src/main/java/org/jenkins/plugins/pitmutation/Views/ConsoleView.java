package org.jenkins.plugins.pitmutation.Views;

import com.google.inject.Injector;
import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.net.URL;

public class ConsoleView extends PageObject {

    public ConsoleView(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
    }

    public ConsoleView(final Injector injector, final URL url, final String id) {
        super(injector, url);
    }

    public void initConsoleView() {
        WebElement console = this.find(by.id(WebElementUtils.MAIN_PANEL_ID));
    }

    public String getConsoleOutput() {
       return this.find(by.id(WebElementUtils.MAIN_PANEL_ID)).getText();
    }
}
