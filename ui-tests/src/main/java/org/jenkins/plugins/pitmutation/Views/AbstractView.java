package org.jenkins.plugins.pitmutation.Views;

import com.google.inject.Injector;
import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.net.URL;

/**
 * Represents an abstract view.
 */
public abstract class AbstractView extends PageObject {
    protected final static String EMPTY_ID = "";

    /**
     * Ctor for AbstractView.
     *
     * @param parent The build.
     * @param id The id.
     */
    public AbstractView(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
    }

    /**
     * Special Ctor for AbstractView.
     * Needed for openPage Method.
     *
     * @param injector The injector.
     * @param url The url.
     * @param id The id.
     */
    public AbstractView(final Injector injector, final URL url, final String id) {
        super(injector, url);
    }

    /**
     * Returns the body WebElement of the current view.
     *
     * @return The body.
     */
    protected WebElement getBody() {
        return this.getElement(by.tagName(WebElementUtils.BODY_TAG));
    }

    /**
     * Opens a new page.
     * The WebElement has to contain a link.
     * The opened page is read into the given type.
     * The given type has to implement the special ctor with injector parameter.
     *
     * @param link The WebElement containing the link.
     * @param type The type to read the page to.
     * @param <T> The type.
     * @return Returns the opened page as type.
     */
    protected  <T extends PageObject> T openPage(final WebElement link, final Class<T> type) {
        String href = link.getAttribute(WebElementUtils.LINK_ATTRIBUTE);
        T result = newInstance(type, injector, url(href), EMPTY_ID);
        link.click();

        return result;
    }

    /**
     * Initializes the new page.
     * Can't be called in ctor for unknown reasons.
     * Pages are not really usable without the initialization call.
     */
    public abstract void initialize();
}
