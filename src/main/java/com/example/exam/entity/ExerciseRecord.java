package com.example.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exercise_records")
public class ExerciseRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String type;
    private Integer questionCount;
    private Double correctRate;
    private LocalDateTime createdAt;
} 