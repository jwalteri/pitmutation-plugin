package org.jenkins.plugins.pitmutation;

import com.google.inject.Injector;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.net.URL;

public class UiTestObject2 extends PageObject {
        private WebElement page;

    public UiTestObject2(final Build parent, String id) {
            super(parent, parent.url(id));
            this.open();
            page = this.getElement(by.tagName("out"));

            System.out.println(page.getText());
        }

    public UiTestObject2(final Injector injector, final URL url, final String id) {
        super(injector, url);
    }

    public WebElement getConsoleOutput() {
        return page.findElement(by.id("out"));
    }
}
