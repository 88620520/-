package com.example.exam.service;

import com.example.exam.dto.QuestionDto;
import java.util.List;

public interface QuestionService {
    List<QuestionDto> getAllQuestions();
    QuestionDto getQuestionById(Long id);  // 获取单个题目的接口
    void addQuestion(QuestionDto questionDto);
    void updateQuestion(QuestionDto questionDto);
    void deleteQuestion(Long id);
}
