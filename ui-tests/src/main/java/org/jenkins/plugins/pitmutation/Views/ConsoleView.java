package org.jenkins.plugins.pitmutation.Views;

import com.google.inject.Injector;
import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.net.URL;

/**
 * Represents the console view of the PitMutation Plugin.
 */
public class ConsoleView extends PageObject {

    /**
     * Ctor for ConsoleView.
     *
     * @param parent The build.
     * @param id The id.
     */
    public ConsoleView(final Build parent, String id) {
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
    public ConsoleView(final Injector injector, final URL url, final String id) {
        super(injector, url);
    }

    /**
     * Returns the output of the console.
     *
     * @return The console output.
     */
    public String getConsoleOutput() {
       return this.find(by.id(WebElementUtils.MAIN_PANEL_ID)).getText();
    }
}
