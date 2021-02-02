package org.jenkinsci.plugins.pitmutation;

import org.apache.commons.digester3.Digester;
import org.jenkinsci.plugins.pitmutation.targets.MutationStats;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author edward
 */
public class MutationReport {

    private Map<String, List<Mutation>> mutationsByPackage;
    private Map<String, List<Mutation>> mutationsByClass;
    private int killCount = 0;

    public MutationReport() {
        this.mutationsByClass = new HashMap<>();
        this.mutationsByPackage = new HashMap<>();
    }

    public static MutationReport create(InputStream xmlReport) throws IOException, SAXException {
        return digestMutations(xmlReport);
    }

    private static MutationReport digestMutations(InputStream input) throws IOException, SAXException {
        Digester digester = new Digester();
        digester.addObjectCreate("mutations", MutationReport.class);
        digester.addObjectCreate("mutations/mutation", Mutation.class);
        digester.addSetNext("mutations/mutation", "addMutation", "org.jenkinsci.plugins.pitmutation.Mutation");
        digester.addSetProperties("mutations/mutation");
        digester.addSetNestedProperties("mutations/mutation");

        MutationReport report = digester.parse(input);
        report.mutationsByClass.forEach((className, mutations) ->
            report.mutationsByPackage.computeIfAbsent(packageNameFromClass(className), k -> new ArrayList<>()).addAll(mutations));
        return report;
    }

    /**
     * Called by digester.
     *
     * @param mutation {@link Mutation} to add
     */
    public void addMutation(Mutation mutation) {
        mutationsByClass.computeIfAbsent(mutation.getMutatedClass(), k -> new ArrayList<>())
            .add(mutation);
        if (mutation.isDetected()) {
            killCount++;
        }
        mutationsByPackage.computeIfAbsent(packageNameFromClass(mutation.getMutatedClass()), k -> new ArrayList<>())
            .add(mutation);
    }

    public Collection<Mutation> getMutationsForPackage(String packageName) {
        return mutationsByPackage.computeIfAbsent(packageName, k -> new ArrayList<>());
    }

    public Map<String, List<Mutation>> getMutationsByPackage() {
        return mutationsByPackage;
    }

    public Collection<Mutation> getMutationsForClassName(String className) {
        return mutationsByClass.computeIfAbsent(className, k -> new ArrayList<>());
    }

    public MutationStats getMutationStats() {
        return new MutationStats() {
            @Override
            public String getTitle() {
                return "Report Stats";
            }

            @Override
            public int getUndetected() {
                return getTotalMutations() - killCount;
            }

            @Override
            public int getTotalMutations() {
                return mutationsByClass.values().stream().mapToInt(Collection::size).sum();
            }
        };
    }

    // TODO: Move somewhere out of this class
    static String packageNameFromClass(String fqcn) {
        int idx = fqcn.lastIndexOf('.');
        return fqcn.substring(0, idx != -1 ? idx : 0);
    }
}
