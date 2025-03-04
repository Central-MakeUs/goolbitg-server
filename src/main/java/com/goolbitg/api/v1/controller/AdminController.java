package com.goolbitg.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.goolbitg.api.model.NoticeType;
import com.goolbitg.api.v1.service.NoticeService;

import lombok.extern.slf4j.Slf4j;

/**
 * AdminController
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/login")
    public String login(
        @RequestParam(name = "error", required = false) String error,
        Model model
    ) {
        if (error != null) {
            model.addAttribute("errorMsg", "Login Failed");
        }
        return "login";
    }

    @GetMapping
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/notice")
    public String notice(Model model) {
        return "notice";
    }

    @PostMapping("/notice/broadcast")
    public String broadcast(
        @RequestParam(name = "type") String type,
        @RequestParam(name = "message") String message,
        RedirectAttributes redirectAttributes
    ) {
        try {
            noticeService.broadcast(message, NoticeType.fromValue(type));
            redirectAttributes.addFlashAttribute("resultMsg", "성공적으로 전송되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errMsg", "전송 실패: " + e.getMessage());
        }
        return "redirect:/admin/notice";
    }

    @GetMapping("/buyornot")
    public String buyornot(Model model) {
        return "buyornot";
    }

    @GetMapping("/user")
    public String user(Model model) {
        return "user";
    }
}
