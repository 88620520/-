-- 先清空用户表
DELETE FROM users WHERE username IN ('admin', 'test');

-- 插入管理员用户 (密码: admin123)
INSERT INTO users (username, password, email, role) VALUES 
('admin', '$2a$10$B0VHzvHOfbUCR97bOgWd8OIHDSZLhVsX9EByHi0KNa4BarKRe59Su', 'admin@example.com', 'ROLE_ADMIN')
ON DUPLICATE KEY UPDATE 
    password='$2a$10$B0VHzvHOfbUCR97bOgWd8OIHDSZLhVsX9EByHi0KNa4BarKRe59Su',
    email=VALUES(email),
    role=VALUES(role);

-- 插入测试用户 (密码: test1234)
INSERT INTO users (username, password, email, role) VALUES 
('test', '$2a$10$mvTx7f2UX.ww3jxK0OxO8uwxzTU2bqOuyuMVottMeP3RA5lfG/FI2', 'test@example.com', 'ROLE_USER');

-- 插入课程数据
INSERT INTO courses (id, name, description) 
VALUES 
(1, 'Java编程基础', 'Java语言基础知识与编程实践'),
(2, 'Spring框架开发', 'Spring框架相关技术栈学习'),
(3, '数据库原理', '数据库基础知识与SQL实践')
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description);

-- 插入章节数据
INSERT INTO chapters (id, name, description, sort_order, course_id, question_count) VALUES
(1, '第一章 Java基础语法', 'Java语言基本语法和数据类型', 1, 1, 4),
(2, '第二章 面向对象编程', '面向对象编程的核心概念和实践', 2, 1, 4),
(3, '第三章 集合框架', 'Java集合框架的使用和原理', 3, 1, 4)
ON DUPLICATE KEY UPDATE 
    name=VALUES(name), 
    description=VALUES(description), 
    sort_order=VALUES(sort_order), 
    course_id=VALUES(course_id),
    question_count=VALUES(question_count);

-- 先清空题目相关表
DELETE FROM exercise_question_records;
DELETE FROM exercise_records;
DELETE FROM question_options;
DELETE FROM questions;

-- 插入题目数据
INSERT INTO questions (chapter_id, type, content, options, correct_answer, difficulty) VALUES
-- 单选题
(1, 'SINGLE_CHOICE', 'Java中的基本数据类型不包括以下哪个？', 
   '["byte", "String", "int", "boolean"]', 
   'B', 'EASY'),
(1, 'SINGLE_CHOICE', '以下哪个不是Java的访问修饰符？', 
   '["public", "private", "friendly", "protected"]', 
   'C', 'MEDIUM'),
(1, 'SINGLE_CHOICE', 'Java中的方法重载是指？', 
   '["同一个类中方法名相同参数不同", "不同类中方法名相同", "同一个类中方法名不同", "父子类中方法名相同"]', 
   'A', 'MEDIUM'),
(1, 'SINGLE_CHOICE', 'Java中的字符串是否可变？', 
   '["可变", "不可变", "视情况而定", "取决于JVM"]', 
   'B', 'EASY'),

-- 多选题
(1, 'MULTIPLE_CHOICE', '以下哪些是Java中的包装类型？', 
   '["Integer", "Boolean", "String", "Character"]', 
   'ABD', 'MEDIUM'),
(1, 'MULTIPLE_CHOICE', '以下哪些是Java中的集合接口？', 
   '["List", "Queue", "Vector", "Stack"]', 
   'AB', 'HARD'),
(1, 'MULTIPLE_CHOICE', '以下哪些是线程安全的集合类？', 
   '["ArrayList", "Vector", "HashTable", "HashMap"]', 
   'BC', 'HARD'),

-- 判断题
(1, 'TRUE_FALSE', 'Java中的接口可以包含默认方法。', 
   NULL, 'TRUE', 'MEDIUM'),
(1, 'TRUE_FALSE', 'Java中的抽象类可以被实例化。', 
   NULL, 'FALSE', 'EASY'),
(1, 'TRUE_FALSE', 'Java支持多重继承。', 
   NULL, 'FALSE', 'EASY');

-- 更新题目解析
UPDATE questions 
SET analysis = '在Java中，String是一个类而不是基本数据类型。基本数据类型包括byte、short、int、long、float、double、boolean和char。'
WHERE content LIKE '%基本数据类型不包括%';

UPDATE questions 
SET analysis = '方法重载是指在同一个类中定义多个同名但参数不同的方法。参数的不同可以是参数个数、参数类型或参数顺序的不同。'
WHERE content LIKE '%方法重载是指%';

UPDATE questions 
SET analysis = 'Integer、Boolean和Character都是Java中的包装类，用于将基本数据类型转换为对象。String不是包装类，而是一个独立的类。'
WHERE content LIKE '%包装类型%';

UPDATE questions 
SET analysis = 'Java 8及以后的版本中，接口可以包含默认方法（使用default关键字修饰）和静态方法。'
WHERE content LIKE '%接口可以包含默认方法%';

-- 插入题目选项
INSERT INTO question_options (question_id, option_text, is_correct) VALUES
-- 基本数据类型题目的选项
(1, 'byte', false),
(1, 'String', true),
(1, 'int', false),
(1, 'boolean', false),

-- 访问修饰符题目的选项
(2, 'public', false),
(2, 'private', false),
(2, 'friendly', true),
(2, 'protected', false),

-- 方法重载题目的选项
(3, '同一个类中方法名相同参数不同', true),
(3, '不同类中方法名相同', false),
(3, '同一个类中方法名不同', false),
(3, '父子类中方法名相同', false),

-- 字符串可变性题目的选项
(4, '可变', false),
(4, '不可变', true),
(4, '视情况而定', false),
(4, '取决于JVM', false);

-- 插入练习记录
INSERT INTO exercise_records (id, user_id, type, question_count, correct_rate) VALUES
(1, 1, '章节练习', 3, 100.0),
(2, 1, '模拟考试', 3, 100.0),
(3, 1, '章节练习', 3, 66.7);

-- 插入练习题目记录
INSERT INTO exercise_question_records (exercise_record_id, question_id, user_answer, is_correct) VALUES
(1, 1, 'B', true),
(1, 2, 'AD', true),
(1, 3, 'TRUE', true),
(2, 1, 'B', true),
(2, 2, 'AD', true),
(2, 3, 'TRUE', true),
(3, 1, 'B', true),
(3, 2, 'AB', false),
(3, 3, 'TRUE', true)
ON DUPLICATE KEY UPDATE 
    user_answer=VALUES(user_answer), 
    is_correct=VALUES(is_correct);

-- 插入模拟考试
INSERT INTO mock_exams (id, name, description, questions) VALUES
(1, 'Java基础模拟考试 A', 'Java语言基础知识测试', 
   '[
     {"id": 1, "type": "SINGLE_CHOICE", "score": 10},
     {"id": 4, "type": "SINGLE_CHOICE", "score": 10},
     {"id": 5, "type": "SINGLE_CHOICE", "score": 10},
     {"id": 6, "type": "SINGLE_CHOICE", "score": 10},
     {"id": 7, "type": "MULTIPLE_CHOICE", "score": 10},
     {"id": 8, "type": "MULTIPLE_CHOICE", "score": 10},
     {"id": 9, "type": "MULTIPLE_CHOICE", "score": 10},
     {"id": 10, "type": "TRUE_FALSE", "score": 5},
     {"id": 11, "type": "TRUE_FALSE", "score": 5},
     {"id": 12, "type": "TRUE_FALSE", "score": 5}
   ]'),
(2, 'Java基础模拟考试 B', 'Java语言基础知识测试', 
   '[
     {"id": 4, "type": "SINGLE_CHOICE", "score": 10},
     {"id": 5, "type": "SINGLE_CHOICE", "score": 10},
     {"id": 6, "type": "SINGLE_CHOICE", "score": 10},
     {"id": 1, "type": "SINGLE_CHOICE", "score": 10},
     {"id": 8, "type": "MULTIPLE_CHOICE", "score": 10},
     {"id": 9, "type": "MULTIPLE_CHOICE", "score": 10},
     {"id": 7, "type": "MULTIPLE_CHOICE", "score": 10},
     {"id": 11, "type": "TRUE_FALSE", "score": 5},
     {"id": 12, "type": "TRUE_FALSE", "score": 5},
     {"id": 10, "type": "TRUE_FALSE", "score": 5}
   ]')
ON DUPLICATE KEY UPDATE 
    name=VALUES(name), 
    description=VALUES(description), 
    questions=VALUES(questions);

-- 插入考试记录
INSERT INTO mock_exam_records (id, user_id, exam_id, score) VALUES
(1, 1, 1, 90),
(2, 1, 2, 85)
ON DUPLICATE KEY UPDATE 
    score=VALUES(score);

-- 插入题目选项
INSERT INTO question_options (question_id, option_text, is_correct) VALUES
(1, 'byte', false),
(1, 'String', true),
(1, 'int', false),
(1, 'boolean', false),
(2, 'final', true),
(2, 'String', false),
(2, 'main', false),
(2, 'void', true),
-- 题目4的选项
(4, '同一个类中方法名相同参数不同', true),
(4, '不同类中方法名相同', false),
(4, '同一个类中方法名不同', false),
(4, '父子类中方法名相同', false),
-- 题目5的选项
(5, 'public', false),
(5, 'private', false),
(5, 'friendly', true),
(5, 'protected', false),
-- 题目6的选项
(6, '可变', false),
(6, '不可变', true),
(6, '视情况而定', false),
(6, '取决于JVM', false),
-- 题目7的选项
(7, 'Integer', true),
(7, 'Boolean', true),
(7, 'String', false),
(7, 'Character', true),
-- 题目8的选项
(8, 'List', true),
(8, 'Queue', true),
(8, 'Vector', false),
(8, 'Stack', false),
-- 题目9的选项
(9, 'ArrayList', false),
(9, 'Vector', true),
(9, 'HashTable', true),
(9, 'HashMap', false);

-- 更新单选题数据
UPDATE questions 
SET analysis = '在Java中，String是一个类而不是基本数据类型。基本数据类型包括byte、short、int、long、float、double、boolean和char。'
WHERE id = 1;

UPDATE questions 
SET analysis = '方法重载是指在同一个类中定义多个同名但参数不同的方法。参数的不同可以是参数个数、参数类型或参数顺序的不同。'
WHERE id = 4;

-- 更新多选题数据
UPDATE questions 
SET analysis = 'Integer、Boolean和Character都是Java中的包装类，用于将基本数据类型转换为对象。String不是包装类，而是一个独立的类。'
WHERE id = 7;

-- 更新判断题数据
UPDATE questions 
SET analysis = 'Java 8及以后的版本中，接口可以包含默认方法（使用default关键字修饰）和静态方法。'
WHERE id = 10; 