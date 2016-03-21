package org.leibnizcenter.rechtspraak.markup.docs.features;

import com.google.common.collect.Maps;
import org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.util.Doubles;
import org.leibnizcenter.rechtspraak.util.Labels;

import java.util.Map;

/**
 * Created by maarten on 2-3-16.
 */
public class HasNumbering {
    public static final Map<Label, Filter> filters;
    public static final Map<Label, Feature> features;

    static {
        Map<Label, Filter> filterMap = Maps.newEnumMap(Label.class);
        Map<Label, Feature> featureMap = Maps.newEnumMap(Label.class);
        for (Label l : Labels.withoutNull) {
            filterMap.put(l, new Filter(l));
            featureMap.put(l, new Feature(l));
        }
        filters = Maps.immutableEnumMap(filterMap);
        features = Maps.immutableEnumMap(featureMap);
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        private final Label label;

        public Feature(Label l) {
            this.label = l;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            return Doubles.asDouble(sequence[indexInSequence].numbering != null && (this.label.equals(currentTag)));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Filter filter = (Filter) o;

            return label == filter.label;

        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }

    public static class Filter extends org.crf.crf.filters.Filter<RechtspraakElement, Label> {
        private final Label label;

        public Filter(Label l) {
            this.label = l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Filter filter = (Filter) o;

            return label == filter.label;

        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }
}