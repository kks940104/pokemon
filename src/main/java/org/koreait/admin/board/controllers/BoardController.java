package org.koreait.admin.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.admin.board.validators.BoardValidators;
import org.koreait.admin.global.menu.SubMenus;
import org.koreait.board.entities.Board;
import org.koreait.board.services.configs.BoardConfigInfoService;
import org.koreait.board.services.configs.BoardConfigUpdateService;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.koreait.global.paging.ListData;
import org.koreait.member.constants.Authority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@ApplyErrorPage
@RequiredArgsConstructor
@RequestMapping("/admin/board")
@Controller("adminBoardController")
public class BoardController implements SubMenus {

    private final Utils utils;
    private final BoardValidators validators;
    private final HttpServletRequest request;
    private final BoardConfigInfoService infoService;
    private final BoardConfigUpdateService configUpdateService;

    @Override
    @ModelAttribute("menuCode")
    public String menuCode() {
        return "board";
    }

    /**
     * 게시판 목록
     * @param model
     * @return
     */
    @GetMapping({"/list",""})
    public String list(@ModelAttribute BoardConfigSearch search, Model model) {
        commonProcess("list", model);
        ListData<Board> data = infoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());
        return "admin/board/list";
    }

    @RequestMapping(path="/list",
            method = {RequestMethod.PATCH, RequestMethod.DELETE})
    public String list_ps(@RequestParam(name="chk", required = false) List<Integer> chks, Model model) {
        configUpdateService.process(chks, request.getMethod().equalsIgnoreCase("DELETE") ? "delete" : "edit");
        model.addAttribute("script", "parent.location.reload()");
        return "common/_execute_script";
    }

    /**
     * 게시판 설정 등록
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(@ModelAttribute RequestBoard form, Model model) {
        commonProcess("add", model);
        form.setSkin("default");
        form.setListAuthority(Authority.ALL);
        form.setViewAuthority(Authority.ALL);
        form.setWriteAuthority(Authority.ALL);
        form.setCommentAuthority(Authority.ALL);
        form.setLocationAfterWriting("list");
        return "admin/board/add";
    }

    /**
     * 게시판 설정 수정
     * @param bid
     * @param model
     * @return
     */
    @GetMapping("/edit/{bid}")
    public String edit(@PathVariable("bid") String bid, Model model) {
        commonProcess("edit", model);
        RequestBoard form = infoService.getForm(bid);
        System.out.println(form);
        model.addAttribute("requestBoard", form);
        return "admin/board/edit";
    }

    /**
     * 게시판 등록, 수정 처리
     *
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestBoard form, Errors errors, Model model) {
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "add";
        commonProcess(mode, model);
        validators.validate(form, errors);
        if (errors.hasErrors()) {
            return "admin/board/" + mode;
        }
        configUpdateService.process(form);
        return "redirect:/admin/board/list";
    }

    /**
     * 게시글 ㄱ관리
     * @param model
     * @return
     */
    @GetMapping("/posts")
    public String posts(Model model) {
        commonProcess("posts", model);

        return "admin/board/posts";
    }


    /**
     * 공통 처리 부분
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "list";

        List<String> addCommonScript = new ArrayList<>();

        String pageTitle = "";
        if (mode.equals("list")) {
            pageTitle = "게시판 목록";
        } else if (mode.equals("add") || mode.equals("edit")) {
            pageTitle = mode.equals("edit") ? "게시판 수정" : "게시판 등록";
            addCommonScript.add("fileManager");
        } else if (mode.equals("posts")) {
            pageTitle = "게시글 관리";
        }
        pageTitle += " - 게시판 관리";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("subMenuCode", mode);
    }

}



















