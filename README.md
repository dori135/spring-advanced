# ♻️ Spring 코드 개선
## 프로젝트 설명
코드 개선, N+1 문제 해결, 테스트 코드 작성, API 로깅 등을 수행하였습니다.

**Level 4**까지 완료하였습니다.


## 🔨 과제 레벨

### Lv 1. 코드 개선

#### 1. Early Return
패키지 `package org.example.expert.domain.auth.service;` 의 `AuthService` 클래스에 있는 `signup()` 중 아래의 코드 부분의 위치를 리팩토링해서
```java
if (userRepository.existsByEmail(signupRequest.getEmail())) {
    throw new InvalidRequestException("이미 존재하는 이메일입니다.");
}
```
해당 에러가 발생하는 상황일 때, passwordEncoder의 encode() 동작이 불필요하게 일어나지 않게 코드 개선

#### 2. 불필요한 if-else 제거
패키지 `package org.example.expert.client;` 의 `WeatherClient` 클래스에 있는 `getTodayWeather()` 중 아래의 코드 부분을 리팩토링해서
```java
WeatherDto[] weatherArray = responseEntity.getBody();
if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
    throw new ServerException("날씨 데이터를 가져오는데 실패했습니다. 상태 코드: " + responseEntity.getStatusCode());
} else {
    if (weatherArray == null || weatherArray.length == 0) {
        throw new ServerException("날씨 데이터가 없습니다.");
    }
}
```
불필요한 `if-else`를 제거해 가독성과 유지보수력을 높일 수 있게 코드 개선

#### 3. Validation 적용
패키지 `package org.example.expert.domain.user.service;` 의 `UserService` 클래스에 있는 `changePassword()` 중 아래 코드 부분을 리팩토링해서

```java
if (userChangePasswordRequest.getNewPassword().length() < 8 ||
        !userChangePasswordRequest.getNewPassword().matches("ㅅ") ||
        !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
    throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
}
```
해당 API의 요청 DTO에서 처리할 수 있게 개선

---

### Lv 2. N+1 문제 해결

- `getTodos` 메서드에서 발생할 수 있는 N+1 문제를 해결
- 기존 `fetch join` JPQL 기반 `TodoRepository`를 `@EntityGraph` 기반으로 수정

---

### Lv 3. 테스트 코드 연습

#### 1. 예상대로 성공하는지에 대한 케이스
- `PassEncoderTest` 클래스의 `matches_메서드가_정상적으로_동작한다()`
  - 테스트 코드 수정

#### 2. 예상대로 예외처리 하는지에 대한 케이스
- `ManagerServiceTest` 클래스의 `manager_목록_조회_시_Todo가_없다면_예외를_던진다()`
  - 테스트 코드와 테스트 코드 메서드명 수정
- `CommentServiceTest` 클래스의 `comment_등록_중_할일을_찾지_못해_에러가_발생한다()`  
  - 테스트 코드 수정
- `ManagerServiceTest` 클래스의 `todo의_user가_null인_경우_예외가_발생한다()`  
  - 서비스 로직 수정

---
### Lv 4. API 로깅
어드민 사용자만 접근가능한 메서드
- `org.example.expert.domain.comment.controller.CommentAdminController` 클래스의 `deleteComment()`
- `org.example.expert.domain.user.controller.UserAdminController` 클래스의 `changeUserRole()`

#### 1. Interceptor 활용
- 요청 정보(`HttpServletRequest`) 사전 처리
- 어드민 권한 여부 확인 후, 인증되지 않은 사용자의 접근을 차단
- 인증 성공 시, 요청 시각과 URL을 로깅하도록 구현

#### 2. AOP 활용
- `@Around` 어노테이션을 사용하여 어드민 API 메서드 실행 전후에 요청/응답 데이터 로깅
- 로깅 내용:
  - 요청한 사용자의 ID
  - API 요청 시각
  - API 요청 URL
  - 요청 본문(`RequestBody`)
  - 응답 본문(`ResponseBody`)
