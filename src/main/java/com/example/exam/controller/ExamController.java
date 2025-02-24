package com.example.exam.controller;

import com.example.exam.dto.ExamDto;
import com.example.exam.dto.ExamRequestDto;
import com.example.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    // 生成试卷
    @PostMapping("/generate")
    public ResponseEntity<ExamDto> generateExam(@RequestBody ExamRequestDto requestDto) {
        ExamDto exam = examService.generateExam(requestDto);
        return ResponseEntity.ok(exam);
    }
} 