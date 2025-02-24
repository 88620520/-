package com.example.exam.controller;

import com.example.exam.dto.ExerciseRecordDto;
import com.example.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RecordController {

    @Autowired
    private UserService userService;

    @GetMapping("/records")
    public String viewRecords(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ExerciseRecordDto> records = userService.getRecentExerciseRecords(username);
        model.addAttribute("records", records);
        return "exercises/records";  // 返回记录页面的视图名
    }
} 