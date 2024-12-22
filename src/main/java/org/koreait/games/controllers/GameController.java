package org.koreait.games.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.games.services.ShadowGameService;
import org.koreait.games.validators.ShadowGameValidators;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@ApplyErrorPage
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final Utils utils;
    private final ShadowGameService shadowGameService;
    private final ShadowGameValidators shadowGameValidators;

    @ModelAttribute("addCss")
    public List<String> addCss() {
        return List.of("game/shadowgame");
    }

    @ModelAttribute("requestShadowGame")
    public RequestShadowGame requestShadowGame() {
        return new RequestShadowGame();
    }


    @GetMapping("/shadowGame")
    public String shadowGame(Model model) {
        commonProcess("game", model);
        return utils.tpl("game/shadowgame");
    }

    @PostMapping("/shadowstart")
    public String shadowStart(@ModelAttribute RequestShadowGame form, Model model, Errors errors) {
        commonProcess("game", model);
        shadowGameValidators.validate(form, errors);

        List<Long> items = shadowGameService.pokemonCount(form);

        return utils.tpl("game/shadowstart");
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "game";
        String pageTitle = null; // 페이지 제목
        List<String> addCommonScript = new ArrayList<>(); // 공통 자바스크립트
        List<String> addScript = new ArrayList<>(); // 프론트쪽에 추가하는 자바 스크립트
        if (mode.equals("game")) {
            pageTitle = "포켓몬 게임하기";
            addScript.add("game/shadowgame");
        }

        // 페이지 제목
        model.addAttribute("pageTitle", pageTitle);

        // 공통 스크립트
        model.addAttribute("addCommonScript", addCommonScript);

        // front 스크립트
        model.addAttribute("addScript", addScript);
    }
}
