package org.koreait.admin.product.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.admin.global.menu.SubMenus;
import org.koreait.file.constants.FileStatus;
import org.koreait.file.services.FileInfoService;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@ApplyErrorPage
@RequiredArgsConstructor
@RequestMapping("/admin/product")
@Controller("adminProductController")
public class ProductController implements SubMenus {

    private final Utils utils;
    private final FileInfoService fileInfoService;

    @Override
    @ModelAttribute("menuCode")
    public String menuCode() {
        return "product";
    }

    /**
     * 상품 목록
     * @param model
     * @return
     */
    @GetMapping({"", "/list"})
    public String list(Model model) {
        commonProcess("list",model);
        return "admin/product/list";
    }

    /**
     * 상품 등록
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(@ModelAttribute RequestProduct form, Model model) {
        commonProcess("add", model); // 상품 등록, 수정 같이.
        form.setGid(UUID.randomUUID().toString());
        return "admin/product/add";
    }

    /**
     * 상품 정보 수정
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/edit/{seq}")
    public String edit(@PathVariable("seq") Long seq, Model model) {
        commonProcess("edit", model); // 상품 등록, 수정 같이.

        return "admin/product/edit";
    }

    /**
     * 상품 등록, 수정 처리
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestProduct form, Errors errors, Model model) {
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "add";
        commonProcess(mode, model); // 상품 등록, 수정 같이.

        if (errors.hasErrors()) {
            String gid = form.getGid();
            form.setMainImages(fileInfoService.getList(gid, "main", FileStatus.ALL));
            form.setListImages(fileInfoService.getList(gid, "list", FileStatus.ALL));
            form.setEditorImages(fileInfoService.getList(gid, "editor", FileStatus.ALL));
            return "admin/product/" + mode;
        }

        // 상품 등록, 수정 처리 서비스

        return "redirect:/admin/product/list";
    }

    /**
     * 상품 분류 목록
     * @param model
     * @return
     */
    @GetMapping("/category")
    public String categoryList(Model model) {
        commonProcess("category", model);

        return "admin/product/category/list";
    }

    /**
     * 분류 등록
     * @param model
     * @return
     */
    @GetMapping({"/category/add", "/category/edit/{cate}"})
    public String categoryUpdate(@PathVariable(name = "cate", required = false) String cate, Model model) {
        commonProcess("category", model);

        return "admin/product/category/add";
    }

    /**
     * 분류 등록, 수정 처리
     * @param model
     * @return
     */
    @PostMapping("/category/save")
    public String categorySave(Model model) {
        commonProcess("category", model);

        return "redirect:admin/product/category";
    }


    /**
     * 배송 정책 관리
     * @param model
     * @return
     */
    @GetMapping("/delivery")
    public String delivery(Model model) {
        commonProcess("delivery", model);

        return "admin/product/delivery/list";
    }
    /**
     * 공통 처리 부분
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        model.addAttribute("subMenuCode", mode);
    }
}



















