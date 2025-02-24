package com.example.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.exam.dto.LoginDto;
import com.example.exam.dto.RegisterDto;
import com.example.exam.dto.ExerciseRecordDto;
import com.example.exam.entity.ExerciseRecord;
import com.example.exam.entity.User;
import com.example.exam.mapper.ExerciseRecordMapper;
import com.example.exam.mapper.UserMapper;
import com.example.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.BeanUtils;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Primary
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ExerciseRecordMapper exerciseRecordMapper;

    @Override
    public void register(RegisterDto registerDto) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(registerDto.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 验证密码确认
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setRole("ROLE_USER");

        userMapper.insert(user);
    }

    @Override
    public String login(LoginDto loginDto) {
        try {
            System.out.println("Attempting login for user: " + loginDto.getUsername());
            
            // 直接使用 Spring Security 的表单登录，不需要手动认证
            return "登录成功";
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("用户名或密码错误: " + e.getMessage());
        }
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public List<ExerciseRecordDto> getRecentExerciseRecords(String username) {
        List<ExerciseRecord> records = exerciseRecordMapper.findRecentRecords(username);
        return records.stream().map(record -> {
            ExerciseRecordDto dto = new ExerciseRecordDto();
            BeanUtils.copyProperties(record, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(
            new QueryWrapper<User>().eq("username", username)
        );
        
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        
        String role = user.getRole().replace("ROLE_", "");
        
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(role)
            .build();
    }

} 