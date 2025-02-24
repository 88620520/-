package com.example.exam.service.impl;

import com.example.exam.dto.ExerciseRequestDto;
import com.example.exam.dto.ExerciseResultDto;
import com.example.exam.entity.ExerciseRecord;
import com.example.exam.entity.ExerciseQuestionRecord;
import com.example.exam.mapper.ExerciseRecordMapper;
import com.example.exam.mapper.ExerciseQuestionRecordMapper;
import com.example.exam.service.ExerciseService;
import com.example.exam.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ExerciseRecordMapper exerciseRecordMapper;

    @Autowired
    private ExerciseQuestionRecordMapper exerciseQuestionRecordMapper;

    @Override
    public ExerciseResultDto startExercise(ExerciseRequestDto requestDto) {
        // 实现开始练习的逻辑
        return new ExerciseResultDto();
    }

    @Override
    public void submitExercise(ExerciseResultDto resultDto) {
        // 实现提交练习的逻辑
    }

    @Override
    @Transactional
    public void saveExerciseRecord(ExerciseRecord record) {
        exerciseRecordMapper.insert(record);
    }

    @Override
    @Transactional
    public void saveExerciseQuestionRecords(List<ExerciseQuestionRecord> records) {
        for (ExerciseQuestionRecord record : records) {
            exerciseQuestionRecordMapper.insert(record);
        }
    }
} 