package org.jenkins.plugins.pitmutation;

import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class DashboardView extends PageObject {
    private WebElement dashboard;
    private String id;

    protected DashboardView(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
        //driver.navigate().refresh();
        this.dashboard = this.getElement(by.tagName("body"));
        this.id = id;
    }

    private WebElement getPitMutationLink() {
        Optional<WebElement> consoleOutput = this.dashboard.findElements(by.tagName("a")).stream()
            .filter(x -> x.getText().contains("PIT Mutation Report"))
            .findFirst();

        if (!consoleOutput.isPresent()) {
            throw new NullPointerException("PIT Mutation Report Button not found!");
        }

        return consoleOutput.get();
    }

    private WebElement getConsoleOutputLink() {
        Optional<WebElement> consoleOutput = this.dashboard.findElements(by.tagName("a")).stream()
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
        T result = newInstance(type, injector, url(href), id);
        link.click();

        return result;
    }
}
