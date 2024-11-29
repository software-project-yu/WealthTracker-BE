# WealthTracker - 💵개인 재무 관리 플랫폼
<img src="https://github.com/user-attachments/assets/ef4c9b71-0899-409a-88ae-918eb9acd8a4" style="width:100%"/>

# 🔖 Version
| Version | Revision Date | Description                                                                                      | Distributor |
|:-------:|:-------------:|:-------------------------------------------------------------------------------------------------|:-----------:|
| 1.0.0   | 2024.11.24      | Request DTO 유효성 검사<br>목표 API<br>비밀번호 재설정 수정<br>지출 목표 CRUD API 구현<br>회원 가입, 이메일 로직 변경<br>회원가입 DTO test code 추가 | 김도연      |
| 1.0.1 | 2024.11.28 | 비밀번호 확인 API 추가 | 김도연 |
| 1.1.0 | 2024.11.29 | 결제 예정 API 추가<br>예외처리 강화<br>지출 목표 Swagger API 형식 작성|김도연|

<br/>

## 👥 Team

|<img src="https://avatars.githubusercontent.com/u/144890194?s=400&u=89b20ce0f01d59364fe15b04bd5a7b2cdb5045a1&v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/144890194?s=400&u=89b20ce0f01d59364fe15b04bd5a7b2cdb5045a1&v=4" width="150" height="150"/> | <img src="https://avatars.githubusercontent.com/u/181474874?v=4" width="150" height="150"/>  |
|:-:|:-:|:-:|
|김도연<br/>[@tkv00](https://github.com/tkv00)|박재성<br/>[@pjs1710](https://github.com/pjs1710)|정현아<br/>[@hyunaaaj](https://github.com/hyunaaaj)

## ✏️ Responsibilities
- 김도연
    - AWS EC2, AWS RDS 서버 배포
    - GitHub Action,AWS Code Deploy를 이용한 CI/CD
    - ERD 구성
    - ErrorCode 에러 처리 구체화
    - Google AI ( GEMINI API )를 이용한 사용자 맞춤 주별 피드백 API
    - Scheduler를 이용하여 일요일 23시 59분 59초 매주 사용자 피드백 초기화 구현
    - 카테코리별 지출 API 구현
    - 카테고리별 수입 API 구현
    - Release 관리
      
- 박재성
    - Spring Security Config 설정
    - ERD 구성
    - JavaMailSender를 활용한 이메일 인증 기반 회원가입 API 구현
    - Naver SMTP를 이용한 Custom 인증코드 전송 구현
    - 이메일 로그인 API 구현
    - Json Web Token ( JWT )을 활용한 로그인 사용자 보안 강화 및 관리
    - 프로필 정보 관리 API 구현
    - 월별 저축 목표 및 일별 목표 금액 저축 API 구현
    - 카테고리별 지출 목표 API 구현
    
- 정현아
<br/>

## 👋 Commit Message

| emoji | message | description |
| --- | --- | --- |
| :sparkles: | feat | 새로운 기능 추가, 기존 기능을 요구 사항에 맞추어 수정 |
| :bug: | fix | 기능에 대한 버그 수정 |
| :closed_book: | docs | 문서(주석) 수정 |
| :art: | style | 코드 스타일, 포맷팅에 대한 수정 |
| :recycle: | refact | 기능 변화가 아닌 코드 리팩터링 |
| :white_check_mark: | test | 테스트 코드 추가/수정 |
| :pushpin: | chore | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore |
| 🛠️                 | merge   | 병합      |
| 🚑 | hotfix | master브랜치로 긴급 수정 |
| 🔖 | realese | 새 버젼 배포 시행 |

<br/>

## 📜 Directory Achitecture
```
.
└── demo
    ├── DTO
    │   ├── CustomUserInfoDTO.java
    │   ├── JwtResponseDTO.java
    │   ├── LoginRequestDTO.java
    │   ├── ReturnCodeDTO.java
    │   ├── SignupRequestDTO.java
    │   ├── VerificationCodeConfirmDTO.java
    │   ├── VerificationCodeRequestDTO.java
    │   ├── dailysaving
    │   │   └── DailySavingRequestDTO.java
    │   ├── feedback
    │   │   ├── ChatRequest.java
    │   │   ├── ChatResponse.java
    │   │   ├── FeedbackResponseDTO.java
    │   │   └── Message.java
    │   ├── income_expend
    │   │   ├── ExpendCategoryAmountDTO.java
    │   │   ├── ExpendDateResponseDTO.java
    │   │   ├── ExpendDayResponseDTO.java
    │   │   ├── ExpendIncomeResponseDTO.java
    │   │   ├── ExpendRequestDTO.java
    │   │   ├── ExpendResponseDTO.java
    │   │   ├── IncomeRequestDTO.java
    │   │   └── IncomeResponseDTO.java
    │   └── target
    │       ├── TargetRequestDTO.java
    │       └── TargetResponseDTO.java
    ├── DemoApplication.java
    ├── config
    │   ├── GeminiRestTemplateConfig.java
    │   ├── SecurityConfig.java
    │   └── SwaggerConfig.java
    ├── constants
    │   ├── ErrorCode.java
    │   └── SuccessCode.java
    ├── controller
    │   ├── AuthController.java
    │   ├── ExpendController.java
    │   ├── ExpendIncomeController.java
    │   ├── FeedbackController.java
    │   ├── IncomeController.java
    │   ├── LoginController.java
    │   └── TargetController.java
    ├── domain
    │   ├── CategoryExpend.java
    │   ├── CategoryIncome.java
    │   ├── DailySaving.java
    │   ├── Expend.java
    │   ├── FeedBack.java
    │   ├── Income.java
    │   ├── Target.java
    │   ├── User.java
    │   └── VerificationCode.java
    ├── enums
    │   ├── Asset.java
    │   ├── Category_Expend.java
    │   └── Category_Income.java
    ├── error
    │   ├── CustomException.java
    │   └── GlobalExceptionHandler.java
    ├── repository
    │   ├── DailySavingRepository.java
    │   ├── ExpendCategoryRepository.java
    │   ├── ExpendRepository.java
    │   ├── FeedbackRepository.java
    │   ├── IncomeCategoryRepository.java
    │   ├── IncomeRepository.java
    │   ├── TargetRepository.java
    │   ├── UserRepository.java
    │   └── VerificationCodeRepository.java
    ├── service
    │   ├── CustomUserDetailsService.java
    │   ├── EmailService.java
    │   ├── EmailServiceImpl.java
    │   ├── FeedbackService.java
    │   ├── FeedbackServiceImpl.java
    │   ├── KakaoOAuth2UserService.java
    │   ├── LoginService.java
    │   ├── LoginServiceImpl.java
    │   ├── SignupService.java
    │   ├── SignupServiceImpl.java
    │   ├── income_expend
    │   │   ├── ExpendIncomeService.java
    │   │   ├── ExpendIncomeServiceImpl.java
    │   │   ├── ExpendService.java
    │   │   ├── ExpendServiceImpl.java
    │   │   ├── IncomeService.java
    │   │   └── IncomeServiceImpl.java
    │   └── target
    │       ├── TargetService.java
    │       └── TargetServiceImpl.java
    └── util
        ├── CategoryExpendInitializer.java
        ├── JwtAuthFilter.java
        ├── JwtUtil.java
        └── VerificationCodeUtil.java



```

- DTO : DTO 클래스
- config : 설정 클래스
- controller
- domain : 엔티티 클래스
- eums : enum 클래스
- error : 에러 클래스
- repository : JPA 레포지토리 클래스
- service
- util : 기능 클래스

## 📄 API Docs
#### 회원가입 로그인
![스크린샷 2024-11-30 02 07 18](https://github.com/user-attachments/assets/bc5fa66e-25bf-4e34-99d6-f3fd2254208a)

#### 지출 및 수입
![스크린샷 2024-11-30 02 05 13](https://github.com/user-attachments/assets/75313bee-af03-4b66-b6f7-5b42f3bb849a)
![스크린샷 2024-11-30 02 07 06](https://github.com/user-attachments/assets/bc0f9530-e385-458a-8b4c-50bb5929b5dd)

#### 피드백 및 지출예정
![스크린샷 2024-11-30 02 05 24](https://github.com/user-attachments/assets/7f171b6e-7b04-4d31-b511-04bbf0928d70)

#### 저축 목표
![스크린샷 2024-11-30 02 06 55](https://github.com/user-attachments/assets/02fedd7f-30b8-4178-933d-33a20a6fb9e3)

## Archictecture

### 1️⃣ ERD

![WealthTracker (1)](https://github.com/user-attachments/assets/72ad4a36-eba5-43bc-a8e3-1093a68419b2)

### 2️⃣ CI/CD
![sf](https://github.com/user-attachments/assets/7c97c013-5cd8-4423-b4e2-45182a12dc8c)


## 📕 Tech 
1️⃣ Framework & Library

- JDK: 21
- SpringBoot: 3.3.4
- Spring Boot Starter Security: 3.3.4
- Spring Boot Starter Data JPA: 3.3.4
- Spring Boot Starter Web: 3.3.4
- Spring Boot Starter WebSocket: 3.3.4
- Spring Boot Starter Batch: 3.3.4
- Spring Boot Starter Validation
- Spring Boot Starter Mail
- Spring Boot Starter JDBC
- Spring Boot DevTools
- Spring Doc Open API: 2.1.0
- Springfox Swagger UI: 3.0.0
- Thymeleaf Extras Spring Security 6
- QueryDsl: 5.0.0
- JSON Web Token (JJWT): 0.11.5
- Project Lombok: 1.18.30
- JUnit: 5
- Testcontainers: 1.19.7
- Apache HttpClient5: 5.2.25.RELEASE
- OpenFeign: 4.0.6
  
2️⃣ Build Tools
- Gradle: 7.6.4

3️⃣ Database
- MySQL 8.0.39

4️⃣ Infra
- AWS EC2
- AWS RDS
- AWS S3
- AWS NAT GATEWAY
- AWS VPC
- AWS CODEDEPLOY
- GitHub Actions
