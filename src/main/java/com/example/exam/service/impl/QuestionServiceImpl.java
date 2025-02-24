package com.example.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.exam.dto.QuestionDto;
import com.example.exam.entity.Chapter;
import com.example.exam.entity.Question;
import com.example.exam.entity.QuestionOption;
import com.example.exam.mapper.ChapterMapper;
import com.example.exam.mapper.QuestionMapper;
import com.example.exam.mapper.QuestionOptionMapper;
import com.example.exam.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionOptionMapper questionOptionMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public List<QuestionDto> getAllQuestions() {
        List<Question> questions = questionMapper.selectList(null);
        return questions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDto getQuestionById(Long id) {
        // 获取题目实体
        Question question = questionMapper.selectById(id);

        // 如果没有找到该题目，返回 null 或者抛出异常
        if (question == null) {
            return null;  // 或者可以抛出一个自定义异常，如 throw new QuestionNotFoundException(id);
        }

        return convertToDto(question);  // 将 Question 实体转为 QuestionDto
    }

    @Override
    @Transactional
    public void addQuestion(QuestionDto questionDto) {
        Question question = convertToEntity(questionDto);

        // 处理选项字符串格式
        if (question.getOptions() != null && !question.getOptions().startsWith("[")) {
            // 将每行选项转换为JSON数组格式
            String[] options = question.getOptions().split("\n");
            StringBuilder jsonOptions = new StringBuilder("[");

            for (int i = 0; i < options.length; i++) {
                if (i > 0) {
                    jsonOptions.append(", ");
                }
                jsonOptions.append("\"").append(options[i].trim()).append("\"");
            }
            jsonOptions.append("]");
            question.setOptions(jsonOptions.toString());
        }

        questionMapper.insert(question);  // 插入题目
    }

    @Override
    @Transactional
    public void updateQuestion(QuestionDto questionDto) {
        Question question = convertToEntity(questionDto);
        questionMapper.updateById(question);  // 更新题目
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) {
        try {
            // 先删除题目选项
            questionOptionMapper.delete(
                    new QueryWrapper<QuestionOption>()
                            .eq("question_id", id)
            );

            // 再删除题目
            questionMapper.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("删除题目失败：" + e.getMessage());
        }
    }

    private QuestionDto convertToDto(Question question) {
        if (question == null) {
            return null;
        }

        QuestionDto dto = new QuestionDto();
        BeanUtils.copyProperties(question, dto);  // 将 Question 转换为 QuestionDto

        // 获取章节名称
        Chapter chapter = chapterMapper.selectById(question.getChapterId());
        if (chapter != null) {
            dto.setChapterName(chapter.getName());
        }

        return dto;
    }

    private Question convertToEntity(QuestionDto dto) {
        if (dto == null) {
            return null;
        }

        Question question = new Question();
        BeanUtils.copyProperties(dto, question);  // 将 QuestionDto 转换为 Question
        return question;
    }
}
