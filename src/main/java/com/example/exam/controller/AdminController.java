package com.example.exam.controller;

import com.example.exam.dto.QuestionDto;
import com.example.exam.entity.Chapter;
import com.example.exam.service.ChapterService;
import com.example.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/questions")
    public String questionManagement(Model model) {
        List<QuestionDto> questions = questionService.getAllQuestions();
        List<Chapter> chapters = chapterService.getAllChapters();
        model.addAttribute("questions", questions);
        model.addAttribute("chapters", chapters);
        return "admin/questions";
    }

    @PostMapping("/questions/add")
    @ResponseBody
    public ResponseEntity<?> addQuestion(@RequestParam String type,
                                       @RequestParam String content,
                                       @RequestParam String options,
                                       @RequestParam String correctAnswer,
                                       @RequestParam String difficulty,
                                       @RequestParam Long chapterId,
                                       @RequestParam(required = false) String analysis) {
        try {
            QuestionDto questionDto = new QuestionDto();
            questionDto.setType(type);
            questionDto.setContent(content);
            questionDto.setOptions(options);
            questionDto.setCorrectAnswer(correctAnswer);
            questionDto.setDifficulty(difficulty);
            questionDto.setChapterId(chapterId);
            questionDto.setAnalysis(analysis);
            
            questionService.addQuestion(questionDto);
            return ResponseEntity.ok().body("题目添加成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("添加失败：" + e.getMessage());
        }
    }

    @PostMapping("/questions/delete")
    @ResponseBody
    public ResponseEntity<?> deleteQuestion(@RequestParam Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.ok().body("删除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/chapters")
    public String chapters() {
        return "admin/chapters";
    }

    @GetMapping("/courses")
    public String courses() {
        return "admin/courses";
    }

    @GetMapping("/users")
    public String users() {
        return "admin/users";
    }
} 