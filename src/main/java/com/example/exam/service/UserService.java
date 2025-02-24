package com.example.exam.service;

import com.example.exam.dto.RegisterDto;
import com.example.exam.dto.LoginDto;
import com.example.exam.entity.User;
import com.example.exam.dto.ExerciseRecordDto;
import java.util.List;

public interface UserService {
    void register(RegisterDto registerDto);
    String login(LoginDto loginDto);
    void logout();
    User getUserByUsername(String username);
    List<ExerciseRecordDto> getRecentExerciseRecords(String username);

} 