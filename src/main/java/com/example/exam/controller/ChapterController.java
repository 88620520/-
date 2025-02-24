package com.example.exam.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.exam.dto.ExerciseSubmitDto;
import com.example.exam.entity.Chapter;
import com.example.exam.entity.ExerciseRecord;
import com.example.exam.entity.Question;
import com.example.exam.entity.User;
import com.example.exam.mapper.ExerciseRecordMapper;
import com.example.exam.mapper.QuestionMapper;
import com.example.exam.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/exercises")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ExerciseRecordMapper exerciseRecordMapper;

    @GetMapping
    public String listChapters(Model model) {
        List<Chapter> chapters = chapterService.getAllChapters();
        model.addAttribute("chapters", chapters);
        return "exercises/chapter-list";
    }

    @GetMapping("/{chapterId}")
    public String getChapterQuestions(@PathVariable Long chapterId, Model model) {
        // 获取章节信息
        Chapter chapter = chapterService.getChapterById(chapterId);
        if (chapter == null) {
            throw new RuntimeException("章节不存在");
        }

        // 获取章节的题目
        List<Question> questions = questionMapper.selectList(
                new QueryWrapper<Question>()
                        .eq("chapter_id", chapterId)
                        .orderByAsc("RAND()")
                        .last("LIMIT 10")
        );

        model.addAttribute("chapter", chapter);
        model.addAttribute("questions", questions);
        model.addAttribute("isTypeExercise", false);  // 标记这是章节练习
        return "exercises/exercise";
    }

    @PostMapping("/submit")
    public String submitExercise(@RequestBody ExerciseSubmitDto submitDto, Model model, HttpSession session) {
        // 获取所有题目
        List<Long> questionIds = new ArrayList<>(submitDto.getAnswers().keySet());
        List<Question> questions = questionMapper.selectBatchIds(questionIds);

        // 计算每道题的得分和总分
        List<Map<String, Object>> questionResults = new ArrayList<>();
        int correctCount = 0;

        for (Question question : questions) {
            Map<String, Object> questionResult = new HashMap<>();
            String userAnswer = submitDto.getAnswers().get(question.getId());
            boolean isCorrect = false;

            // 格式化用户答案
            String formattedUserAnswer = formatAnswer(userAnswer, question.getType());
            if (formattedUserAnswer != null && !formattedUserAnswer.trim().isEmpty()) {
               String qgca=question.getCorrectAnswer();
                if(qgca.equalsIgnoreCase("TRUE")){
                    qgca="√";
                } else if (qgca.equalsIgnoreCase("FALSE")) {
                    qgca="×";
                }else{
                   qgca=qgca;
                }
                isCorrect = qgca.equalsIgnoreCase(formattedUserAnswer);
                if (isCorrect) {
                    correctCount++;
                }
            }

            questionResult.put("content", question.getContent());
            questionResult.put("userAnswer", formattedUserAnswer);
            questionResult.put("correctAnswer", formatAnswer(question.getCorrectAnswer(), question.getType()));
            questionResult.put("isCorrect", isCorrect);
            questionResult.put("analysis", question.getAnalysis());

            questionResults.add(questionResult);
        }

        // 计算正确率
        double correctRate = questions.isEmpty() ? 0 : (correctCount * 100.0 / questions.size());

        // 保存练习记录
        User user = (User) session.getAttribute("user");
        if (user != null) {
            ExerciseRecord record = new ExerciseRecord();
            record.setUserId(user.getId());
            record.setType("章节练习");
            record.setQuestionCount(questions.size());
            record.setCorrectRate(correctRate);
            record.setCreatedAt(LocalDateTime.now());
            exerciseRecordMapper.insert(record);
        }

        // 添加结果到模型
        model.addAttribute("questions", questionResults);
        model.addAttribute("correctCount", correctCount);
        model.addAttribute("totalCount", questions.size());
        model.addAttribute("correctRate", correctRate);

        return "exercises/result";
    }

    private String formatAnswer(String answer, String type) {
        if (answer == null || answer.trim().isEmpty()) {
            return "未作答";
        }

        switch (type) {
            case "TRUE_FALSE":
                return "TRUE".equalsIgnoreCase(answer) ? "√" : "×";
            case "SINGLE_CHOICE":
                return answer;
            case "MULTIPLE_CHOICE":
                String filteredAnswer = answer.replaceAll("[^A-Za-z]", ""); // 只保留字母
                char[] chars = filteredAnswer.toCharArray();
                Arrays.sort(chars);

                return new String(chars);
            default:
                return answer;
        }
    }
}