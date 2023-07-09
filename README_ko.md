# ARCH4J (Application Archetype for Java) 

## 데모 사이트

* 서비스 페이지: [https://arch4j-web.oopscraft.org](https://arch4j-web.oopscraft.org)

* 관리자 콘솔: [https://arch4j-web.oopscraft.org/admin](https://arch4j-web.oopscraft.org/admin)

테스트 계정: dev/dev

## 주요 기능

제공되는 주요기능은 다음과 같다.

### 관리자 웹콘솔

/admin 으로 접속 시 시스템 관리콘솔 접속

### 디자인테마

Thymeleaf 템플릿 기반 테마 기능

### 사용자/권한그룹/권한

데이터베이스 + Spring Security 기반 사용자, 역할, 권한 기능

### 다중 게시판

스킨,파일첨부,댓글 다중 게시판 기능

### 동적 페이지

데이터베이스 기반 동적 컨텐츠 페이지 구성 기능

### Git 연동으로 Markdown 컨텐츠

Git 연동으로 Markdown 페이지 서비스 기능

### 메뉴관리

데이터베이스 기반 동적 메뉴 기능

### 이메일 템플릿 

이메일 템플릿 기능

### 추가개발 시 필요한 메시지,변수관리,공통코드 기능

추가 비지니스 개발 시 사용될 공통 기능


## 데이터베이스 생성 

### Mysql
```sql
create database arch4j;
create user 'user'@'%' identified by 'password';
grant all privileges on arch4j.* to 'user'@'%';
```


