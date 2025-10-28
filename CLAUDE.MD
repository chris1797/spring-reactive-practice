# Spring Reactive Practice

Spring WebFlux와 R2DBC를 사용한 리액티브 프로그래밍 실습 프로젝트입니다.

## 기술 스택

### Core
- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.5.7
- **Java Version**: 21

### Reactive Stack
- **Web**: Spring WebFlux (Netty)
- **Data Access**: Spring Data R2DBC
- **Database Driver**: R2DBC PostgreSQL
- **Reactive Library**: Project Reactor

### Database
- **Database**: PostgreSQL
- **Migration**: Flyway
- **Schema**: payment-order

### Others
- **Messaging**: Spring Kafka
- **Build Tool**: Gradle 8.14.3

## 프로젝트 구조

```
src/main/kotlin/com/reactive/core/
├── SpringReactivePracticeApplication.kt
├── controller/
│   └── UserController.kt
├── service/
│   └── UserService.kt
├── repository/
│   └── UserRepository.kt
└── domain/
    ├── entity/
    │   ├── User.kt
    │   ├── Product.kt
    │   ├── Order.kt
    │   ├── Payment.kt
    │   ├── Item.kt
    │   └── Board.kt
    └── enums/
        ├── UserStatus.kt
        ├── OrderStatus.kt
        ├── PaymentStatus.kt
        ├── PaymentMethod.kt
        └── ProductStatus.kt
```

## 데이터베이스 구조

### 연결 정보
- **Host**: localhost:5432
- **Database**: dev
- **Schema**: payment-order
- **Username**: ${USER}
- **Password**: (empty)

### 테이블 구조

#### users
사용자 정보를 저장하는 테이블
- `id` (BIGINT, PK): 사용자 ID
- `user_name` (VARCHAR): 사용자 이름
- `address` (VARCHAR): 주소
- `status` (VARCHAR): 상태 (ACTIVE, INACTIVE)
- `created_at` (TIMESTAMP): 생성일시
- `updated_at` (TIMESTAMP): 수정일시

#### products
상품 정보를 저장하는 테이블
- `id` (BIGINT, PK): 상품 ID
- `name` (VARCHAR): 상품명
- `price` (NUMERIC): 가격
- `status` (SMALLINT): 상태 (0: INACTIVE, 1: ACTIVE)
- `created_at` (TIMESTAMP): 생성일시
- `updated_at` (TIMESTAMP): 수정일시

#### orders
주문 정보를 저장하는 테이블
- `id` (BIGINT, PK): 주문 ID
- `user_id` (BIGINT, FK): 사용자 ID
- `product_name` (VARCHAR): 상품명
- `quantity` (INT): 수량
- `total_amount` (NUMERIC): 총 금액
- `address` (VARCHAR): 배송 주소
- `status` (VARCHAR): 주문 상태 (PENDING, COMPLETED, CANCELLED)
- `created_at` (TIMESTAMP): 생성일시
- `updated_at` (TIMESTAMP): 수정일시
- **Indexes**: user_id, status

#### payments
결제 정보를 저장하는 테이블
- `id` (BIGINT, PK): 결제 ID
- `user_id` (BIGINT, FK): 사용자 ID
- `order_id` (BIGINT, FK, UNIQUE): 주문 ID
- `amount` (NUMERIC): 결제 금액
- `method` (VARCHAR): 결제 수단 (CREDIT_CARD, CASH)
- `status` (VARCHAR): 결제 상태 (INITIATED, APPROVED, DECLINED)
- `tx_id` (VARCHAR, UNIQUE): 거래 ID
- `created_at` (TIMESTAMP): 생성일시
- `updated_at` (TIMESTAMP): 수정일시
- **Indexes**: user_id, order_id, status, tx_id

#### items
아이템 정보를 저장하는 테이블
- `id` (BIGINT, PK): 아이템 ID
- `name` (VARCHAR): 아이템명
- `price` (INT): 가격
- `created_at` (TIMESTAMP): 생성일시
- `updated_at` (TIMESTAMP): 수정일시

#### board
게시판 정보를 저장하는 테이블
- `id` (BIGINT, PK): 게시글 ID
- `title` (VARCHAR): 제목
- `content` (VARCHAR): 내용
- `user_id` (BIGINT): 작성자 ID
- `created_at` (VARCHAR): 생성일시

## API 엔드포인트

### User API

#### 사용자 조회
```http
GET /api/users/{id}
```

**Response Example:**
```json
{
  "id": 7,
  "userName": "홍길동",
  "address": "서울시 강남구",
  "status": "ACTIVE",
  "createdAt": "2025-10-29T00:25:26.774025",
  "updatedAt": "2025-10-29T00:25:26.774025"
}
```

**Status Codes:**
- `200 OK`: 조회 성공
- `200 OK (empty body)`: 사용자를 찾을 수 없음

## 개발 가이드

### 프로젝트 실행

```bash
# 애플리케이션 실행
./gradlew bootRun

# 애플리케이션은 http://localhost:8080 에서 실행됩니다
```

### API 테스트

```bash
# 사용자 조회
curl http://localhost:8080/api/users/7

# JSON 포맷으로 보기
curl -s http://localhost:8080/api/users/7 | python3 -m json.tool
```

### 데이터베이스 접속

```bash
# PostgreSQL 접속
psql -h localhost -p 5432 -d dev

# payment-order 스키마의 테이블 확인
\dt "payment-order".*

# users 테이블 조회
SELECT * FROM "payment-order".users;
```

## 리액티브 프로그래밍 개념

### Mono vs Flux

- **Mono<T>**: 0개 또는 1개의 요소를 비동기로 반환
  - 예: 단일 사용자 조회 → `Mono<User>`

- **Flux<T>**: 0개 이상의 요소를 비동기로 반환
  - 예: 사용자 목록 조회 → `Flux<User>`

### 논블로킹 I/O

WebFlux는 Netty를 사용하여 논블로킹 방식으로 요청을 처리합니다:

1. 요청이 들어오면 작업을 시작하고 즉시 반환
2. 데이터베이스 조회가 완료되면 콜백으로 응답
3. 스레드가 블로킹되지 않아 더 많은 요청을 동시에 처리 가능

### R2DBC

R2DBC(Reactive Relational Database Connectivity)는 리액티브 스트림을 지원하는 데이터베이스 드라이버입니다:

- **JDBC**: 블로킹 방식 (전통적인 Spring Data JPA)
- **R2DBC**: 논블로킹 방식 (Spring Data R2DBC)

## 아키텍처

### 레이어 구조

```
Controller (REST API)
    ↓
Service (비즈니스 로직)
    ↓
Repository (데이터 접근)
    ↓
R2DBC (리액티브 DB 드라이버)
    ↓
PostgreSQL Database
```

### 의존성 주입

Kotlin에서는 생성자 주입을 사용하며, `private val`로 선언합니다:

```kotlin
@Service
class UserService(
    private val userRepository: UserRepository
) {
    // ...
}
```

## 주의사항

### Entity 클래스

- **JPA의 `@Entity`를 사용하지 않습니다**
- R2DBC는 `@Table` 어노테이션만 사용
- `org.springframework.data.relational.core.mapping.Table` 패키지

### Flyway

- 기존 스키마는 다른 프로젝트에서 관리됨
- 현재 프로젝트에서는 스키마 변경 시에만 마이그레이션 파일 추가
- 버전은 V2부터 시작 (V1은 이미 실행됨)

### 스키마 네이밍

- 스키마명에 하이픈(`-`)이 포함되어 있어 SQL에서 큰따옴표 필요
- 예: `"payment-order".users`

## 다음 단계

### 구현 예정 기능

- [ ] 전체 사용자 목록 조회 API (GET /api/users)
- [ ] 사용자 생성 API (POST /api/users)
- [ ] 사용자 수정 API (PUT /api/users/{id})
- [ ] 사용자 삭제 API (DELETE /api/users/{id})
- [ ] 404 에러 핸들링 개선
- [ ] Product, Order, Payment API 구현
- [ ] 예외 처리 및 에러 응답 표준화
- [ ] 통합 테스트 작성
- [ ] API 문서화 (Swagger/OpenAPI)

### 학습 주제

- [ ] Reactive Streams 심화
- [ ] Backpressure 개념
- [ ] WebFlux 테스트 작성법
- [ ] R2DBC 트랜잭션 관리
- [ ] Reactor의 다양한 오퍼레이터 활용

## 참고 자료

- [Spring WebFlux 공식 문서](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Spring Data R2DBC 공식 문서](https://docs.spring.io/spring-data/r2dbc/reference/)
- [Project Reactor 공식 문서](https://projectreactor.io/docs/core/release/reference/)
- [R2DBC 공식 문서](https://r2dbc.io/)

## 문의 및 이슈

프로젝트 관련 질문이나 이슈가 있다면 팀원과 논의하거나 관련 문서를 참고하세요.