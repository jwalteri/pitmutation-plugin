package org.jenkinsci.plugins.pitmutation.targets;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jenkinsci.plugins.pitmutation.Mutation;

import java.util.Collection;

/**
 * @author Ed Kimber
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MutationStatsImpl extends MutationStats {

    private String title;
    private int undetected = 0;
    private int totalMutations;

    public MutationStatsImpl(String title, Collection<Mutation> mutations) {
        this.title = title;
        if (mutations == null) {
            return;
        }
        for (Mutation m : mutations) {
            if (!m.isDetected()) {
                undetected++;
            }
        }

        totalMutations = mutations.size();
    }
}

