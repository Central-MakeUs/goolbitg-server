# 🎣 굴비잇기 API 서버

## 스펙 주도 개발

이 프로젝트는 OAS(Open API Spec) 3.0.3 스펙을 먼저 작성하고 [Swagger
Codegen](https://github.com/swagger-api/swagger-codegen)을 이용하여 API 및
데이터 모델 코드를 자동 생성하여 개발하는 방식을 채택했습니다.

스펙의 변화가 있을 경우 `src/main/resources/api/openapi.yaml`를 수정한 뒤
프로젝트를 빌드하면 API 코드가 자동생성됩니다.

OAS 3.0.3 스펙 정보는 [여기](https://spec.openapis.org/oas/v3.0.3.html#schema-object)에서 확인할 수 있습니다.

## 개발 서버 운영방식

`dev` 브랜치에 개발중인 기능이 적용된 코드를 유지하고, 이를 개발 서버에
배포합니다.

아직 구현이 완료되지 않은 End-Point들은 `501` 상태코드와 함께 예시 응답이
제공됩니다. 이 경우 요청을 변경하여도 항상 같은 응답을 받게 됩니다.

개발 서버에서 충분히 안정성 검증이 완료된 경우 `main` 브랜치에 변경사항이
적용되며, 운영 서버에 배포됩니다.

## 브랜치 전략

개발에 사용되는 주요 브랜치는 다음과 같습니다.

- `main`: 운영 서버에 배포될 코드들을 저장합니다.
- `dev`: 개발 서버에 배포될 코드들을 저장합니다.
- `spec/<version>`: 특정 버전의 스펙을 정의합니다.
- `feat/<feat-name>`: 특정 기능을 개발합니다.
