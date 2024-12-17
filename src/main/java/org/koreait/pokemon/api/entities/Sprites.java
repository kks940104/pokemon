package org.koreait.pokemon.api.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class Sprites {
    @JsonAlias("front_default")
    private String frontDefault;
    private SpritesOther other;
}
