package com.example.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exercise_question_records")
public class ExerciseQuestionRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long exerciseRecordId;
    private Long questionId;
    private String userAnswer;
    private Boolean isCorrect;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setCorrect(boolean correct) {
        this.isCorrect = correct;
    }
} 