package com.example.exam.service.impl;

import com.example.exam.entity.Chapter;
import com.example.exam.entity.Question;
import com.example.exam.mapper.ChapterMapper;
import com.example.exam.mapper.QuestionMapper;
import com.example.exam.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Chapter> getAllChapters() {
        return chapterMapper.selectList(null);
    }

    @Override
    public Chapter getChapterById(Long id) {
        return chapterMapper.selectById(id);
    }

    @Override
    public List<Question> getQuestionsByChapterId(Long chapterId) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        return questionMapper.selectList(wrapper);
    }
} 