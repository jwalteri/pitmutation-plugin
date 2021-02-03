package org.jenkinsci.plugins.pitmutation.targets;

import hudson.util.TextFile;
import lombok.Getter;

import org.jenkinsci.plugins.pitmutation.Mutation;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * @author Ed Kimber
 */
public class MutatedClass extends MutationResult<MutatedClass>
{

    @Getter
    private String name;
    @Getter
    private String package_;
    @Getter
    private String fileName;
    private Collection<Mutation> mutations;
    private Map<String, MutatedLine> mutatedLines;

    public MutatedClass(String name, MutationResult parent, Collection<Mutation> mutations)
    {
        super(name, parent);
        this.name = name;
        this.mutations = mutations;

        int lastDot = name.lastIndexOf('.');
        package_ = lastDot >= 0 ? name.substring(0, lastDot) : "";
        fileName = lastDot >= 0 ? name.substring(lastDot + 1) + ".java.html" : "";

        mutatedLines = createMutatedLines(mutations);
    }

    private Map<String, MutatedLine> createMutatedLines(Collection<Mutation> mutations)
    {
        return mutations.stream()
            .collect(groupingBy(Mutation::getLineNumber))
            .values()
            .stream()
            .map(m -> new MutatedLine(m.get(0).getLineNumber(), MutatedClass.this, m))
            .collect(toMap(MutatedLine::getName, o -> o));
    }

    @Override
    public boolean isSourceLevel()
    {
        return true;
    }

    @Override
    public String getSourceFileContent()
    {
        String sourceFilePath =
            getOwner().getRootDir() + File.separator + "mutation-report-" + getParent().getParent().getName() +
                File.separator + package_ + File.separator + fileName;
        try
        {
            String fullContents = new TextFile(new File(sourceFilePath)).read();
            return fullContents.contains("</h1>") ? fullContents.substring(fullContents.indexOf("</h1>") + 5) :
                fullContents;
        }
        catch (IOException exception)
        {
            return "Could not read source file: " + sourceFilePath + "\n";
        }
    }

    public String getDisplayName()
    {
        return "Class: " + name;
    }

    @Override
    public MutationStats getMutationStats()
    {
        return new MutationStatsImpl(getName(), mutations);
    }

    @Override
    public Map<String, ? extends MutationResult<?>> getChildMap()
    {
        return mutatedLines;
    }

    @Override
    public int compareTo(@Nonnull MutatedClass other)
    {
        return this.getMutationStats().getUndetected() - other.getMutationStats().getUndetected();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MutatedClass that = (MutatedClass) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(package_, that.package_) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(mutations, that.mutations) &&
            Objects.equals(mutatedLines, that.mutatedLines);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, package_, fileName, mutations, mutatedLines);
    }
}
