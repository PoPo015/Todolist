# 요구사항
Todo list 
- 사용자는 회원가입을 할 수 있다.
- 사용자는 회원탈퇴를 할 수 있다.
- 사용자는 로그인을 할 수 있다.
- 사용자는 Todo 항목을 등록 할 수 있다.
- 사용자는 Todo 항목을 상태를 수정 할 수 있다.


# 개발 환경 
- **Java Version**: 17
- **Spring Boot Version**: 3.3
- **Database**: MySQL 8.0
- **ORM**: JPA 

# 운영 환경
- On-premise Server

# 접속 URL
#### 해당 UI는 모바일 화면에 최적화 되어있습니다.
- 회원가입 : http://popo015.servehttp.com/html/signup.html
- 회원탈퇴 : http://popo015.servehttp.com/html/withdraw.html
- 로그인 : http://popo015.servehttp.com/html/login.html
- TODO : http://popo015.servehttp.com/html/todo.html


### 테스트 계정

| ID                | PW       |
|-------------------|----------|
| popo015@naver.com | password1234 |

# 서비스 Flow

# ERD
![스크린샷 2024-06-20 오후 4 36 48](https://github.com/PoPo015/Todolist/assets/62968441/68e032a0-5c63-4e31-8ac6-4fd368576484)

### DDL

```
CREATE TABLE tb_refresh_token (
	id BIGINT unsigned NOT NULL AUTO_INCREMENT COMMENT '아이디',
    user_id BIGINT NOT NULL COMMENT '유저_아이디',
    token VARCHAR(255) NOT NULL COMMENT '리프레시_토큰',
    create_dt datetime NOT NULL COMMENT '등록시간',
    PRIMARY KEY (id),
    KEY `idx_refresh_token_01` (`token`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='유저_리프레시_토큰';
```

```
CREATE TABLE tb_user (
    id BIGINT unsigned NOT NULL AUTO_INCREMENT COMMENT '아이디',
    email VARCHAR(100) NOT NULL COMMENT '이메일',
    password VARCHAR(255) NOT NULL COMMENT '패스워드',
    nick_name VARCHAR(50) NOT NULL COMMENT '닉네임',
    create_dt datetime NOT NULL COMMENT '가입 시간',
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='유저';
```

```
CREATE TABLE `tb_todo` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '아이디',
  `user_id` bigint NOT NULL COMMENT '유저_아이디',
  `title` varchar(255) NOT NULL COMMENT '타이틀제목',
  `description` varchar(2000) NULL COMMENT '설명',
  `status` varchar(10) NOT NULL COMMENT '상태',
  `create_dt` datetime NOT NULL COMMENT '등록시간',
  `update_dt` datetime NULL COMMENT '최종수정시간',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='TODO';
```