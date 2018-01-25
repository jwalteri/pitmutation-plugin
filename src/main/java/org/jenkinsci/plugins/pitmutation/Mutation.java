package org.jenkinsci.plugins.pitmutation;

import lombok.Data;

/**
 * @author edward
 */
@Data
public class Mutation {
    private boolean detected;
    private String status;
    private String sourceFile;
    private String mutatedClass;
    private String mutatedMethod;
    private int lineNumber;
    private String mutator;
    private int index;
    private String killingTest;
    private String methodDescription;
    private String description;
    private String block;

    public String getMutatorClass() {
        int lastDot = mutator.lastIndexOf('.');
        String className = mutator.substring(lastDot + 1);
        return className.endsWith("Mutator")
            ? className.substring(0, className.length() - 7)
            : className;
    }
}
