package org.jenkins.plugins.pitmutation;

import org.openqa.selenium.WebElement;

import java.util.List;

import static org.jenkinsci.test.acceptance.Matchers.by;

public class WebElementUtils {
    public final static String LINK_TAG = "a";
    public final static String LINK_ATTRIBUTE = "href";
    public final static String BODY_TAG = "body";
    public final static String TABLE_TAG = "table";
    public final static String ROW_TAG = "tr";
    public final static String TD_TAG = "td";
    public final static String LI_TAG = "li";
    public final static String UL_TAG = "ul";
    public final static String MAIN_PANEL_ID = "main-panel";

    public static WebElement getLink(WebElement input) {
        return input.findElement(by.tagName(LINK_TAG));
    }

    public static List<WebElement> getByTagName(WebElement input, String tagName) {
        return input.findElements(by.tagName(tagName));
    }

    public static String getAttribute(WebElement input, String attributeName) {
        return input.getAttribute(attributeName);
    }

    public static WebElement getById(WebElement input, String id) {
        return input.findElement(by.id(id));
    }
}
