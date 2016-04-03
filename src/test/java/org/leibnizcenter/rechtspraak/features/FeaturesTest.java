package org.leibnizcenter.rechtspraak.features;

import org.junit.Test;
import org.leibnizcenter.rechtspraak.features.elementpatterns.ElementFeature;
import org.leibnizcenter.rechtspraak.features.elementpatterns.NumberingFeature;
import org.leibnizcenter.rechtspraak.features.textpatterns.GeneralTextPattern;
import org.leibnizcenter.rechtspraak.features.textpatterns.KnownSurnamesNl;
import org.leibnizcenter.rechtspraak.features.textpatterns.TitlePatterns;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by maarten on 1-4-16.
 */
public class FeaturesTest {
    @Test
    public void allFeaturesHaveDifferentNames() {
        Set<String> names = new HashSet<>();

        addNames(names, ElementFeature.values());
        addNames(names, NumberingFeature.values());
        addNames(names, GeneralTextPattern.values());
        addNames(names, KnownSurnamesNl.values());
        addNames(names, TitlePatterns.General.values());
        addNames(names, TitlePatterns.TitlesNormalizedMatchesLowConf.values());
        addNames(names, TitlePatterns.TitlesNormalizedMatchesHighConf.values());
    }

    private void addNames(Set<String> names, Enum[] values) {
        for (Enum p : values) {
            String name = p.name();
            assertFalse("Clashing name " + name, names.contains(name));
            names.add(name);
        }
    }
}