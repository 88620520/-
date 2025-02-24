package com.example.exam.service.impl;

import com.example.exam.entity.Question;
import com.example.exam.mapper.QuestionMapper;
import com.example.exam.service.MockExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MockExamServiceImpl implements MockExamService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Question> getRandomExamQuestions() {
        List<Question> selectedQuestions = new ArrayList<>();
        
        // 获取所有题目并按类型分组
        List<Question> allQuestions = questionMapper.selectList(null);
        Map<String, List<Question>> questionsByType = allQuestions.stream()
            .collect(Collectors.groupingBy(Question::getType));
        
        // 随机选择4道单选题
        randomSelectQuestions(questionsByType.getOrDefault("SINGLE_CHOICE", new ArrayList<>()), 4)
            .forEach(selectedQuestions::add);
        
        // 随机选择4道多选题
        randomSelectQuestions(questionsByType.getOrDefault("MULTIPLE_CHOICE", new ArrayList<>()), 4)
            .forEach(selectedQuestions::add);
        
        // 随机选择4道判断题
        randomSelectQuestions(questionsByType.getOrDefault("TRUE_FALSE", new ArrayList<>()), 4)
            .forEach(selectedQuestions::add);
        
        return selectedQuestions;
    }

    private List<Question> randomSelectQuestions(List<Question> questions, int count) {
        if (questions == null || questions.isEmpty() || count <= 0) {
            return new ArrayList<>();
        }
        
        List<Question> shuffled = new ArrayList<>(questions);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }
} 