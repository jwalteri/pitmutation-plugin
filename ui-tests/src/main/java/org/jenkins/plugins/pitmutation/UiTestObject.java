package org.jenkins.plugins.pitmutation;

import com.google.inject.Injector;
import org.apache.commons.text.translate.NumericEntityUnescaper;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UiTestObject extends PageObject {
    private WebElement page;

    protected UiTestObject(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
        page = this.getElement(by.tagName("body"));

        System.out.println(page.getText());
    }

    public WebElement getAll() {
        return page;
    }

    /*
<svg viewBox="0 0 24 24" tooltip="Success" focusable="false" class="svg-icon icon-blue icon-xlg">
    <use href="/static/7397001f/images/build-status/build-status-sprite.svg#last-successful"></use>
</svg>
 */

    public WebElement getBuildStatusSVG() {
        Optional<WebElement> svg = page.findElements(by.tagName("svg")).stream()
            .filter(x -> x.getText().contains("build-status-sprite"))
            .collect(Collectors.toList()).stream().findFirst();

        return svg.orElse(null);
    }
}
