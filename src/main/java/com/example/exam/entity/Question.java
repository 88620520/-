package com.example.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("questions")
public class Question {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long chapterId;
    private String type;
    private String content;
    private String options;
    private String correctAnswer;
    private String difficulty;
    private String analysis;
    
    @TableField(exist = false)
    private Chapter chapter;

    public List<String> getOptionList() {
        try {
            if (options == null || options.trim().isEmpty()) {
                return new ArrayList<>();
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(options, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
} 