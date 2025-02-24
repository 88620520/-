package com.example.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mock_exams")
public class MockExam {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String questions;  // JSON 格式的题目列表
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 