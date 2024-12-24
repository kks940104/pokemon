package org.koreait.admin.basic.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.admin.global.menu.MenuDetail;
import org.koreait.admin.global.menu.Menus;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.entities.SiteConfig;
import org.koreait.global.libs.Utils;
import org.koreait.global.services.CodeValueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@ApplyErrorPage
@RequiredArgsConstructor
@RequestMapping("/admin/basic")
public class BasicController {

    private final CodeValueService codeValueService;
    private final Utils utils;

    @ModelAttribute("menuCode")
    public String menuCode() {
        return "basic";
    }

    @ModelAttribute("submenus")
    public List<MenuDetail> submenus() {
        return Menus.getMenus(menuCode());
    }

    @GetMapping({"", "/siteConfig"})
    public String siteConfig (Model model) {
        commonProcess("siteConfig", model);
        SiteConfig form = Objects.requireNonNullElseGet(codeValueService.get("siteConfig", SiteConfig.class), SiteConfig::new);
        System.out.println(form);
        model.addAttribute("siteConfig", form);

        return "admin/basic/siteConfig";
    }

    @PatchMapping("/siteConfig")
    public String siteConfigPs(SiteConfig form, Model model) {
        commonProcess("siteConfig", model);
        codeValueService.save("siteConfig", form);

        utils.showSessionMessage("저장되었습니다.");

        return "admin/basic/siteConfig"; // 임시
    }

    /**
     * 기본 설정 공통 처리 부분
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        model.addAttribute("subMenuCode", mode);
    }
}












