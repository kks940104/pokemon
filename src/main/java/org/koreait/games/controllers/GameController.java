package org.koreait.games.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.games.services.ShadowGameService;
import org.koreait.games.validators.ShadowGameValidators;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.koreait.pokemon.entities.Pokemon;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@Controller
@ApplyErrorPage
@RequiredArgsConstructor
@RequestMapping("/game")
@SessionAttributes("requestShadowGame")
public class GameController {

    private final Utils utils;
    private final HttpServletRequest request;
    private final ShadowGameService shadowGameService;
    private final ShadowGameValidators shadowGameValidators;

/*    @ModelAttribute("addCss")
    public List<String> addCss() {
        return List.of("game/shadowgame");
    }*/

    @ModelAttribute("requestShadowGame")
    public RequestShadowGame requestShadowGame() {
        return new RequestShadowGame();
    }


    @GetMapping("/shadowGame")
    public String shadowGame(Model model) {
        commonProcess("game", model);
        return utils.tpl("game/shadowgame");
    }

    @RequestMapping(path="/shadowstart", method={RequestMethod.POST, RequestMethod.GET})
    public String shadowStart(@ModelAttribute RequestShadowGame form, Model model) {
        commonProcess("gamestart", model);
        String message = request.getMethod().toLowerCase();
        if(message.equals("post")) {
            form.setPokemonCount(shadowGameService.pokemonGameSetting(form)); // 상중하 골라서 count check
        }
        Pokemon pokemon = shadowGameService.findPokemon(form); // 포켓몬 뽑기
        pokemon.setGameFlavorText(shadowGameService.getFlavors(pokemon)); // 설명넣기
        model.addAttribute("pokemon", pokemon);
        return utils.tpl("game/shadowstart");
    }
/*
    @GetMapping("/shadowstart") // 포켓몬 맞추고 난 후
    public String shadowStart(Model model) {
        commonProcess("gamestart", model);
        return utils.tpl("game/shadowstart");
    }*/

    @PostMapping("/shadowstart_ps")
    public String shadowStart_ps(@ModelAttribute RequestShadowGame form, Model model, SessionStatus status, Errors errors) {
        commonProcess("gamestart", model);
        shadowGameValidators.validate(form, errors);

        shadowGameService.validatePokemon(form);
        if (errors.hasErrors()) {
            status.setComplete();
            return utils.tpl("game/shadowstart_ps");
        }
        return utils.tpl("game/shadowstart_ps");
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "game";
        String pageTitle = null; // 페이지 제목
        List<String> addCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>(); // 공통 자바스크립트
        List<String> addScript = new ArrayList<>(); // 프론트쪽에 추가하는 자바 스크립트
        if (mode.equals("game")) {
            addCss.add("game/shadowgame");
            pageTitle = "포켓몬 게임하기";
            addScript.add("game/shadowgame");
        } else if (mode.equals("gamestart")) {
            pageTitle = "포켓몬 게임시작";
            addCss.add("game/shadowstart");
            addScript.add("game/shadowstart");
        }

        // 페이지 제목
        model.addAttribute("pageTitle", pageTitle);

        // 공통 스크립트
        model.addAttribute("addCommonScript", addCommonScript);

        // front 스크립트
        model.addAttribute("addScript", addScript);

        // front Css
        model.addAttribute("addCss", addCss);
    }
}
