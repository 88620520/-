package com.example.exam.service;

import com.example.exam.dto.StatisticsDto;
import com.example.exam.dto.ChapterStatDto;
import java.util.Map;
import java.util.List;

public interface StatisticsService {
    StatisticsDto getUserStatistics();
    Map<String, Object> getExerciseTrend();
    Map<String, Object> getQuestionTypeDistribution();
    List<ChapterStatDto> getChapterStatistics();
} 