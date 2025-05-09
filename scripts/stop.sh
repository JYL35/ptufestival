#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/ptufestival"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date +%c)

echo "$TIME_NOW > 실행 중인 애플리케이션 종료 시도" >> $DEPLOY_LOG

CURRENT_PID=$(pgrep -f "$JAR_FILE")
if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 실행 중인 애플리케이션 없음" >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 실행 중인 PID $CURRENT_PID 종료" >> $DEPLOY_LOG
  kill -15 "$CURRENT_PID"
  sleep 3

  if ps -p "$CURRENT_PID" > /dev/null; then
    echo "$TIME_NOW > 정상 종료 실패, 강제 종료 시도" >> $DEPLOY_LOG
    kill -9 "$CURRENT_PID"
  fi
fi
