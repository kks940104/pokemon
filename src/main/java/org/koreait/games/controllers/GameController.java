package org.koreait.games.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koreait.games.constants.Level;
import org.koreait.games.entitis.GameRank;
import org.koreait.games.services.*;
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

@Slf4j
@Controller
@ApplyErrorPage
@RequiredArgsConstructor
@RequestMapping("/game")
@SessionAttributes("requestShadowGame")
public class GameController {

    private final Utils utils;
    private final HttpServletRequest request;
    private final ShadowGameValidators Validators;
    private final ShadowGameInfoService InfoService;
    private final ShadowGameSettingService SettingService;
    private final ShadowGameValidateService ValidateService;
    private final ShadowGameRankUpdateService rankUpdateService;
    private final ShadowGameRankInfoService rankInfoService;

/*    @ModelAttribute("addCss")
    public List<String> addCss() {
        return List.of("game/shadowgame");
    }*/

    @ModelAttribute("requestShadowGame")
    public RequestShadowGame requestShadowGame() {
        return new RequestShadowGame();
    }

    @ModelAttribute("rankSearch")
    public RankSearch rankSearch() {
        return new RankSearch();
    }


    @GetMapping("/shadowGame")
    public String shadowGame(Model model) {
        commonProcess("game", model);
        model.addAttribute("requestShadowGame", requestShadowGame());
        return utils.tpl("game/shadowgame");
    }

    @RequestMapping(path="/shadowstart", method={RequestMethod.POST, RequestMethod.GET})
    public String shadowStart(@ModelAttribute RequestShadowGame form, Model model, SessionStatus status, Errors errors, HttpSession session) {
        commonProcess("gamestart", model);
        String message = request.getMethod().toLowerCase();
        Validators.validate(form, errors);
        if (errors.hasErrors()) {
            rankUpdateService.process(form);
            status.setComplete();
            session.removeAttribute("requestShadowGame");
            return "redirect:/";
        }
        if (message.equals("post")) {
            form.setPokemonCount(SettingService.pokemonGameSetting(form)); // 상중하 골라서 count check
        }
        Pokemon pokemon = InfoService.findPokemon(form); // 포켓몬 뽑기
        pokemon.setGameFlavorText(InfoService.getFlavors(pokemon)); // 설명넣기
        form.setGameBtnClick(false);
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
    public String shadowStart_ps(@ModelAttribute RequestShadowGame form, Model model) {
        commonProcess("gamestart", model);
        ValidateService.validatePokemon(form);
        Pokemon pokemon = form.getGamePokemon();
        pokemon.setGameFlavorText(InfoService.getFlavors(pokemon)); // 설명넣기
        model.addAttribute("pokemon", pokemon);
        model.addAttribute("mode", "ps");
        return utils.tpl("game/shadowstart_ps");
    }

    @GetMapping("/shadowrank")
    public String rankShow(/*@ModelAttribute RankSearch search, */Model model) {
        commonProcess("rank", model);
        List<GameRank> ranks = rankInfoService.getRankList(Level.ROW);
        System.out.println("ranks : " + ranks);
        model.addAttribute("ranks", ranks);
        return utils.tpl("game/ranklist");
    }

    @PostMapping("/shadowrank")
    public String rankShow2(/*@ModelAttribute RankSearch search, */Model model) {
/*        commonProcess("rank", model);
        GameRank ranks = rankInfoService.getRank(search.getLevel(), search);
        System.out.println("ranks : " + ranks);
        model.addAttribute("ranks", ranks);
        return utils.tpl("game/ranklist");*/


        commonProcess("rank", model);
        List<GameRank> ranks = rankInfoService.getRankList(Level.ROW);
        System.out.println("ranks : " + ranks);
        model.addAttribute("ranks", ranks);
        return utils.tpl("game/ranklist");
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
        } else if (mode.equals("rank")) {
            addCss.add("game/ranklist");
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
