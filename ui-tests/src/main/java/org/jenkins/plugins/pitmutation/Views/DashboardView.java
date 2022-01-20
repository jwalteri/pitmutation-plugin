package org.jenkins.plugins.pitmutation.Views;

import org.jenkins.plugins.pitmutation.WebElementUtils;
import org.jenkinsci.test.acceptance.po.Build;
import org.openqa.selenium.WebElement;

import java.util.Optional;

/**
 * Represents the landing page of the build.
 */
public class DashboardView extends AbstractView {

    /**
     * Ctor for DashboardView.
     *
     * @param parent The build.
     * @param id The id.
     */
    public DashboardView(final Build parent, String id) {
        super(parent, id);
        this.open();
    }

    @Override
    public void initialize() {  /* Nothing to initialize here */  }

    /**
     * Returns the WebElement containing the link to the PitMutation Plugin site.
     *
     * @return The WebElement containing the link to the PitMutation Plugin site.
     */
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

    /**
     * Returns the WebElement containing the link to the console output site.
     *
     * @return The WebElement containing the link to the console output site.
     */
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

    /**
     * Opens the PitMutation Plugin site.
     *
     * @return The PitMutation Plugin site.
     */
    public MutationTableView openPitMutationView () {
        return openPage(getPitMutationLink(), MutationTableView.class);
    }

    /**
     * Opens the console output site.
     *
     * @return The console output site.
     */
    public ConsoleView openConsoleView() {
        return openPage(getConsoleOutputLink(), ConsoleView.class);
    }
}
