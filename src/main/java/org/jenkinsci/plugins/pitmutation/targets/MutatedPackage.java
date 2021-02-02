package org.jenkinsci.plugins.pitmutation.targets;

import lombok.extern.slf4j.Slf4j;
import org.jenkinsci.plugins.pitmutation.Mutation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * @author Ed Kimber
 */
@Slf4j
public class MutatedPackage extends MutationResult<MutatedPackage> {

    private Map<String, List<Mutation>> classMutations;

    public MutatedPackage(String name, MutationResult parent, Map<String, List<Mutation>> classMutations) {
        super(name, parent);
        this.classMutations = classMutations;
    }

    @Override
    public String getDisplayName() {
        return "Package: " + getName();
    }

    @Override
    public MutationStats getMutationStats() {
        return new MutationStatsImpl(getName(), classMutations.values().stream().flatMap(List::stream).collect(toList()));
    }

    @Override
    public Map<String, MutatedClass> getChildMap() {
        Map<String, MutatedClass> childMap = new HashMap<>();
        for (Map.Entry<String, List<Mutation>> classMutation : classMutations.entrySet()) {
            String className = classMutation.getKey();
            List<Mutation> mutations = classMutation.getValue();
            log.debug("found " + mutations.size() + " reports for " + className);
            childMap.put(className, new MutatedClass(className, this, mutations));
        }
        return childMap;
    }

    @Override
    public int compareTo(@Nonnull MutatedPackage other) {
        return this.getMutationStats().getUndetected() - other.getMutationStats().getUndetected();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getMutationStats(), this.getChildMap(), this.getDisplayName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MutatedPackage)) {
            return false;
        }

        return Objects.equals(getMutationStats().getUndetected(), ((MutatedPackage) obj).getMutationStats().getUndetected());
    }
}
