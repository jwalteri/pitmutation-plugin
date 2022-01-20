package org.jenkins.plugins.pitmutation.tables;

/**
 * Represents an abstract table.
 */
public abstract class AbstractTable {
    protected AbstractTableSorting sorting;

    /**
     * Returns the sorting of the table.
     *
     * @return The sorting.
     */
    public AbstractTableSorting getSorting() {
        return sorting;
    }
}
