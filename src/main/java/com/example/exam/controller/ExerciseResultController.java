package com.example.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exercises/result")
public class ExerciseResultController {

    @GetMapping("/{id}")
    public String showResult(@PathVariable Long id, Model model) {
        // TODO: 从数据库获取练习结果
        model.addAttribute("score", 80);
        model.addAttribute("correctCount", 8);
        model.addAttribute("totalCount", 10);
        return "exercises/result";
    }
} 