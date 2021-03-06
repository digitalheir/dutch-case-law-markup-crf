package org.leibnizcenter.rechtspraak.cfg.rule.type;

import org.leibnizcenter.rechtspraak.cfg.rule.RightHandSide;

/**
 * Created by maarten on 18-4-16.
 */
public interface Type extends Comparable<Type> {
    boolean isTerminal();

    static Type parse(String typeString) {
        switch (typeString) {
            case "ε":
            case "<epsilon>":
            case "epsilon":
            case "ϵ":
            case "϶":
            case "Ⲉ":
            case "ⲉ":
            case "ɛ":
            case "ᶓ":
            case "ᵋ":
            case "ɜ":
            case "ɝ":
            case "ᶔ":
            case "ᶟ":
            case "ᴈ":
            case "ᵌ":
            case "ʚ":
            case "ɞ":
            case "∈":
            case "Ɛ":
                return null;
            default:
                if (Character.isUpperCase(typeString.charAt(0))) {
                    return new NonTerminal(typeString);
                } else {
                    return new Terminal(typeString);
                }
        }
    }
}
