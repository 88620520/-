package com.example.exam.service.impl;

import com.example.exam.dto.ExamDto;
import com.example.exam.dto.ExamRequestDto;
import com.example.exam.service.ExamService;
import com.example.exam.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public ExamDto generateExam(ExamRequestDto requestDto) {
        // 实现生成试卷的逻辑
        return new ExamDto();
    }
} 