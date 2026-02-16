# Study With Backend

스터디 그룹 모집 및 운영을 위한 백엔드 API 서버입니다.  
회원은 스터디 그룹을 생성하고, 가입 신청 / 승인 / 운영(공지, 매니저 권한 등)을 수행할 수 있습니다.

---

## 1. 프로젝트 개요

### 목적
- 스터디 그룹 **모집 → 가입 → 운영 → 종료** 전 과정을 지원
- 방장 / 매니저 / 일반 회원 역할 기반 스터디 운영
- 명확한 상태 전이 정책을 통한 안정적인 스터디 관리

### 주요 사용자
- **비회원**: 스터디 그룹 목록 및 상세 조회
- **회원**: 스터디 가입 신청, 참여, 탈퇴
- **방장**: 스터디 생성, 수정, 삭제, 매니저 지정
- **매니저**: 가입 승인/거부, 공지 관리

## 인증 방식

- JWT 기반 인증(bearer token)
- Access Token 사용
- OAuth2 (Kakao) 로그인 지원
---

## 2. 기술 스택

### Backend
- Java 21
- Spring Boot 3.5
- Spring Validation
- Spring Data JPA (Hibernate)
- Spring Security
- OAuth2 Client (kakao)

### Database
- MySQL

### Build & Docs
- Gradle
- Springdoc OpenAPI (Swagger UI)

---

## 3. 아키텍처 / 설계

### 아키텍처 개요
- DDD 기반 Layered Architecture
- 도메인 규칙 중심 설계
- JPA 의존성은 Infrastructure 계층으로 한정

### 설계 원칙
- Controller -> UseCase -> Domain <- Infrastructure 단방향 의존
- 도메인 규칙은 Entity / Domain Service에 위치
- 트랜잭션 및 인증/인가 처리는 Application 계층에서 관리
- 공통 응답 포맷 및 예외 처리 일관성 유지
- 쓰기와 읽기 패키지 분리(command/query)  
---


## 4. ERD
<img width="1247" height="661" alt="erd" src="https://github.com/user-attachments/assets/aed52267-89b7-41cc-a41b-b10c4fa6b414" />

---

## 5. Deployment

- Ubuntu (NCP)
- Nginx Reverse Proxy
- HTTPS 적용
- systemd 서비스 등록
  
---

## 6. API 목록
### [Swagger 이동 하기](http://localhost:8080/swagger-ui/index.html#/)


