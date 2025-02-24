package com.example.exam.service.impl;

import com.example.exam.dto.StatisticsDto;
import com.example.exam.dto.ChapterStatDto;
import com.example.exam.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Override
    public StatisticsDto getUserStatistics() {
        StatisticsDto stats = new StatisticsDto();
        // TODO: 从数据库获取实际数据
        stats.setTotalExercises(100);
        stats.setTotalQuestions(500);
        stats.setAverageScore(85.5);
        stats.setTotalTime(10);
        stats.setCorrectRate(80.0);
        return stats;
    }

    @Override
    public Map<String, Object> getExerciseTrend() {
        Map<String, Object> trend = new HashMap<>();
        // TODO: 从数据库获取实际数据
        trend.put("dates", List.of("2024-01", "2024-02", "2024-03"));
        trend.put("counts", List.of(50, 80, 120));
        return trend;
    }

    @Override
    public Map<String, Object> getQuestionTypeDistribution() {
        Map<String, Object> distribution = new HashMap<>();
        // TODO: 从数据库获取实际数据
        distribution.put("labels", List.of("单选题", "多选题", "判断题"));
        distribution.put("data", List.of(60, 30, 10));
        return distribution;
    }

    @Override
    public List<ChapterStatDto> getChapterStatistics() {
        List<ChapterStatDto> stats = new ArrayList<>();
        // TODO: 从数据库获取实际数据
        ChapterStatDto stat = new ChapterStatDto();
        stat.setName("第一章");
        stat.setExerciseCount(50);
        stat.setQuestionCount(200);
        stat.setCorrectRate(85.0);
        stat.setMasteryRate(75.0);
        stats.add(stat);
        return stats;
    }
} 