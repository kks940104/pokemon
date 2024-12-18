package org.koreait.pokemon.libs;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("pUtils")
public class PokemonUtils {

    private static Map<String, String> colors = new HashMap<>();
    static {
        colors.put("type1", "red");
    }
    public String getColorByType(String type) {
        return colors.get(type);
    }
}
