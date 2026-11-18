# P-Dong Backend Repository

## 프로젝트 소개

<p align="center">
  <img width="2478" height="1859" alt="project-image" src="https://github.com/user-attachments/assets/e8c742fc-283e-4be3-a4ba-8a14e22f9b77" />
</p>

P-Dong(피동)은 평택대학교 2025년 축제 안내 웹서비스입니다. 방문자는 모바일에서 공연 일정, 부스 위치와 상세 정보, 운영진 공지를 한곳에서 확인할 수 있습니다. 이 저장소에는 Spring Boot 백엔드 코드가 있습니다.

기획부터 배포·운영까지의 과정과 실제 사용 지표를 회고에 정리했습니다. [피동 개발 및 운영 후기 (Velog)](https://velog.io/@dlwodbs1223/project-pdong-review)

## 팀원 및 기여

**4인 팀** · 백엔드 2명, 프론트엔드 2명 · 2025년 3월~5월

<table width="100%">
  <tr>
    <th width="229" align="center"><a href="https://github.com/JYL35"><img src="https://avatars.githubusercontent.com/u/154980427?v=4" width="64" alt="이재윤 GitHub 프로필 사진" /><br>이재윤</a></th>
    <th width="229" align="center"><a href="https://github.com/kimin3"><img src="https://avatars.githubusercontent.com/u/123732797?v=4" width="64" alt="김민서 GitHub 프로필 사진" /><br>김민서</a></th>
    <th width="229" align="center"><a href="https://github.com/gbwlxhd97"><img src="https://avatars.githubusercontent.com/u/49021925?v=4" width="64" alt="이지원 GitHub 프로필 사진" /><br>이지원</a></th>
    <th width="229" align="center"><a href="https://github.com/Lee-chaerin"><img src="https://avatars.githubusercontent.com/u/81899084?v=4" width="64" alt="이채린 GitHub 프로필 사진" /><br>이채린</a></th>
  </tr>
  <tr>
    <td align="center">팀장 · 백엔드</td>
    <td align="center">백엔드</td>
    <td align="center">프론트엔드</td>
    <td align="center">프론트엔드</td>
  </tr>
</table>

## 주요 기능

- **공연 타임테이블** — 날짜별 축제 일정과 현재·다음 공연을 확인합니다.
- **부스 배치도** — 지도에서 유형별로 부스를 찾고 상세 정보와 판매 품목을 확인합니다.
- **축제 공지사항** — 운영진이 게시한 공지의 목록과 상세 내용을 확인합니다.
- **실시간 공지** — 운영진이 보낸 긴급 공지를 모바일 브라우저에서 SSE(Server-Sent Events)로 받습니다.

### SSE를 선택한 이유와 재연결 처리

축제 운영진은 사이트를 보고 있는 방문자에게 바로 공지를 전달하고 싶어 했습니다. 공지가 서버에서 브라우저로 전달되는 단방향 흐름이어서 SSE를 선택했습니다. 재연결로 연결이 중복되지 않도록 프론트엔드 팀과 클라이언트 ID를 재사용하기로 했습니다. [백엔드는 같은 ID로 연결 요청이 오면 기존 연결을 닫고 새 연결로 교체합니다.](src/main/java/com/capstone7/ptufestival/notification/service/NotificationService.java)

## 기술 스택

- **백엔드** — Java 17, Spring Boot 3.4, Spring Data JPA, Spring Security, SSE
- **데이터베이스** — MySQL, Amazon RDS
- **배포** — GitHub Actions, Amazon S3, AWS CodeDeploy, Amazon EC2
- **API 문서** — springdoc-openapi, Swagger UI

## API 명세

[OpenAPI 설정](src/main/java/com/capstone7/ptufestival/auth/config/SwaggerConfig.java)에서 API 문서와 Bearer 인증 방식을 정의합니다. Swagger UI와 OpenAPI JSON 경로는 각각 `SPRINGDOC_SWAGGER_UI_PATH`와 `SPRINGDOC_API_DOCS_PATH`로 설정합니다.

| 메서드 | 경로 | 설명 |
| --- | --- | --- |
| `GET` | `/api/timetable/{date}` | 지정한 날짜(`yyyy-MM-dd`)의 축제 일정을 조회합니다. |
| `GET` | `/api/timetable/now-and-next` | 현재 진행 중인 공연과 다음 공연을 조회합니다. |
| `GET` | `/api/booth/{id}` | ID로 부스 정보를 조회합니다. |
| `GET` | `/api/booth/theme/{theme}` | 유형에 해당하는 부스 목록을 조회합니다. |
| `GET` | `/api/booth/detail/{id}` | 부스 상세 정보와 판매 품목을 조회합니다. |
| `GET` | `/api/notice/read` | 공지사항 목록을 조회합니다. |
| `GET` | `/api/notice/read/{id}` | 공지사항 상세 내용을 조회합니다. |
| `GET` | `/api/notification/connect?clientId={clientId}` | `notification` 이벤트를 받기 위한 SSE 연결(`text/event-stream`)을 엽니다. |
| `POST` | `/api/notification/send` | `title`과 `description`을 담은 실시간 공지를 전송합니다. 관리자 전용입니다. |
| `POST` | `/api/notice/create` | `title`과 `content`를 담은 공지사항을 등록합니다. 관리자 전용입니다. |
| `PUT` | `/api/notice/update/{id}` | 공지사항을 수정합니다. 관리자 전용입니다. |
| `DELETE` | `/api/notice/delete/{id}` | 공지사항을 삭제합니다. 관리자 전용입니다. |
| `POST` | `/api/auth/register` | 계정을 등록합니다. |
| `POST` | `/api/auth/login` | 로그인하고 액세스 토큰과 리프레시 토큰을 발급받습니다. |
| `POST` | `/api/auth/refresh` | 새 액세스 토큰을 발급받습니다. |
| `GET` | `/api/user/me` | 로그인한 사용자의 정보를 조회합니다. |

## ERD

![사용자, 리프레시 토큰, 공지사항, 타임테이블, 부스, 상품 테이블을 보여주는 피동 ERD](https://github.com/user-attachments/assets/a2973763-828c-48e3-863f-cb058e2d97d4)

## 시스템 아키텍처

![브라우저, EC2에서 실행되는 Spring Boot, RDS의 MySQL, GitHub Actions 배포 흐름을 보여주는 피동 시스템 아키텍처](https://github.com/user-attachments/assets/520ae904-826d-4abf-b602-bdee4147c15b)

- **서비스 흐름** — 브라우저는 HTTPS로 EC2의 Spring Boot 백엔드에 REST 요청을 보내고 SSE 연결을 맺습니다. 백엔드는 Amazon RDS의 MySQL에서 데이터를 읽고 씁니다.
- **배포 흐름** — [GitHub Actions 워크플로](.github/workflows/deploy.yml)가 애플리케이션을 빌드하고 배포 파일을 S3에 올립니다. AWS CodeDeploy는 해당 파일을 EC2에 배포합니다.
