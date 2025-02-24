package com.example.exam.service;

import com.example.exam.entity.Question;
import java.util.List;

public interface MockExamService {
    List<Question> getRandomExamQuestions();
} 