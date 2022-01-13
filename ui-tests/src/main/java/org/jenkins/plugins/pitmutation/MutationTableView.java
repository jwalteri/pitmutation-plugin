package org.jenkins.plugins.pitmutation;

import com.google.inject.Injector;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.PageObject;

import java.net.URL;

public class MutationTableView extends PageObject {
    private String id;

    protected MutationTableView(final Build parent, String id) {
        super(parent, parent.url(id));
        this.open();
        this.id = id;
    }

    public MutationTableView(final Injector injector, final URL url, final String id) {
        super(injector, url);
    }

    public static class MutationTableEntry {
        
    }
}
