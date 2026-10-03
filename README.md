# 여행 일정 관리 API

## ① 프로젝트 소개

여행 이름, 목적지, 시작일, 종료일, 예산, 메모를 관리하는 REST CRUD API입니다. HTML 화면과 데이터베이스 없이 메모리에 저장하며, 서버 재시작 시 데이터가 초기화됩니다.

- 개인 저장소: https://github.com/Evvvaaaaan/week05_webService
- Organization 저장소: https://github.com/2026-2-WebService/assign05-c01-22300404
- 배포 URL: https://week05-webservice-1.onrender.com/
- 배포 API: https://week05-webservice-1.onrender.com/api/trips

화면을 구현하지 않은 REST API 프로젝트이므로 루트 경로 `/`의 404는 정상입니다. API 확인에는 `/api/trips`를 사용합니다.

| 필드 | Java 타입 |
|---|---|
| id | Long — 서버에서 자동 생성 |
| title, destination, memo | String |
| startDate, endDate | LocalDate |
| budget | Integer — 원 단위 예산 |

### 프로젝트 구조

기본 패키지: `org.example.db.tripmanager`

```text
TripManagerApplication.java
controller/TripController.java
service/TripService.java
repository/TripRepository.java
repository/MemoryTripRepository.java
domain/Trip.java
dto/TripRequest.java
dto/TripResponse.java
```

### 로컬 실행과 API

IntelliJ에서 `build.gradle`을 열고 Gradle 동기화 후 `TripManagerApplication.main()`을 실행합니다. 로컬 주소는 `http://localhost:8082`입니다.

| Method | Endpoint | 기능 | 성공 상태 |
|---|---|---|---|
| POST | /api/trips | 등록 | 201 |
| GET | /api/trips | 전체 조회 | 200 |
| GET | /api/trips/{id} | 단건 조회 | 200 |
| PUT | /api/trips/{id} | 수정, ID 유지 | 200 |
| DELETE | /api/trips/{id} | 삭제 | 204 |
| GET | /api/trips?destination=제주 | 목적지 정확히 일치하는 여행 조회 | 200 |

잘못된 등록·수정 입력은 400, 없는 ID의 조회·수정·삭제는 404를 반환합니다.

### 실제 등록 요청·응답

`POST /api/trips` 요청:

```json
{
  "title": "README 테스트 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 300000,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 응답: **201 Created**

```json
{
  "id": 2,
  "title": "README 테스트 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 300000,
  "memo": "문서 작성용 임시 데이터"
}
```

## ② 개발환경 및 Dependency

| 항목 | 내용 |
|---|---|
| IDE | IntelliJ IDEA 2026.2.3, Ultimate 제품 코드 IU, 빌드 262.10968.63 |
| JDK | IntelliJ 프로젝트 SDK: Homebrew OpenJDK 17.0.17 (`homebrew-17`). Gradle toolchain: 17. 터미널 빌드·JAR 검증: OpenJDK 17.0.18 |
| Spring Boot | 4.1.1 |
| Build Tool | Gradle Wrapper 9.7.1 |
| 데이터 저장 | LinkedHashMap<Long, Trip> |
| 배포 환경 | Render Docker Web Service — https://week05-webservice-1.onrender.com/ |

프로젝트 생성 시 선택한 Dependency를 유지했습니다. 현재 REST API에 필요한 것은 Web MVC와 테스트 관련 Dependency입니다. WebClient, WebFlux, Web Services와 각각의 테스트 Dependency는 생성 시 함께 선택했지만 현재 코드에서는 직접 사용하지 않으며, 이번 과제 기능에 필수는 아닙니다.

| Dependency | 역할과 사용 이유 |
|---|---|
| spring-boot-starter-webmvc | HTTP 요청 처리와 JSON 응답에 사용 |
| spring-boot-starter-webmvc-test | 웹 요청 테스트 도구 |
| junit-platform-launcher | JUnit 테스트 실행 지원 |
| spring-boot-starter-webclient | 외부 HTTP 호출 도구. 현재 직접 사용하지 않음 |
| spring-boot-starter-webflux | 반응형 웹 기능. 현재 직접 사용하지 않음 |
| spring-boot-starter-webservices | SOAP 웹 서비스 기능. 현재 직접 사용하지 않음 |
| webclient-test, webflux-test, webservices-test | 해당 기능의 테스트 도구. 현재 직접 사용하지 않음 |

## ③ Solution 분석

참고: [Book CRUD Solution](https://github.com/2026-2-WebService/sb_book_crud_solution). 분석용 `sub/`는 Git에서 제외했습니다.

**Q1. 등록 요청은 어떤 메서드를 거치는가?**  
`BookController.create()`가 `BookService.create()`를 호출합니다. Service가 `BookRepository.save()`를 호출하면 실제 구현체인 `MemoryBookRepository.save()`가 저장합니다.

**Q2. BookRequest, Book, BookResponse의 역할은 무엇인가?**  
요청, 저장 데이터, 응답을 각각 담당합니다. `BookService.create()`에서 요청으로 `Book`을 만들고, `toResponse()`에서 `BookResponse`로 변환합니다.

**Q3. ID는 어디에서 생성되는가?**  
`MemoryBookRepository.save()`의 `book.setId(++sequence)`에서 생성됩니다.

**Q4. 없는 ID는 어떻게 404가 되는가?**  
`MemoryBookRepository.findById()`가 빈 Optional을 반환하면 `BookService.findBook()`의 `orElseThrow()`가 `ResponseStatusException(HttpStatus.NOT_FOUND, ...)`을 발생시킵니다.

**Q5. Domain을 응답 DTO로 어떻게 변환하는가?**  
`BookService.toResponse()`가 getter로 값을 읽어 `BookResponse`를 만듭니다. `findAll()`은 `map(this::toResponse)`로 각 객체를 변환합니다.

## ④ 개발 과정 요약

| 단계 | 작성·변경 내용 | 확인 방법 |
|---|---|---|
| 1. 프로젝트 설정 | IntelliJ에서 생성한 프로젝트명·패키지·TripManagerApplication 변경 | IntelliJ 서버 실행 |
| 2. 데이터 설계 | Trip, TripRequest, TripResponse 작성. 요청에서 ID 제외 | 코드의 필드·생성자 확인, 등록 응답 확인 |
| 3. 저장소 구현 | TripRepository와 MemoryTripRepository의 CRUD 메서드 작성 | API 테스트로 저장·조회·수정·삭제 확인 |
| 4. API 구현 | TripService의 CRUD·findTrip()·toResponse(), TripController의 HTTP 매핑 작성 | Postman으로 CRUD·404 확인 |
| 5. 기능 확장 | validate()와 findAll(String destination) 추가 | 정상·비정상 입력과 목적지 조건 비교 |

## ⑤ 기능 수정·확장 및 로컬 테스트

### A. 잘못된 입력 처리

잘못된 값이 저장되지 않도록 `TripService.validate()`를 작성했습니다. `create()`에서 등록 전, `update()`에서 setter 호출 전에 검사합니다. 빈 이름·목적지, 음수·누락 예산, 날짜 누락·역전은 거절합니다.

| 테스트 요청 | 예상 결과 | 실제 결과 |
|---|---|---|
| 정상 POST /api/trips | 등록 성공 | 201 |
| budget을 -100으로 바꾼 POST /api/trips | 등록 거절 | 400 |
| budget을 -100으로 바꾼 PUT /api/trips/2 | 수정 거절, 기존 값 유지 | 400, 후속 GET의 예산 500000 유지 |

### B. 목적지 필터링

원하는 목적지만 조회하도록 `TripController.findAll()`에서 선택적 destination 파라미터를 받고, `TripService.findAll(String destination)`에서 `filter()`와 `equals()`로 비교합니다.

| 테스트 요청 | 예상 결과 | 실제 결과 |
|---|---|---|
| GET /api/trips?destination=README_TEST_JEJU_20261003 | 제주 테스트 여행만 반환 | 200, ID 2 여행만 반환 |
| GET /api/trips?destination=README_TEST_BUSAN_20261003 | 부산 테스트 여행만 반환 | 200, ID 3 여행만 반환 |
| GET /api/trips?destination=README_TEST_NONE_20261003 | 빈 목록 | 200, [] |

2026-10-03에 curl로 등록·조회·수정·삭제, 400·404, 필터링을 재확인했습니다. 수정 시 ID가 유지됐고 삭제는 204, 삭제한 ID의 조회·수정·재삭제는 각각 404였습니다. 임시 여행 2개는 삭제하고 기존 데이터를 보존했습니다.

전체 요청 JSON과 실제 응답은 [로컬 테스트 기록](docs/local-test-results.md)에 있습니다. Dockerfile 작성 전 `./gradlew test`, 작성 후 `./gradlew test bootJar`를 실행해 성공을 확인했습니다. 테스트 1개, 실패·오류 0개입니다.

## ⑥ 배포 과정 요약

Render 배포를 위해 최상위에 `Dockerfile`과 `.dockerignore`를 추가했습니다. Dockerfile은 Java 17 환경에서 `test bootJar`를 실행하고, 생성된 JAR을 Java 17 JRE에서 실행합니다. `application.properties`의 `server.port=${PORT:8082}`는 Render의 PORT를 사용하며 로컬 기본값은 8082입니다. 빌드한 JAR을 `PORT=18082`로 실행하고 `GET /api/trips`의 200 및 빈 목록 응답을 확인했습니다.

Render에서 package.json을 찾는 오류가 발생해 Docker 실행 환경을 선택했습니다. 배포 설정은 Branch `main`, Root Directory 빈 값, Dockerfile Path `./Dockerfile`입니다. Docker가 로컬에서 실행 중이지 않아 컨테이너 빌드는 아직 확인하지 못했습니다.

빌드 및 배포 순서는 다음과 같습니다.

1. `./gradlew test bootJar`로 테스트와 실행 JAR 빌드를 확인했습니다. 검토 시 `--rerun-tasks`를 추가해 재실행한 결과도 `BUILD SUCCESSFUL`이었으며, 기본 테스트 1개의 실패·오류는 0개였습니다.
2. Java 17 빌드 단계와 JRE 실행 단계를 사용하는 `Dockerfile`, 빌드 컨텍스트에서 불필요한 파일을 제외하는 `.dockerignore`, Render의 `PORT`를 읽는 `application.properties` 설정을 준비했습니다.
3. 배포 설정과 README를 포함한 main 커밋 `432f6f4`가 개인 저장소와 Organization 저장소 양쪽에 올라간 것을 확인했습니다. 이 시점의 작업 커밋은 10개입니다.
4. Render에서 Docker 실행 환경으로 배포했습니다. 설정은 Branch `main`, Root Directory 빈 값, Dockerfile Path `./Dockerfile`입니다. 실제 배포 주소는 https://week05-webservice-1.onrender.com/ 입니다.
5. 2026-10-03에 AI 도구가 curl로 외부 URL에서 GET·POST, 입력 검증과 목적지 필터링을 테스트했습니다. 11건의 요청이 예상 상태와 일치했습니다. 임시 여행만 삭제하고 테스트 전후 기존 여행 목록이 같음을 확인했습니다.

| 외부 URL 테스트 | 예상 결과 | 실제 결과 |
|---|---|---|
| GET /api/trips | 저장된 여행 목록 조회 | 200, 기존 ID 1 여행 반환 |
| POST /api/trips — 정상 여행 JSON | ID 자동 생성과 등록 | 201, ID 2와 입력한 6개 필드 반환 |
| GET /api/trips — 등록 후 | 등록한 여행 포함 | 200, 기존 ID 1과 테스트 ID 2 반환 |
| GET /api/trips?destination=DEPLOY_AUDIT_20261003_220421_JEJU | 테스트 목적지 여행만 반환 | 200, ID 2만 반환 |
| GET /api/trips?destination=DEPLOY_AUDIT_20261003_220421_NONE | 일치하는 여행 없음 | 200, `[]` |
| POST /api/trips — budget -100 | 잘못된 등록 거절 | 400 Bad Request |
| PUT /api/trips/2 — budget -100 | 잘못된 수정 거절 | 400, 후속 GET에서 기존 budget 300000 유지 |
| DELETE /api/trips/2, 이후 GET /api/trips/2 | 삭제 후 조회 실패 | 204, 이후 404 Not Found |

예를 들어 아래 요청은 목적지 필터 기능의 실제 배포 확인에 사용했습니다.

```bash
curl 'https://week05-webservice-1.onrender.com/api/trips?destination=DEPLOY_AUDIT_20261003_220421_JEJU'
```

실제 응답은 200이었고, 본문은 다음과 같았습니다.

```json
[
  {
    "id": 2,
    "title": "배포 확인 여행",
    "destination": "DEPLOY_AUDIT_20261003_220421_JEJU",
    "startDate": "2026-11-01",
    "endDate": "2026-11-03",
    "budget": 300000,
    "memo": "외부 URL 테스트용 임시 데이터"
  }
]
```

전체 외부 요청 URL, POST·PUT 요청 JSON, 실제 응답 본문은 [배포 테스트 기록](docs/deployment-test-results.md)에 정리했습니다. 이 결과는 해당 시점의 기록이며 테스트 ID 2는 삭제했습니다. 메모리 저장소이므로 서버 재시작 후 데이터와 ID 순서는 달라질 수 있습니다. Render 내부 빌드 로그는 이번 확인에 포함하지 않았고, 외부 API 동작으로 배포 서버의 실행을 확인했습니다.

## ⑦ Weekly Report

**Key Learning**

1. Controller는 HTTP 처리, Service는 검증·DTO 변환, Repository는 저장을 담당합니다.
2. Request DTO에는 입력을 담고, Response DTO에는 서버가 생성한 ID도 포함합니다.
3. Map.remove()는 없는 ID에 예외를 발생시키지 않으므로 삭제 전 존재 여부 확인이 필요합니다.

**Problem & Solution:** 수정 시 ID가 바뀌었습니다. `TripService.update()`에서 ID를 새로 생성하는 `repository.save()`를 호출한 것이 원인이었습니다. `repository.update()`로 변경하고 PUT·GET에서 ID 유지를 확인했습니다.

**Code Review:** `TripService.update()`는 `findTrip(id)`로 존재 여부를 확인하고 `validate(request)`로 검증합니다. 성공하면 setter로 내용을 바꾸고 `repository.update()`의 결과를 `toResponse()`로 변환합니다. 검증을 setter보다 먼저 수행해 잘못된 값이 기존 객체에 반영되지 않도록 했습니다.

**AI Usage:** 요청 흐름, DTO, 생성자 주입, LinkedHashMap, 수정·삭제 동작을 AI에 질문했습니다. 제안 코드를 IntelliJ에서 작성하고 Postman으로 확인했습니다. 수정 시 save() 사용 오류와 검증 호출 누락을 확인·수정했습니다. AI가 README를 작성하고 curl로 실제 응답을 기록했습니다. 제출 기준 검토에서는 빌드와 테스트를 재실행하고 별도 로컬 서버에 HTTP 요청 47건을 보내 CRUD, 400·404, 필터링을 확인했습니다. 배포 URL 제공 후에는 AI가 외부 요청 11건의 실제 결과를 문서화했으며, Reflection과 건의사항을 포함한 최종 보고서 작성에도 활용했습니다.

**Reflection:** 메모리 저장소로 CRUD를 구현하면서 서버가 재시작되면 데이터가 사라진다는 한계를 이해했습니다. 다음에는 데이터베이스와 JPA를 사용해 Repository 구현을 바꾸고, Controller와 Service를 어느 정도 유지할 수 있는지 확인하고 싶습니다. 또한 현재 검증 메서드를 Bean Validation으로 표현하는 방법과, 수동으로 확인한 400·404 및 수정 시 ID 유지 동작을 자동 API 테스트로 검증하는 방법을 더 공부하고 싶습니다.

**건의사항:** 배포 실습 자료에 Docker 실행 환경 선택, Root Directory, Dockerfile Path, PORT 설정을 한 번에 확인할 수 있는 예시가 있으면 좋겠습니다. 특히 화면이 없는 REST API는 루트 경로가 404여도 `/api/trips`에서 정상 응답할 수 있다는 점을 안내하면 배포 성공 여부를 판단하는 데 도움이 될 것 같습니다.
