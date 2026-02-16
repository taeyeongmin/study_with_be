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

---

## 4. 요구사항 정의서

<details>
<summary><strong>요구사항 정의서 전체 펼치기</strong></summary>

---

## A. 스터디 그룹 관리

| 기능 ID | 기능명 | 요구사항 ID | 요구사항 내용 |
|--------|--------|------------|---------------|
| F-A-001 | 스터디 그룹 생성 | R1 | 로그인한 회원만 스터디 그룹을 생성할 수 있다 |
|        |        | R2 | 필수 입력값: 스터디명, 카테고리, 주제, 진행 방식, 정원, 스터디 소개 |
|        |        | R3 | 스터디 그룹 생성자는 해당 스터디의 방장이 된다 |
|        |        | R4 | 생성 시 초기 상태는 `RECRUITING`으로 설정된다 |
|        |        | R5 | 생성 시 현재 인원은 1명(방장 포함)으로 설정된다 |
|        |        | R6 | 생성된 스터디는 즉시 목록 조회 대상에 포함된다 |
|        |        | R7 | 스터디 상태는 `RECRUITING`, `RECRUIT_END`, `ONGOING`, `CLOSED`, `SUSPENDED` 중 하나를 가진다 |
| F-A-002 | 스터디 그룹 수정 | R1 | 방장만 스터디 그룹 정보를 수정할 수 있다 |
|        |        | R2 | 수정 가능 항목: 스터디명, 소개, 카테고리, 주제, 진행 방식, 지역, 정원, 일정 |
|        |        | R3 | 상태별 수정 제한: RECRUITING(전체), ONGOING(운영 정보만), CLOSED(불가) |
|        |        | R4 | 정원은 현재 인원 수보다 작은 값으로 변경할 수 없다 |
| F-A-003 | 스터디 그룹 삭제 | R1 | 모집중 상태이며 방장 혼자만 참여 중일 때만 삭제 가능하다 |
| F-A-004 | 매니저 권한 관리 | R1 | 매니저 권한 부여/회수는 방장만 가능하다 |
|        |        | R2 | 승인 완료된 스터디 회원에게만 매니저 권한을 부여할 수 있다 |
|        |        | R3 | 매니저는 최대 2명까지 지정 가능하다 |
|        |        | R4 | 매니저는 가입 신청 조회/승인/거부 및 공지 작성을 수행할 수 있다 |
|        |        | R5 | 매니저는 스터디 수정/삭제, 매니저 관리, 방장 권한 위임은 불가하다 |
|        |        | R6 | 매니저 권한 회수 시 즉시 반영된다 |
| F-A-005 | 스터디 그룹 목록 조회 | R1 | 비회원 및 회원 모두 스터디 목록 조회가 가능하다 |
|        |        | R2 | 필터 조건: 카테고리, 주제, 지역, 온/오프라인 |
|        |        | R3 | 모집 상태(모집중/마감) 필터가 가능하다 |
|        |        | R4 | 목록에는 스터디명, 카테고리, 모집 상태, 정원, 현재 인원이 표시된다 |
|        |        | R5 | 목록은 최신 생성순으로 정렬된다 |
|        |        | R6 | `SUSPENDED` 상태의 스터디는 목록 및 상세 조회에서 제외된다 |
| F-A-006 | 스터디 그룹 상세 조회 | R1 | 비회원 및 회원 모두 스터디 상세 조회가 가능하다 |
|        |        | R2 | 상세 정보에는 소개, 모집/현재 인원, 모집 상태, 일정, 방식, 마감일, 방장 정보가 포함된다 |
| F-A-006-1 | 공지글 관리 | R1 | 공지글 목록 조회는 승인된 회원만 가능하다 |
|        |        | R2 | 공지글에는 제목, 작성자, 작성일, 고정 여부가 포함된다 |
|        |        | R3 | 고정 공지는 목록 최상단에 노출된다 |
|        |        | R4 | 공지는 최신 작성순으로 정렬된다 |
|        |        | R5 | `SUSPENDED` 상태에서는 공지글 조회가 제한될 수 있다 |
|        |        | R6 | `CLOSED` 또는 `SUSPENDED` 상태에서는 공지글 작성이 제한된다 |
| F-A-007 | 스터디 그룹 탈퇴 | R1 | 로그인한 회원만 스터디 그룹 탈퇴가 가능하다 |
|        |        | R2 | 방장은 권한 위임 후에만 탈퇴할 수 있다 |
|        |        | R3 | 탈퇴 시 즉시 스터디 인원 수에 반영된다 |
|        |        | R4 | 탈퇴 후 스터디 접근 권한은 즉시 제거된다 |

---

## B. 가입 관리

| 기능 ID | 기능명 | 요구사항 ID | 요구사항 내용 |
|--------|--------|------------|---------------|
| F-B-001 | 그룹 가입 신청 | R1 | 로그인한 회원만 가입 신청이 가능하다 |
|        |        | R2 | `RECRUITING` 상태의 스터디만 신청 가능하다 |
|        |        | R3 | 이미 가입된 스터디에는 신청할 수 없다 |
|        |        | R4 | 중복 신청은 불가능하다 |
|        |        | R5 | 가입 신청 상태는 “대기”로 저장된다 |
| F-B-002 | 가입 승인/거부 | R1 | 방장만 가입 신청 목록을 조회할 수 있다 |
|        |        | R2 | 방장은 신청을 승인 또는 거부할 수 있다 |
|        |        | R3 | 승인 시 스터디 인원 수가 증가한다 |
|        |        | R4 | 정원이 초과되면 추가 승인은 불가능하다 |
|        |        | R5 | 거부된 신청의 재신청 가능 여부는 정책에 따른다 |
|        |        | R6 | 승인 시점에 상태가 RECRUITING/RECRUIT_END가 아니면 승인 불가 |
|        |        | R7 | 승인 시점에 정원이 초과되면 승인 불가 |
| F-B-003 | 가입 신청 취소 | R1 | 로그인한 회원만 가입 신청 취소가 가능하다 |
|        |        | R2 | 승인 대기 상태의 신청만 취소 가능하다 |
|        |        | R3 | 취소 시 신청 상태는 삭제 또는 “취소”로 변경된다 |

---

## C. 회원

| 기능 ID | 기능명 | 요구사항 ID | 요구사항 내용 |
|--------|--------|------------|---------------|
| F-C-001 | 회원 가입 및 로그인 | R1 | 카카오 로그인을 지원하며 최초 로그인 시 자동 회원 가입 처리 |
|        |        | R2 | 로컬 회원 가입을 지원하며 ID, 비밀번호, 이메일을 입력받는다 |
| F-C-002 | 마이페이지 | R1 | 내가 가입 신청한 스터디의 상태를 조회할 수 있다 |
|        |        | R2 | 과거에 진행했던 스터디 목록을 조회할 수 있다 |
|        |        | R3 | 회원 정보(닉네임, 이메일 등)를 수정할 수 있다 |
|        |        | R4 | 이메일 수신 여부를 설정할 수 있다 |

</details>



---

## 5. API 목록

<details>
<summary><strong>API 목록 전체 펼치기</strong></summary>

### 인증 / 회원

| Method | Endpoint | 설명 |
|------|---------|------|
| POST | `/api/auth/login` | 로컬 로그인 |
| POST | `/api/auth/signup` | 로컬 회원 가입 |
| POST | `/api/auth/logout` | 로그아웃 |
| POST | `/api/auth/token/refresh` | 토큰 재발급 |

---

### 스터디 그룹

| Method | Endpoint | 설명 |
|------|---------|------|
| GET | `/api/study-groups` | 스터디 그룹 목록 조회 |
| GET | `/api/study-groups/{studyGroupId}` | 스터디 그룹 상세 조회 |
| POST | `/api/study-groups` | 스터디 그룹 생성 |
| PUT | `/api/study-groups/{studyGroupId}` | 스터디 그룹 수정 |
| DELETE | `/api/study-groups/{studyGroupId}` | 스터디 그룹 삭제 |

---

### 가입 관리

| Method | Endpoint | 설명 |
|------|---------|------|
| POST | `/api/study-groups/{studyGroupId}/join-requests` | 가입 신청 |
| DELETE | `/api/join-requests/{joinRequestId}` | 가입 신청 취소 |
| GET | `/api/study-groups/{studyGroupId}/join-requests` | 가입 신청 목록 |
| POST | `/api/join-requests/{joinRequestId}/approve` | 가입 승인 |
| POST | `/api/join-requests/{joinRequestId}/reject` | 가입 거부 |
| DELETE | `/api/study-groups/{studyGroupId}/members/me` | 스터디 탈퇴 |

---

### 매니저 권한

| Method | Endpoint | 설명 |
|------|---------|------|
| GET | `/api/study-groups/{studyGroupId}/managers` | 매니저 목록 조회 |
| POST | `/api/study-groups/{studyGroupId}/managers` | 매니저 지정 |
| DELETE | `/api/study-groups/{studyGroupId}/managers/{memberId}` | 매니저 해제 |

---

### 공지글

| Method | Endpoint | 설명 |
|------|---------|------|
| GET | `/api/study-groups/{studyGroupId}/notices` | 공지 목록 조회 |
| POST | `/api/study-groups/{studyGroupId}/notices` | 공지 작성 |
| PUT | `/api/notices/{noticeId}` | 공지 수정 |
| DELETE | `/api/notices/{noticeId}` | 공지 삭제 |

---

### 마이페이지

| Method | Endpoint | 설명 |
|------|---------|------|
| GET | `/api/members/me` | 내 정보 조회 |
| PUT | `/api/members/me` | 내 정보 수정 |
| GET | `/api/members/me/studies` | 내 스터디 목록 |
| DELETE | `/api/members/me` | 회원 탈퇴 |

</details>



