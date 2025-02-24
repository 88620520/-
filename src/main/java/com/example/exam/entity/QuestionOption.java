package com.example.exam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("question_options")
public class QuestionOption {
    private Long id;
    private Long questionId;
    private String optionText;
    private Boolean isCorrect;
} 