package com.example.exam.dto;

import lombok.Data;

@Data
public class ExamRequestDto {
    private Long chapterId; // 如果为null则生成整门课程的试卷
    private Integer singleChoiceCount;
    private Integer multipleChoiceCount;
    private Integer trueFalseCount;
} 