package com.example.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.exam.entity.ExerciseRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ExerciseRecordMapper extends BaseMapper<ExerciseRecord> {
    @Select("SELECT er.* " +
            "FROM exercise_records er " +
            "WHERE er.user_id = (SELECT id FROM users WHERE username = #{username}) " +
            "ORDER BY er.created_at DESC " +
            "LIMIT 10")
    List<ExerciseRecord> findRecentRecords(String username);
} 