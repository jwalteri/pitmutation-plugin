package org.jenkins.plugins.pitmutation.Views;

import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkinsci.test.acceptance.po.Build;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class DashboardView extends AbstractView {

    public DashboardView(final Build parent, String id) {
        super(parent, id);
        this.open();
    }

    @Override
    public void initialize() {  /* Nothing to intialize */  }

    private WebElement getPitMutationLink() {
        driver.navigate().refresh();
        Optional<WebElement> consoleOutput = WebElementUtils.getByTagName(getBody(), WebElementUtils.LINK_TAG).stream()
            .filter(x -> x.getText().contains("PIT Mutation Report"))
            .findFirst();

        if (!consoleOutput.isPresent()) {
            throw new NullPointerException("PIT Mutation Report Button not found!");
        }

        return consoleOutput.get();
    }

    private WebElement getConsoleOutputLink() {
        driver.navigate().refresh();
        Optional<WebElement> consoleOutput = WebElementUtils.getByTagName(getBody(), WebElementUtils.LINK_TAG).stream()
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
}
