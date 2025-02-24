package com.example.exam.service;

import com.example.exam.entity.Chapter;
import com.example.exam.entity.Question;
import java.util.List;

public interface ChapterService {
    List<Chapter> getAllChapters();
    Chapter getChapterById(Long id);
    List<Question> getQuestionsByChapterId(Long chapterId);
} 