package org.koreait.games.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.games.services.ShadowGameService;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final Utils utils;
    private final ShadowGameService shadowGameService;

    @ModelAttribute("requestShadowGame")
    public RequestShadowGame requestShadowGame() {
        return new RequestShadowGame();
    }


    @GetMapping("/shadowGame")
    public String shadowGame(Model model) {
        commonProcess("game", model);
        List<Long> count = shadowGameService.pokemonCount();
        model.addAttribute("pokemonCount", count);
        return utils.tpl("game/shadowgame");
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "game";
        String pageTitle = null; // 페이지 제목
        List<String> addCommonScript = new ArrayList<>(); // 공통 자바스크립트
        List<String> addScript = new ArrayList<>(); // 프론트쪽에 추가하는 자바 스크립트
        if (mode.equals("game")) {
            pageTitle = "포켓몬 게임하기";
        }

        // 페이지 제목
        model.addAttribute("pageTitle", pageTitle);

        // 공통 스크립트
        model.addAttribute("addCommonScript", addCommonScript);

        // front 스크립트
        model.addAttribute("addScript", addScript);
    }
}
