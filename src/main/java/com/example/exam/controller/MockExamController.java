package com.example.exam.controller;

import com.example.exam.dto.ExamSubmitDto;
import com.example.exam.entity.ExerciseRecord;
import com.example.exam.entity.Question;
import com.example.exam.entity.User;
import com.example.exam.mapper.ExerciseRecordMapper;
import com.example.exam.mapper.QuestionMapper;
import com.example.exam.service.MockExamService;
import com.example.exam.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mock-exams")
public class MockExamController {

    @Autowired
    private MockExamService mockExamService;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ExerciseRecordMapper exerciseRecordMapper;

    @GetMapping
    public String startExam(Model model) {
        List<Question> questions = mockExamService.getRandomExamQuestions();

        // 按题型分类
        Map<String, List<Question>> questionsByType = questions.stream()
                .collect(Collectors.groupingBy(Question::getType));

        model.addAttribute("singleChoiceQuestions",
                questionsByType.getOrDefault("SINGLE_CHOICE", List.of()));
        model.addAttribute("multipleChoiceQuestions",
                questionsByType.getOrDefault("MULTIPLE_CHOICE", List.of()));
        model.addAttribute("trueFalseQuestions",
                questionsByType.getOrDefault("TRUE_FALSE", List.of()));

        return "mock-exam/exam";
    }

    @PostMapping("/submit")
    public String submitExam(@RequestParam String answers, Model model) {
        try {
            // 获取当前用户
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getUserByUsername(username);

            // 解析答案
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, String>> answerList = mapper.readValue(answers,
                    new TypeReference<List<Map<String, String>>>() {});

            if (answerList.isEmpty()) {
                throw new RuntimeException("答案不能为空");
            }

            // 获取题目ID列表
            List<Long> questionIds = answerList.stream()
                    .map(answer -> Long.parseLong(answer.get("questionId")))
                    .collect(Collectors.toList());

            // 获取题目列表
            List<Question> questions = questionMapper.selectBatchIds(questionIds);

            // 计算得分并收集答题情况
            int correctCount = 0;
            List<Map<String, Object>> questionResults = new ArrayList<>();

            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                String userAnswer = answerList.get(i).get("answer");
                boolean isCorrect = false;
                // 判断答案
                // 判断用户是否作答
//                if (userAnswer == null || userAnswer.trim().isEmpty()) {
//                    userAnswer = "未作答";
//                    System.out.println(userAnswer);
//                }else{

                    String correctAnswer = question.getCorrectAnswer(); // 假设正确答案是字母
                    System.out.println(correctAnswer+"对的");
                    System.out.println(userAnswer+"用户");
                    if(correctAnswer.equalsIgnoreCase("TRUE")){
                        correctAnswer="TRUE";

                    }else if(correctAnswer.equalsIgnoreCase("FALSE")){
                        correctAnswer="FALSE";
                    }else{
                        correctAnswer=correctAnswer;
                    }
                    isCorrect = correctAnswer.equalsIgnoreCase(userAnswer);

                    if (isCorrect) {
                        correctCount++;
                    }
//                }


                Map<String, Object> result = new HashMap<>();
                result.put("content", question.getContent());
                result.put("userAnswer", formatAnswer(userAnswer, question.getType()));
                result.put("correctAnswer", formatAnswer(question.getCorrectAnswer(), question.getType()));
                result.put("isCorrect", isCorrect);
                result.put("analysis", question.getAnalysis());
                questionResults.add(result);
            }

            // 计算正确率
            double correctRate = questions.isEmpty() ? 0 : (correctCount * 100.0 / questions.size());

            // 保存练习记录
            ExerciseRecord record = new ExerciseRecord();
            record.setUserId(user.getId());
            record.setType("模拟考试");
            record.setQuestionCount(questions.size());
            record.setCorrectRate(correctRate);
            record.setCreatedAt(LocalDateTime.now());
            exerciseRecordMapper.insert(record);

            // 添加结果到模型
            model.addAttribute("questions", questionResults);
            model.addAttribute("correctCount", correctCount);
            model.addAttribute("totalCount", questions.size());
            model.addAttribute("correctRate", correctRate);

            return "mock-exam/result";
        } catch (Exception e) {
            throw new RuntimeException("提交失败：" + e.getMessage());
        }
    }

    private String formatAnswer(String answer, String type) {
        if (answer == null || answer.trim().isEmpty()) {
            return "未作答";
        }


        switch (type) {
            case "TRUE_FALSE":
                return "TRUE".equalsIgnoreCase(answer) ? "√" : "×";
            case "SINGLE_CHOICE":
            case "MULTIPLE_CHOICE":
                return answer;
            default:
                return answer;
        }
    }
}