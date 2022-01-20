package org.jenkins.plugins.pitmutation.Views;

import com.google.inject.Injector;
import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.net.URL;

public abstract class AbstractView extends PageObject {
    protected final static String EMPTY_ID = "";

    public AbstractView(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
    }

    public AbstractView(final Injector injector, final URL url, final String id) {
        super(injector, url);
    }

    protected WebElement getBody() {
        return this.getElement(by.tagName(WebElementUtils.BODY_TAG));
    }

    protected  <T extends PageObject> T openPage(final WebElement link, final Class<T> type) {
        String href = link.getAttribute(WebElementUtils.LINK_ATTRIBUTE);
        T result = newInstance(type, injector, url(href), EMPTY_ID);
        link.click();

        return result;
    }

    public abstract void initialize();
}
