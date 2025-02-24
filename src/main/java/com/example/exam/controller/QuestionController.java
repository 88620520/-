package com.example.exam.controller;

import com.example.exam.dto.QuestionDto;
import com.example.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/add")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionDto questionDto) {
        questionService.addQuestion(questionDto);
        return ResponseEntity.ok("题目添加成功");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editQuestion(@PathVariable Long id, @RequestBody QuestionDto questionDto) {
        questionDto.setId(id);
        questionService.updateQuestion(questionDto);
        return ResponseEntity.ok("题目编辑成功");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("题目删除成功");
    }

    @GetMapping("/list")
    public ResponseEntity<List<QuestionDto>> listQuestions() {
        List<QuestionDto> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    // 新增：获取单个题目
    @GetMapping("/get/{id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable Long id) {
        QuestionDto questionDto = questionService.getQuestionById(id);
        if (questionDto == null) {
            return ResponseEntity.notFound().build();  // 如果没有找到该题目，返回 404
        }
        return ResponseEntity.ok(questionDto);  // 返回找到的题目
    }
}
