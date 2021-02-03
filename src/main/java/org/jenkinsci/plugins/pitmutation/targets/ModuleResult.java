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
                report.getMutationsForPackage(packageName).stream().collect(groupingBy(this::getClassName));

            childMap.put(packageName, new MutatedPackage(packageName, this, classMutations));
        }
        return childMap;
    }

    @Override
    public int compareTo(@Nonnull ModuleResult other) {
        return this.getMutationStats().getUndetected() - other.getMutationStats().getUndetected();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ModuleResult that = (ModuleResult) o;
        return Objects.equals(report, that.report) &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(report, name);
    }

    private String getClassName(Mutation mutation)
    {
        String mutatedClassName = mutation.getMutatedClass();
        int firstDollar = mutatedClassName.indexOf('$');
        return firstDollar >= 0
            ? mutatedClassName.substring(0, firstDollar)
            : mutatedClassName;
    }
}
