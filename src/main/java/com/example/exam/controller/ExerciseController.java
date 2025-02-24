package com.example.exam.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.ui.Model;
import com.example.exam.dto.ExerciseSubmitDto;
import com.example.exam.entity.Question;
import com.example.exam.entity.User;
import com.example.exam.entity.ExerciseRecord;
import com.example.exam.entity.ExerciseQuestionRecord;
import com.example.exam.mapper.QuestionMapper;
import com.example.exam.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;
    
    @Autowired
    private QuestionMapper questionMapper;

    @PostMapping("/start")
    public ResponseEntity<?> startExercise(@RequestBody Map<String, Object> request) {
        Long chapterId = Long.valueOf(request.get("chapterId").toString());
        Integer questionCount = Integer.valueOf(request.get("questionCount").toString());
        
        // TODO: 调用 service 方法获取练习题目
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "练习开始"
        ));
    }

    @PostMapping("/type/submit")
    public String submitTypeExercise(@RequestBody ExerciseSubmitDto submitDto, Model model, HttpSession session) {
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
            
            if (userAnswer == null || userAnswer.trim().isEmpty()) {
                userAnswer = "未作答";
            } else {
                System.out.println(userAnswer);
                String result=userAnswer.replace(",","");
                System.out.println(question.getCorrectAnswer());
                isCorrect = question.getCorrectAnswer().equalsIgnoreCase(result);
                if (isCorrect) {
                    correctCount++;
                }
            }
            
            questionResult.put("content", question.getContent());
            questionResult.put("userAnswer", formatAnswer(userAnswer, question.getType()));
            questionResult.put("correctAnswer", formatAnswer(question.getCorrectAnswer(), question.getType()));
            questionResult.put("isCorrect", isCorrect);
            questionResult.put("analysis", question.getAnalysis());
            
            questionResults.add(questionResult);
        }
        
        // 计算正确率
        double correctRate = questions.isEmpty() ? 0 : (correctCount * 100.0 / questions.size());
        
        // 保存练习记录
        saveExerciseRecord(submitDto, questions, correctRate, session);
        
        // 添加结果到模型
        model.addAttribute("questions", questionResults);
        model.addAttribute("correctCount", correctCount);
        model.addAttribute("totalCount", questions.size());
        model.addAttribute("correctRate", correctRate);
        
        return "exercises/result";
    }

    private String formatAnswer(String answer, String type) {
        if (answer == null || answer.trim().isEmpty() || "未作答".equals(answer)) {
            return "未作答";
        }
        
        switch (type) {
            case "TRUE_FALSE":
                return "TRUE".equalsIgnoreCase(answer) ? "√" : "×";
            case "SINGLE_CHOICE":
                return answer;  // 答案已经是字母格式
            case "MULTIPLE_CHOICE":
                // 确保多选题答案按字母顺序排序
                String filteredAnswer = answer.replaceAll("[^A-Za-z]", ""); // 只保留字母
                char[] chars = filteredAnswer.toCharArray();
//                char[] chars = answer.toCharArray();
                Arrays.sort(chars);
                return new String(chars);
            default:
                return answer;
        }
    }

    private void saveExerciseRecord(ExerciseSubmitDto submitDto, List<Question> questions, double correctRate, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return;

        // 创建练习记录
        ExerciseRecord record = new ExerciseRecord();
        record.setUserId(user.getId());
        record.setType("章节练习");
        record.setQuestionCount(questions.size());
        record.setCorrectRate(correctRate);
        exerciseService.saveExerciseRecord(record);

        // 创建题目记录
        List<ExerciseQuestionRecord> questionRecords = questions.stream().map(question -> {
            String userAnswer = submitDto.getAnswers().get(question.getId());
            boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(userAnswer);

            ExerciseQuestionRecord qRecord = new ExerciseQuestionRecord();
            qRecord.setExerciseRecordId(record.getId());
            qRecord.setQuestionId(question.getId());
            qRecord.setUserAnswer(userAnswer);
            qRecord.setCorrect(isCorrect);
            return qRecord;
        }).collect(Collectors.toList());

        exerciseService.saveExerciseQuestionRecords(questionRecords);
    }

    @GetMapping("/type/{type}")
    public String practiceByType(@PathVariable String type, Model model) {
        // 获取指定类型的所有题目
        List<Question> questions = questionMapper.selectList(
            new QueryWrapper<Question>()
                .eq("type", type)
                .orderByAsc("RAND()")
                .last("LIMIT 10")
        );
        
        if (questions.isEmpty()) {
            throw new RuntimeException("没有找到该类型的题目");
        }

        // 添加标题
        String title;
        switch (type) {
            case "SINGLE_CHOICE":
                title = "单选题练习";
                break;
            case "MULTIPLE_CHOICE":
                title = "多选题练习";
                break;
            case "TRUE_FALSE":
                title = "判断题练习";
                break;
            default:
                title = "题目练习";
        }

        model.addAttribute("questions", questions);
        model.addAttribute("type", type);
        model.addAttribute("title", title);
        model.addAttribute("isTypeExercise", true);  // 标记这是题型练习
        return "exercises/exercise";
    }
} 