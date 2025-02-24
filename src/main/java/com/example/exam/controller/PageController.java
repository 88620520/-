package com.example.exam.controller;

import com.example.exam.dto.RegisterDto;
import com.example.exam.entity.Chapter;
import com.example.exam.entity.Question;
import com.example.exam.service.UserService;
import com.example.exam.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(RegisterDto registerDto, Model model) {
        try {
            if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
                model.addAttribute("error", "两次输入的密码不一致");
                return "register";
            }
            
            userService.register(registerDto);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
} 