#!/bin/bash

echo "📦 Generating application.properties from .env..."

# .env 파일 불러오기
set -a
source .env
set +a

mkdir -p src/main/resources

cat <<EOF > src/main/resources/application.properties
spring.application.name=ptufestival

# SSL 설정 추가
server.port=$SERVER_PORT
server.ssl.enabled=$SERVER_SSL_ENABLED
server.ssl.key-store-type=$SERVER_SSL_KEY_STORE_TYPE
server.ssl.key-store=$SERVER_SSL_KEY_STORE
server.ssl.key-store-password=$SERVER_SSL_KEY_STORE_PASSWORD
server.ssl.key-alias=$SERVER_SSL_KEY_ALIAS

spring.datasource.url=$SPRING_DATASOURCE_URL
spring.datasource.username=$SPRING_DATASOURCE_USERNAME
spring.datasource.password=$SPRING_DATASOURCE_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

jwt.secret=$JWT_SECRET
jwt.expiration=$JWT_EXPIRATION

springdoc.swagger-ui.path=$SPRINGDOC_SWAGGER_UI_PATH
springdoc.api-docs.path=$SPRINGDOC_API_DOCS_PATH
springdoc.default-consumes-media-type=application/json
springdoc.default-produces-media-type=application/json
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.disable-swagger-default-url=true
EOF

echo "✅ application.properties 생성 완료!"
