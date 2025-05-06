#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/ptufestival"
JAR_SOURCE=$(ls $PROJECT_ROOT/build/libs/*.jar | tail -n 1)
JAR_TARGET="$PROJECT_ROOT/spring-webapp.jar"

APP_LOG="$PROJECT_ROOT/app.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

echo "$TIME_NOW > 기존 Java 프로세스 종료 시도" >> $DEPLOY_LOG
sudo pkill -f "java -jar"
sleep 3

echo "$TIME_NOW > JAR 복사: $JAR_SOURCE -> $JAR_TARGET" >> $DEPLOY_LOG
cp "$JAR_SOURCE" "$JAR_TARGET"

echo "$TIME_NOW > 실행 권한 부여" >> $DEPLOY_LOG
chmod +x "$JAR_TARGET"

echo "$TIME_NOW > 애플리케이션 실행" >> $DEPLOY_LOG
sudo nohup java -Duser.timezone=Asia/Seoul -jar "$JAR_TARGET" > "$APP_LOG" 2> "$ERROR_LOG" &

sleep 5

CURRENT_PID=$(pgrep -f "$JAR_TARGET")
if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 애플리케이션 시작 실패!" >> $DEPLOY_LOG
  tail -n 50 "$ERROR_LOG" >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 실행된 프로세스 PID: $CURRENT_PID" >> $DEPLOY_LOG
fi
