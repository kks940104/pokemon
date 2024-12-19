package org.koreait.pokemon.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.koreait.pokemon.api.entities.*;
import org.koreait.pokemon.entities.Pokemon;
import org.koreait.pokemon.repositories.PokemonRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiUpdateService {
    private final RestTemplate tpl;
    private final PokemonRepository repository;
    private final ObjectMapper om;

    /**
     * 1페이지당 100개씩 DB 반영
     *
     * @param page
     */
    public void update(int page) {
        int limit = 50;
        int offset = (page - 1) * limit; // 시작 레코드 번호, 0, 100, ...
        String url = String.format("https://pokeapi.co/api/v2/pokemon?offset=%d&limit=%d",offset,limit);

        ApiResponse response = tpl.getForObject(URI.create(url), ApiResponse.class);
        List<UrlItem> items = response.getResults();
        if (items == null || items.isEmpty()) { // 조회된 결과가 없는 경우 처리 X
            return;
        }


        // region 상세 정보 처리

        List<Pokemon> pokemons = new ArrayList<>();

        for (UrlItem item : items) {
            Pokemon pokemon = new Pokemon();
            ApiPokemon data1 = tpl.getForObject(URI.create(item.getUrl()), ApiPokemon.class);
            pokemon.setSeq(Long.valueOf(data1.getId()));
            pokemon.setNameEn(data1.getName()); // 영문 이름
            pokemon.setHeight(data1.getHeight());
            pokemon.setWeight(data1.getWeight());
            pokemon.setBaseExperience(data1.getBaseExperience());
            pokemon.setFrontImage(data1.getSprites().getOther().getOfficialArtwork().get("front_default"));
            pokemon.setFrontDefault(data1.getSprites().getFrontDefault());

            // region 타입 처리

            String types = data1.getTypes().stream().map(d -> d.getType().getName())
                    .collect(Collectors.joining("||"));

            // endregion

            // region 능력 처리 되던거 만약 안된다면 이걸로 다시 되돌리자.

            // String abilities = data1.getAbilities().stream().map(d -> d.getAbility().getName()).collect(Collectors.joining());

/*            List<String> abilityUrl = data1.getAbilities().stream().map(d -> d.getAbility().getUrl()).toList(); // 특성 URL
            Map<String, String> ability = new HashMap<>();

            for (String url1 : abilityUrl) {
                ApiPokemon abilityData = tpl.getForObject(URI.create(url1), ApiPokemon.class);
                String abilityCheck1 = abilityData.getNames().stream().filter(d -> d.getLanguage().getName().equals("ko")).map(Names::getName).collect(Collectors.joining());
                String abilityFlavorText = abilityData.getFlavorTextEntries().stream().filter(d -> d.getLanguage().getName().equals("ko")).map(FlavorText::getFlavorText).collect(Collectors.joining("||"));
                ability.put(abilityCheck1, abilityFlavorText.split("\\|\\|")[0]);
            }

            try {
                String abilities = om.writeValueAsString(ability);
                pokemon.setAbilities(abilities);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }*/

            // endregion

            // region 능력 처리 Map안에 value값을 map으로 변환.

            // String abilities = data1.getAbilities().stream().map(d -> d.getAbility().getName()).collect(Collectors.joining());

            List<String> abilityUrl = data1.getAbilities().stream().map(d -> d.getAbility().getUrl()).toList(); // 특성 URL
            Map<String, Map<String, Boolean>> ability = new HashMap<>();
            List<Boolean> isHidden = new ArrayList<>(data1.getAbilities().stream().map(Ability::isHidden).toList());

            for (String url1 : abilityUrl) {
                Map<String, Boolean> hidden = new HashMap<>();
                ApiPokemon abilityData = tpl.getForObject(URI.create(url1), ApiPokemon.class);
                String abilityCheck1 = abilityData.getNames().stream().filter(d -> d.getLanguage().getName().equals("ko")).map(Names::getName).collect(Collectors.joining());
                String abilityFlavorText = abilityData.getFlavorTextEntries().stream().filter(d -> d.getLanguage().getName().equals("ko")).map(FlavorText::getFlavorText).collect(Collectors.joining("||"));
                String[] realAbility = abilityFlavorText.split("\\|\\|");
                hidden.put(realAbility[realAbility.length - 1], isHidden.remove(0));
                ability.put(abilityCheck1, hidden);
            }

            try {
                String abilities = om.writeValueAsString(ability);
                pokemon.setAbilities(abilities);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // endregion

            // region Status 처리

            String hp = data1.getStats().stream().filter(d -> d.getStat().getName().equals("hp")).map(Stats::getBase_stat).collect(Collectors.joining());
            String attack = data1.getStats().stream().filter(d -> d.getStat().getName().equals("attack")).map(Stats::getBase_stat).collect(Collectors.joining());
            String defense = data1.getStats().stream().filter(d -> d.getStat().getName().equals("defense")).map(Stats::getBase_stat).collect(Collectors.joining());
            String specialAttack = data1.getStats().stream().filter(d -> d.getStat().getName().equals("special-attack")).map(Stats::getBase_stat).collect(Collectors.joining());
            String specialDefense = data1.getStats().stream().filter(d -> d.getStat().getName().equals("special-defense")).map(Stats::getBase_stat).collect(Collectors.joining());
            String speed = data1.getStats().stream().filter(d -> d.getStat().getName().equals("speed")).map(Stats::getBase_stat).collect(Collectors.joining());

            pokemon.setHp(hp);
            pokemon.setAttack(attack);
            pokemon.setDefense(defense);
            pokemon.setSpecialAttack(specialAttack);
            pokemon.setSpecialDefense(specialDefense);
            pokemon.setSpeed(speed);

            // endregion

            pokemon.setTypes(types);
            // pokemon.setAbilities(abilities);

            // region 포켓몬 한글 이름, 포켓몬 한글 설명
            String url2 = String.format("https://pokeapi.co/api/v2/pokemon-species/%d", data1.getId());

            ApiPokemon data2 = tpl.getForObject(URI.create(url2), ApiPokemon.class);

            // region 한글 이름

            String namekr = data2.getNames().stream().filter(d -> d.getLanguage().getName().equals("ko")).map(d -> d.getName()).collect(Collectors.joining());
            pokemon.setName(namekr);

            // endregion

            // region 버전 추출 필 X

            // d는 지금 FlavorText가 들어가있음.
            // 내가 뽑아내려고 하는 Text는 version과 name이 ko가 들어가있는거.
            // 문제점. 싹 다 가져와버림.
            // String version = data2.getFlavorTextEntries().stream().filter(d -> d.getLanguage().getName().equals("ko")).map(d -> d.getVersion().getName()).collect(Collectors.joining());

            // endregion

            // region 한글 설명 필X
            // String flavorText = data2.getFlavorTextEntries().stream().filter(d -> d.getLanguage().getName().equals("ko")).map(FlavorText::getFlavorText).collect(Collectors.joining());
            // pokemon.setFlavorText(version + "_" + flavorText);
            // endregion

            // region 버전 및 설명 합치기

/*            String versionFlavorText = data2.getFlavorTextEntries().stream()
                    .filter(d -> d.getLanguage().getName().equals("ko"))
                    .map(d -> d.getVersion().getName() + "_" + d.getFlavorText())
                    .collect(Collectors.joining("||"));
            pokemon.setFlavorText(versionFlavorText);*/

            Map<String, String> flavorTexts = data2.getFlavorTextEntries().stream()
                    .filter(d -> d.getLanguage().getName().equals("ko"))
                    .collect(Collectors.toMap(d -> d.getVersion().getName(), d -> d.getFlavorText(), (p1, p2) -> p2));


            try {
                 String json = om.writeValueAsString(flavorTexts);
                 pokemon.setFlavorText(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // endregion

            // region 포켓몬 분류

            String genus = data2.getGenera().stream().filter(x -> x.getLanguage().getName().equals("ko")).map(Genus::getGenus).collect(Collectors.joining());

            pokemon.setGenus(genus);

            // endregion

            // endregion

            pokemons.add(pokemon);

        }
        // endregion

        repository.saveAllAndFlush(pokemons);
    }

    public void updateTest(int page) {
        int limit = 1;
        int offset = (page - 1) * limit; // 시작 레코드 번호, 0, 100, ...
        String url = String.format("https://pokeapi.co/api/v2/pokemon?offset=%d&limit=%d",offset,limit);

        ApiResponse response = tpl.getForObject(URI.create(url), ApiResponse.class);
        List<UrlItem> items = response.getResults();
        if (items == null || items.isEmpty()) { // 조회된 결과가 없는 경우 처리 X
            return;
        }

        // region 상세 정보 처리

        for (UrlItem item : items) {
            ApiPokemon data1 = tpl.getForObject(URI.create(item.getUrl()), ApiPokemon.class);

            List<String> abilityUrl = data1.getAbilities().stream().map(d -> d.getAbility().getUrl()).toList(); // 특성 URL
            // List<Boolean> isHidden = data1.getAbilities().stream().map(Ability::isHidden).toList(); // 히든.

            List<String> ability = new ArrayList<>();

            for (String url1 : abilityUrl) {
                ApiPokemon data2 = tpl.getForObject(URI.create(url1), ApiPokemon.class);
                String abilityCheck1 = data2.getNames().stream().filter(d -> d.getLanguage().getName().equals("ko")).map(d -> d.getName()).collect(Collectors.joining());
                System.out.println(abilityCheck1);
                ability.add(abilityCheck1);
            }
            String realAbility = ability.stream().collect(Collectors.joining("||"));

            System.out.println(realAbility);
        }

    }
}
