package org.jenkinsci.plugins.pitmutation.targets;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jenkinsci.plugins.pitmutation.Mutation;
import org.jenkinsci.plugins.pitmutation.MutationReport;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author edward
 */
@Slf4j
public class ModuleResult extends MutationResult<ModuleResult> {

    private MutationReport report;
    @Getter
    private String name;

    public ModuleResult(String name, MutationResult parent, MutationReport report) {
        super(name, parent);
        this.name = name;
        this.report = report;
    }

    @Override
    public String getDisplayName() {
        return "Module: " + getName();
    }

    @Override
    public MutationStats getMutationStats() {
        return report.getMutationStats();
    }

    @Override
    public Map<String, MutatedPackage> getChildMap() {
        Map<String, MutatedPackage> childMap = new HashMap<>();
        for (String packageName : report.getMutationsByPackage().keySet()) {
            Map<String, List<Mutation>> classMutations =
                report.getMutationsForPackage(packageName).stream().collect(groupingBy(Mutation::getMutatedClass));

            childMap.put(packageName, new MutatedPackage(packageName, this, classMutations));
        }
        return childMap;
    }

    @Override
    public int compareTo(@Nonnull ModuleResult other) {
        return this.getMutationStats().getUndetected() - other.getMutationStats().getUndetected();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ModuleResult)) {
            return false;
        }

        return Objects.equals(getMutationStats().getUndetected(), ((ModuleResult) obj).getMutationStats().getUndetected());
    }
}
