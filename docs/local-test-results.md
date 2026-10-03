# 로컬 API 실제 테스트 결과

테스트 날짜: 2026-10-03 (Asia/Seoul)

IntelliJ에서 실행 중인 `http://localhost:8082` 서버에 AI 도구가 curl로 요청한 실제 결과입니다. 학생이 앞서 수행한 Postman 테스트와 구분해 기록합니다. 테스트용 목적지를 별도로 사용했고, 생성한 두 여행은 마지막에 삭제했습니다. 테스트 전후 기존 ID 1의 여행이 보존되었습니다.

## 1. 정상 등록

```http
POST http://localhost:8082/api/trips
```

요청 JSON:

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

실제 상태 코드: **201**

실제 응답:

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

## 2. 전체 조회

```http
GET http://localhost:8082/api/trips
```

실제 상태 코드: **200**

실제 응답:

```json
[
  {
    "id": 1,
    "title": "제주1",
    "destination": "제주",
    "startDate": "2026-11-01",
    "endDate": "2026-11-04",
    "budget": 500000,
    "memo": "숙소 예약 완료"
  },
  {
    "id": 2,
    "title": "README 테스트 여행",
    "destination": "README_TEST_JEJU_20261003",
    "startDate": "2026-11-01",
    "endDate": "2026-11-03",
    "budget": 300000,
    "memo": "문서 작성용 임시 데이터"
  }
]
```

## 3. 단건 조회

```http
GET http://localhost:8082/api/trips/2
```

실제 상태 코드: **200**

실제 응답:

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

## 4. 정상 수정

```http
PUT http://localhost:8082/api/trips/2
```

요청 JSON:

```json
{
  "title": "README 수정 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 500000,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 상태 코드: **200**

실제 응답:

```json
{
  "id": 2,
  "title": "README 수정 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 500000,
  "memo": "문서 작성용 임시 데이터"
}
```

## 5. 수정 결과 조회

```http
GET http://localhost:8082/api/trips/2
```

실제 상태 코드: **200**

실제 응답:

```json
{
  "id": 2,
  "title": "README 수정 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 500000,
  "memo": "문서 작성용 임시 데이터"
}
```

## 6. 음수 예산 등록 거절

```http
POST http://localhost:8082/api/trips
```

요청 JSON:

```json
{
  "title": "README 테스트 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": -100,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 상태 코드: **400**

실제 응답:

```json
{
  "timestamp": "2026-10-03T12:42:40.050Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/trips"
}
```

## 7. 음수 예산 수정 거절

```http
PUT http://localhost:8082/api/trips/2
```

요청 JSON:

```json
{
  "title": "README 수정 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": -100,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 상태 코드: **400**

실제 응답:

```json
{
  "timestamp": "2026-10-03T12:42:40.169Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/trips/2"
}
```

## 8. 잘못된 수정 후 기존 값 확인

```http
GET http://localhost:8082/api/trips/2
```

실제 상태 코드: **200**

실제 응답:

```json
{
  "id": 2,
  "title": "README 수정 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 500000,
  "memo": "문서 작성용 임시 데이터"
}
```

## 9. 빈 여행 이름

```http
POST http://localhost:8082/api/trips
```

요청 JSON:

```json
{
  "title": "   ",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 300000,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 상태 코드: **400**

실제 응답:

```json
{
  "timestamp": "2026-10-03T12:42:40.401Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/trips"
}
```

## 10. 빈 목적지

```http
POST http://localhost:8082/api/trips
```

요청 JSON:

```json
{
  "title": "README 테스트 여행",
  "destination": "",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 300000,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 상태 코드: **400**

실제 응답:

```json
{
  "timestamp": "2026-10-03T12:42:40.517Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/trips"
}
```

## 11. 예산 누락

```http
POST http://localhost:8082/api/trips
```

요청 JSON:

```json
{
  "title": "README 테스트 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": null,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 상태 코드: **400**

실제 응답:

```json
{
  "timestamp": "2026-10-03T12:42:40.635Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/trips"
}
```

## 12. 날짜 누락

```http
POST http://localhost:8082/api/trips
```

요청 JSON:

```json
{
  "title": "README 테스트 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": null,
  "endDate": "2026-11-03",
  "budget": 300000,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 상태 코드: **400**

실제 응답:

```json
{
  "timestamp": "2026-10-03T12:42:40.754Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/trips"
}
```

## 13. 날짜 역전

```http
POST http://localhost:8082/api/trips
```

요청 JSON:

```json
{
  "title": "README 테스트 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-10-01",
  "budget": 300000,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 상태 코드: **400**

실제 응답:

```json
{
  "timestamp": "2026-10-03T12:42:40.875Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/trips"
}
```

## 14. 다른 목적지 등록

```http
POST http://localhost:8082/api/trips
```

요청 JSON:

```json
{
  "title": "README 부산 여행",
  "destination": "README_TEST_BUSAN_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 300000,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 상태 코드: **201**

실제 응답:

```json
{
  "id": 3,
  "title": "README 부산 여행",
  "destination": "README_TEST_BUSAN_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 300000,
  "memo": "문서 작성용 임시 데이터"
}
```

## 15. 일치 목적지 조회

```http
GET http://localhost:8082/api/trips?destination=README_TEST_JEJU_20261003
```

실제 상태 코드: **200**

실제 응답:

```json
[
  {
    "id": 2,
    "title": "README 수정 여행",
    "destination": "README_TEST_JEJU_20261003",
    "startDate": "2026-11-01",
    "endDate": "2026-11-03",
    "budget": 500000,
    "memo": "문서 작성용 임시 데이터"
  }
]
```

## 16. 다른 목적지 조회

```http
GET http://localhost:8082/api/trips?destination=README_TEST_BUSAN_20261003
```

실제 상태 코드: **200**

실제 응답:

```json
[
  {
    "id": 3,
    "title": "README 부산 여행",
    "destination": "README_TEST_BUSAN_20261003",
    "startDate": "2026-11-01",
    "endDate": "2026-11-03",
    "budget": 300000,
    "memo": "문서 작성용 임시 데이터"
  }
]
```

## 17. 없는 목적지 조회

```http
GET http://localhost:8082/api/trips?destination=README_TEST_NONE_20261003
```

실제 상태 코드: **200**

실제 응답:

```json
[]
```

## 18. 여행 삭제

```http
DELETE http://localhost:8082/api/trips/2
```

실제 상태 코드: **204**

실제 응답 본문: 없음.

## 19. 삭제 후 단건 조회

```http
GET http://localhost:8082/api/trips/2
```

실제 상태 코드: **404**

실제 응답:

```json
{
  "timestamp": "2026-10-03T12:42:41.567Z",
  "status": 404,
  "error": "Not Found",
  "path": "/api/trips/2"
}
```

## 20. 삭제 후 수정

```http
PUT http://localhost:8082/api/trips/2
```

요청 JSON:

```json
{
  "title": "README 수정 여행",
  "destination": "README_TEST_JEJU_20261003",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 500000,
  "memo": "문서 작성용 임시 데이터"
}
```

실제 상태 코드: **404**

실제 응답:

```json
{
  "timestamp": "2026-10-03T12:42:41.679Z",
  "status": 404,
  "error": "Not Found",
  "path": "/api/trips/2"
}
```

## 21. 삭제 후 재삭제

```http
DELETE http://localhost:8082/api/trips/2
```

실제 상태 코드: **404**

실제 응답:

```json
{
  "timestamp": "2026-10-03T12:42:41.789Z",
  "status": 404,
  "error": "Not Found",
  "path": "/api/trips/2"
}
```

## 22. 임시 여행 정리

```http
DELETE http://localhost:8082/api/trips/3
```

실제 상태 코드: **204**

실제 응답 본문: 없음.

## 23. 기존 데이터 보존 확인

```http
GET http://localhost:8082/api/trips
```

실제 상태 코드: **200**

실제 응답:

```json
[
  {
    "id": 1,
    "title": "제주1",
    "destination": "제주",
    "startDate": "2026-11-01",
    "endDate": "2026-11-04",
    "budget": 500000,
    "memo": "숙소 예약 완료"
  }
]
```


