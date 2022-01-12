package org.jenkins.plugins.pitmutation;

import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DashboardView extends PageObject {
    private WebElement dashboard;

    protected DashboardView(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
        this.dashboard = this.getElement(by.tagName("body"));
    }

    public ConsoleView getConsoleView() {
        Optional<WebElement> consoleOutput = this.dashboard.findElements(by.tagName("a")).stream()
            .filter(x -> x.getText().contains("PIT Mutation Report"))
            .findFirst();

        if (!consoleOutput.isPresent()) {
            throw new NullPointerException("Console Output Button not found!");
        }

        WebElement consoleOutputButton = consoleOutput.get();

        String link = consoleOutputButton.getAttribute("href");
        ConsoleView consoleView = newInstance(ConsoleView.class, injector, url(link), "test");
        consoleOutputButton.click();
        consoleView.initConsoleView();

        return consoleView;
    }
}
