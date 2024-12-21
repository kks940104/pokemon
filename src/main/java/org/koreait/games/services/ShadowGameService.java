package org.koreait.games.services;

import lombok.RequiredArgsConstructor;
import org.koreait.pokemon.repositories.PokemonRepository;
import org.koreait.pokemon.services.PokemonInfoService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class ShadowGameService {

    private final PokemonInfoService pokemonInfoService;
    private final PokemonRepository pokemonRepository;

    public List<Long> pokemonCount() {
        return LongStream.range(1L, pokemonRepository.count() + 1L).boxed().toList();
    }
}
