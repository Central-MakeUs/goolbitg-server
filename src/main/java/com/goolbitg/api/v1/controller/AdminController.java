package com.goolbitg.api.v1.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

/**
 * AdminController
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

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

    @GetMapping("/home")
    public String home(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);
        return "home";
    }
}
