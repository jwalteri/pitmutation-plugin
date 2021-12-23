package org.jenkinsci.plugins.pitmutation.targets;

import static org.jenkinsci.plugins.pitmutation.PitPublisher.MULTI_MODULE_REPORT_FORMAT;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

import org.jenkinsci.plugins.pitmutation.MutationReport;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
        return new ModuleChildMapBuilder(report, this).build();
    }

    @Override
    protected String getMutationReportDirectory() {
        return String.format(MULTI_MODULE_REPORT_FORMAT, getName());
    }

    @Override
    public int compareTo(@Nonnull ModuleResult other) {
        return this.getMutationStats().getUndetected() - other.getMutationStats().getUndetected();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ModuleResult that = (ModuleResult) o;
        return Objects.equals(report, that.report) &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(report, name);
    }
}
