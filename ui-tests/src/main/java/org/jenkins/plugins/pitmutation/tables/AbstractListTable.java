package org.jenkins.plugins.pitmutation.tables;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractListTable<T> extends AbstractTable {
    protected List<T> dataEntries = new ArrayList<>();

    public List<T> getDataEntries() {
        return dataEntries;
    }
}
