package com.example.exam.service;

import com.example.exam.dto.ExamDto;
import com.example.exam.dto.ExamRequestDto;

public interface ExamService {
    ExamDto generateExam(ExamRequestDto requestDto);
} 