# 🎣 굴비잇기 API 서버

## 스펙 주도 개발

이 프로젝트는 OAS(Open API Spec) 3.0.3 스펙을 먼저 작성하고 [Swagger
Codegen](https://github.com/swagger-api/swagger-codegen)을 이용하여 API 및
데이터 모델 코드를 자동 생성하여 개발하는 방식을 채택했습니다.

스펙의 변화가 있을 경우 `src/main/resources/api/openapi.yaml`를 수정한 뒤
프로젝트를 빌드하면 API 코드가 자동생성됩니다.

OAS 3.0.3 스펙 정보는 [여기](https://spec.openapis.org/oas/v3.0.3.html#schema-object)에서 확인할 수 있습니다.
