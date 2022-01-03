package org.jenkins.plugins.pitmutation;

import com.google.errorprone.annotations.MustBeClosed;
import com.google.inject.Injector;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class FirstClass extends PageObject {
    private final String id;

    public FirstClass(final Build parent, final String id) {
        super(parent, parent.url(id));
        parent.open();
        WebElement test = getElement(By.tagName("body"));



        this.id = id;
    }

    /**
     * Provides file system operations using real IO.
     */
    private static final class DefaultFileSystem {

        @MustBeClosed
        public InputStream open(final String fileName) throws IOException {
            return Files.newInputStream(Paths.get(fileName));
        }
    }

    public Collection<Tab> getAvailableTabs() {
        return all(By.xpath("//a[@role='tab']")).stream()
            .map(tab -> tab.getAttribute("href"))
            .map(this::extractRelativeUrl)
            .map(Tab::valueWithHref)
            .collect(Collectors.toList());
    }

    public Tab getActiveTab() {
        WebElement activeTab = find(By.xpath("//a[@role='tab' and contains(@class, 'active')]"));

        return Tab.valueWithHref(extractRelativeUrl(activeTab.getAttribute("href")));
    }

    private String extractRelativeUrl(final String absoluteUrl) {
        return "#" + StringUtils.substringAfterLast(absoluteUrl, "#");
    }

    public enum Tab {
        TOOLS("origin"),
        MODULES("moduleName"),
        PACKAGES("packageName"),
        FOLDERS("folder"),
        FILES("fileName"),
        CATEGORIES("category"),
        TYPES("type"),
        ISSUES("issues"),
        BLAMES("blames"),
        FORENSICS("forensics");

        private final String contentId;
        private final String property;

        Tab(final String property) {
            this.property = property;
            contentId = property + "Content";
        }

        /**
         * Returns the selenium {@link By} selector to find the specific tab.
         *
         * @return the selenium filter rule
         */
        By getXpath() {
            return By.xpath("//a[@href='#" + contentId + "']");
        }

        /**
         * Returns the enum element that has the specified href property.
         *
         * @param href
         *         the href to select the tab
         *
         * @return the tab
         * @throws NoSuchElementException
         *         if the tab could not be found
         */
        static Tab valueWithHref(final String href) {
            for (Tab tab : Tab.values()) {
                if (tab.contentId.equals(href.substring(1))) {
                    return tab;
                }
            }
            throw new NoSuchElementException("No such tab with href " + href);
        }
    }
}
