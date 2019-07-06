package org.jenkinsci.plugins.pitmutation.targets;

import org.jenkinsci.plugins.pitmutation.Mutation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toSet;

/**
 * @author edward
 */
public class MutatedLine extends MutationResult<MutatedLine> {

    private int lineNumber;
    private Collection<Mutation> mutations;

    public MutatedLine(int line, MutationResult parent, Collection<Mutation> mutations) {
        super(String.valueOf(line), parent);
        this.mutations = mutations;
        this.lineNumber = line;
    }

    public Collection<String> getMutators() {
        return mutations.stream().filter(Objects::nonNull).map(Mutation::getMutatorClass).collect(toSet());
    }

    @Override
    public String getName() {
        return String.valueOf(lineNumber);
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public MutationStats getMutationStats() {
        return new MutationStatsImpl(getName(), mutations);
    }

    @Override
    public Map<String, MutationResult<?>> getChildMap() {
        return new HashMap<>();
    }

    @Override
    public String getUrl() {
        String source = getParent().getSourceFileContent();
        Pattern p = Pattern.compile("(#org.*_" + getName() + ")'");
        Matcher m = p.matcher(source);
        if (m.find()) {
            return m.group(1);
        }
        return super.getUrl();
    }

    @Override
    public int compareTo(@Nonnull MutatedLine other) {
        return other.lineNumber - lineNumber;
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

        if (!(obj instanceof MutatedLine)) {
            return false;
        }

        return Objects.equals(getMutationStats().getUndetected(), ((MutatedLine) obj).getMutationStats().getUndetected());
    }
}
