package com.example.exam.controller;

import com.example.exam.dto.StatisticsDto;
import com.example.exam.dto.ChapterStatDto;
import com.example.exam.dto.ExerciseRecordDto;
import com.example.exam.entity.User;
import com.example.exam.service.StatisticsService;
import com.example.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profile(Model model) {
        // 获取当前登录用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // 获取用户信息
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        return "user/profile";
    }

    @GetMapping("/history")
    public String history() {
        return "user/history";
    }

    @GetMapping("/wrong-questions")
    public String wrongQuestions() {
        return "user/wrong-questions";
    }

    @GetMapping("/statistics")
    public String statistics(Model model) {
        // 获取基础统计数据
        StatisticsDto statistics = statisticsService.getUserStatistics();
        model.addAttribute("statistics", statistics);

        // 获取练习趋势数据
        Map<String, Object> trendData = statisticsService.getExerciseTrend();
        model.addAttribute("trendData", trendData);

        // 获取题型分布数据
        Map<String, Object> typeData = statisticsService.getQuestionTypeDistribution();
        model.addAttribute("typeData", typeData);

        // 获取章节掌握度数据
        List<ChapterStatDto> chapterStats = statisticsService.getChapterStatistics();
        model.addAttribute("chapterStats", chapterStats);

        return "user/statistics";
    }

} 