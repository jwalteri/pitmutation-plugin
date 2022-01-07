package org.jenkins.plugins.pitmutation;

import com.google.inject.Injector;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.net.URL;

public class UiTestObject extends PageObject {
    protected UiTestObject(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
        WebElement page = this.getElement(by.tagName("body"));

        System.out.println(page.getText());
    }
}
