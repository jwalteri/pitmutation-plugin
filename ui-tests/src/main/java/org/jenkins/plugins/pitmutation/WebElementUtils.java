package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

import java.util.List;

import static org.jenkinsci.test.acceptance.Matchers.by;

/**
 * Represents utils for WebElements.
 */
public class WebElementUtils {
    public final static String LINK_TAG = "a";
    public final static String LINK_ATTRIBUTE = "href";
    public final static String BODY_TAG = "body";
    public final static String TABLE_TAG = "table";
    public final static String TR_TAG = "tr";
    public final static String TD_TAG = "td";
    public final static String TH_TAG = "th";
    public final static String LI_TAG = "li";
    public final static String UL_TAG = "ul";
    public final static String MAIN_PANEL_ID = "main-panel";

    /**
     * Returns the first WebElement containing a link tag.
     *
     * @param input The WebElement which contains a link tag.
     * @return The link tag.
     */
    public static WebElement getLink(WebElement input) {
        return input.findElement(by.tagName(LINK_TAG));
    }

    /**
     * Returns all WebElements with a specific tag.
     *
     * @param input WebElement which contains the searched WebElements.
     * @param tagName The tag name.
     * @return List of WebElements with the specific tag name.
     */
    public static List<WebElement> getByTagName(WebElement input, String tagName) {
        return input.findElements(by.tagName(tagName));
    }

    /**
     * Returns a specific attribute of a WebElement.
     *
     * @param input The WebElement.
     * @param attributeName The name of the attribute.
     * @return The value of the attribute.
     */
    public static String getAttribute(WebElement input, String attributeName) {
        return input.getAttribute(attributeName);
    }

    /**
     * Returns a specific WebElement of a WebElement.
     *
     * @param input The WebElement which contains.
     * @param id The id of the contained WebElement.
     * @return The specific WebElement.
     */
    public static WebElement getById(WebElement input, String id) {
        return input.findElement(by.id(id));
    }
}
