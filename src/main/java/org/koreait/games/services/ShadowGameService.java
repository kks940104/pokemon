package org.koreait.games.services;

import lombok.RequiredArgsConstructor;
import org.koreait.games.controllers.RequestShadowGame;
import org.koreait.pokemon.entities.Pokemon;
import org.koreait.pokemon.repositories.PokemonRepository;
import org.koreait.pokemon.services.PokemonInfoService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class ShadowGameService {

    private final PokemonInfoService pokemonInfoService;
    private final PokemonRepository pokemonRepository;

    public List<Long> pokemonCount(RequestShadowGame form) {



        List<Long> pokemonCounts = new ArrayList<>();
        for (int i = 0; i < form.getPokemonCheck().length; i++) {
            if (form.getPokemonCheck()[i]) {
                if (i == 0) {
                    Collections.addAll(pokemonCounts,LongStream.range(1L, 151L).boxed().toArray(Long[]::new));
                } else if (i == 1) {
                    Collections.addAll(pokemonCounts,LongStream.range(152L, 252L).boxed().toArray(Long[]::new));
                } else if (i == 2) {
                    Collections.addAll(pokemonCounts,LongStream.range(252L, 387L).boxed().toArray(Long[]::new));
                } else if (i == 3) {
                    Collections.addAll(pokemonCounts,LongStream.range(387L, 494L).boxed().toArray(Long[]::new));
                } else if (i == 4) {
                    Collections.addAll(pokemonCounts,LongStream.range(494L, 650L).boxed().toArray(Long[]::new));
                } else if (i == 5) {
                    Collections.addAll(pokemonCounts,LongStream.range(650L, 722L).boxed().toArray(Long[]::new));
                } else if (i == 6) {
                    Collections.addAll(pokemonCounts,LongStream.range(722L, 810L).boxed().toArray(Long[]::new));
                } else if (i == 7) {
                    Collections.addAll(pokemonCounts,LongStream.range(810L, 899L).boxed().toArray(Long[]::new));
                }
            }
        }

        return pokemonCounts;
    }
}
