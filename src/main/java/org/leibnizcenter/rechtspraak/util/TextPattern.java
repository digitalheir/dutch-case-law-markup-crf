package org.leibnizcenter.rechtspraak.util;

import org.leibnizcenter.rechtspraak.markup.Label;
import org.leibnizcenter.rechtspraak.markup.features.TokenMatch;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by maarten on 11-3-16.
 */
public class TextPattern {
    public final Map<Label, TokenMatch.Filter> filters = new EnumMap<>(Label.class);
    public final Map<Label, TokenMatch.Feature> features = new EnumMap<>(Label.class);
    private final String name;
    private final Pattern pattern;

    public TextPattern(String name, Pattern regex) {
        this.name = name;
        this.pattern = regex;
        for (Label l : Label.values()) {
            filters.put(l, new TokenMatch.Filter(l, pattern));
            features.put(l, new TokenMatch.Feature(l, pattern));
        }
    }

    public TextPattern(Pattern compile) {
        this(compile.pattern(), compile);
    }

    public boolean matches(String text) {
        return pattern.matcher(text).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextPattern that = (TextPattern) o;

        return name.equals(that.name) && pattern.equals(that.pattern) && filters.equals(that.filters) && features.equals(that.features);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + pattern.hashCode();
        result = 31 * result + filters.hashCode();
        result = 31 * result + features.hashCode();
        return result;
    }
}