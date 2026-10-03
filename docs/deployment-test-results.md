# 배포 API 실제 테스트 결과

테스트 날짜: 2026-10-03T22:04:25+09:00

배포 URL: https://week05-webservice-1.onrender.com/

AI 도구가 curl로 외부 배포 URL에 요청한 실제 결과입니다. 테스트용 목적지를 구분했고 생성한 임시 데이터만 삭제했습니다. 테스트 전후 기존 목록이 동일함을 확인했습니다. 서버가 재시작되면 메모리 데이터와 ID 순서가 초기화될 수 있습니다.

| 테스트 | Method 및 경로 | 예상 상태 | 실제 상태 |
|---|---|---|---|
| 테스트 전 전체 조회 | GET /api/trips | 200 | 200 |
| 정상 등록 | POST /api/trips | 201 | 201 |
| 등록 후 전체 조회 | GET /api/trips | 200 | 200 |
| 목적지 일치 필터 | GET /api/trips?destination=DEPLOY_AUDIT_20261003_220421_JEJU | 200 | 200 |
| 목적지 불일치 필터 | GET /api/trips?destination=DEPLOY_AUDIT_20261003_220421_NONE | 200 | 200 |
| 음수 예산 등록 거절 | POST /api/trips | 400 | 400 |
| 음수 예산 수정 거절 | PUT /api/trips/2 | 400 | 400 |
| 잘못된 수정 후 기존 값 유지 | GET /api/trips/2 | 200 | 200 |
| 생성한 임시 여행 삭제 | DELETE /api/trips/2 | 204 | 204 |
| 삭제한 ID 조회 | GET /api/trips/2 | 404 | 404 |
| 테스트 후 전체 조회 | GET /api/trips | 200 | 200 |

## 1. 테스트 전 전체 조회

```http
GET https://week05-webservice-1.onrender.com/api/trips
```

실제 상태 코드: **200**

실제 응답 JSON:

```json
[
  {
    "id": 1,
    "title": "제주 가족 여행",
    "destination": "제주",
    "startDate": "2026-11-01",
    "endDate": "2026-11-04",
    "budget": 500000,
    "memo": "숙소 예약 완료"
  }
]
```

## 2. 정상 등록

```http
POST https://week05-webservice-1.onrender.com/api/trips
```

요청 JSON:

```json
{
  "title": "배포 확인 여행",
  "destination": "DEPLOY_AUDIT_20261003_220421_JEJU",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 300000,
  "memo": "외부 URL 테스트용 임시 데이터"
}
```

실제 상태 코드: **201**

실제 응답 JSON:

```json
{
  "id": 2,
  "title": "배포 확인 여행",
  "destination": "DEPLOY_AUDIT_20261003_220421_JEJU",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 300000,
  "memo": "외부 URL 테스트용 임시 데이터"
}
```

## 3. 등록 후 전체 조회

```http
GET https://week05-webservice-1.onrender.com/api/trips
```

실제 상태 코드: **200**

실제 응답 JSON:

```json
[
  {
    "id": 1,
    "title": "제주 가족 여행",
    "destination": "제주",
    "startDate": "2026-11-01",
    "endDate": "2026-11-04",
    "budget": 500000,
    "memo": "숙소 예약 완료"
  },
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

## 4. 목적지 일치 필터

```http
GET https://week05-webservice-1.onrender.com/api/trips?destination=DEPLOY_AUDIT_20261003_220421_JEJU
```

실제 상태 코드: **200**

실제 응답 JSON:

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

## 5. 목적지 불일치 필터

```http
GET https://week05-webservice-1.onrender.com/api/trips?destination=DEPLOY_AUDIT_20261003_220421_NONE
```

실제 상태 코드: **200**

실제 응답 JSON:

```json
[]
```

## 6. 음수 예산 등록 거절

```http
POST https://week05-webservice-1.onrender.com/api/trips
```

요청 JSON:

```json
{
  "title": "배포 확인 여행",
  "destination": "DEPLOY_AUDIT_20261003_220421_JEJU",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": -100,
  "memo": "외부 URL 테스트용 임시 데이터"
}
```

실제 상태 코드: **400**

실제 응답 JSON:

```json
{
  "timestamp": "2026-10-03T13:04:23.453Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/trips"
}
```

## 7. 음수 예산 수정 거절

```http
PUT https://week05-webservice-1.onrender.com/api/trips/2
```

요청 JSON:

```json
{
  "title": "배포 확인 여행",
  "destination": "DEPLOY_AUDIT_20261003_220421_JEJU",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": -100,
  "memo": "외부 URL 테스트용 임시 데이터"
}
```

실제 상태 코드: **400**

실제 응답 JSON:

```json
{
  "timestamp": "2026-10-03T13:04:23.778Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/trips/2"
}
```

## 8. 잘못된 수정 후 기존 값 유지

```http
GET https://week05-webservice-1.onrender.com/api/trips/2
```

실제 상태 코드: **200**

실제 응답 JSON:

```json
{
  "id": 2,
  "title": "배포 확인 여행",
  "destination": "DEPLOY_AUDIT_20261003_220421_JEJU",
  "startDate": "2026-11-01",
  "endDate": "2026-11-03",
  "budget": 300000,
  "memo": "외부 URL 테스트용 임시 데이터"
}
```

## 9. 생성한 임시 여행 삭제

```http
DELETE https://week05-webservice-1.onrender.com/api/trips/2
```

실제 상태 코드: **204**

실제 응답 본문: 없음.

## 10. 삭제한 ID 조회

```http
GET https://week05-webservice-1.onrender.com/api/trips/2
```

실제 상태 코드: **404**

실제 응답 JSON:

```json
{
  "timestamp": "2026-10-03T13:04:24.811Z",
  "status": 404,
  "error": "Not Found",
  "path": "/api/trips/2"
}
```

## 11. 테스트 후 전체 조회

```http
GET https://week05-webservice-1.onrender.com/api/trips
```

실제 상태 코드: **200**

실제 응답 JSON:

```json
[
  {
    "id": 1,
    "title": "제주 가족 여행",
    "destination": "제주",
    "startDate": "2026-11-01",
    "endDate": "2026-11-04",
    "budget": 500000,
    "memo": "숙소 예약 완료"
  }
]
```
