### 커밋 메시지

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

### Directory Achitecture
```
├── HELP.md
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── WealthTracker
    │   │           └── demo
    │   │               ├── DTO
    │   │               │   └── test
    │   │               ├── DemoApplication.java
    │   │               ├── config
    │   │               │   └── test
    │   │               ├── controller
    │   │               │   └── test
    │   │               ├── domain
    │   │               │   └── test
    │   │               ├── enums
    │   │               │   └── test
    │   │               ├── error
    │   │               │   └── test
    │   │               ├── repository
    │   │               │   └── test
    │   │               ├── service
    │   │               │   └── test
    │   │               └── util
    │   │                   └── test
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── com
                └── WealthTracker
                    └── demo
                        └── DemoApplicationTests.java


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
