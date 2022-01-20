package org.jenkins.plugins.pitmutation.Views;

import org.jenkins.plugins.pitmutation.Views.PitMutation.MutationTableView;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class DashboardView extends PageObject {

    public DashboardView(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
    }

    private WebElement getBody() {
        return this.getElement(by.tagName("body"));
    }

    private WebElement getPitMutationLink() {
        driver.navigate().refresh();
        Optional<WebElement> consoleOutput = getBody().findElements(by.tagName("a")).stream()
            .filter(x -> x.getText().contains("PIT Mutation Report"))
            .findFirst();

        if (!consoleOutput.isPresent()) {
            throw new NullPointerException("PIT Mutation Report Button not found!");
        }

        return consoleOutput.get();
    }

    private WebElement getConsoleOutputLink() {
        driver.navigate().refresh();
        Optional<WebElement> consoleOutput = getBody().findElements(by.tagName("a")).stream()
            .filter(x -> x.getText().contains("Console Output"))
            .findFirst();

        if (!consoleOutput.isPresent()) {
            throw new NullPointerException("Console Output Button not found!");
        }

        return consoleOutput.get();
    }

    public MutationTableView openPitMutationView () {
        return openPage(getPitMutationLink(), MutationTableView.class);
    }

    public ConsoleView openConsoleView() {
        return openPage(getConsoleOutputLink(), ConsoleView.class);
    }

    private <T extends PageObject> T openPage(final WebElement link, final Class<T> type) {
        String href = link.getAttribute("href");
        T result = newInstance(type, injector, url(href), "");
        link.click();

        return result;
    }
}
