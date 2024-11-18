#!/bin/bash

# Set the path where your JAR files are located
ROOT_PATH="/home/ubuntu/libs"  # Adjust to the correct directory
JAR="$ROOT_PATH/application.jar"

APP_LOG="$ROOT_PATH/application.log"
ERROR_LOG="$ROOT_PATH/error.log"
START_LOG="$ROOT_PATH/start.log"

NOW=$(date +%c)

# Log the JAR copy action
echo "[$NOW] $JAR 복사" >> $START_LOG

# Adjusted copy path to the build directory, assuming your JAR is in /libs/
cp $ROOT_PATH/spring-github-action-1.0.0.jar $JAR

# Log the JAR execution
echo "[$NOW] > $JAR 실행" >> $START_LOG

# Run the application in the background
nohup java -jar $JAR > $APP_LOG 2> $ERROR_LOG &

# Get the service PID
SERVICE_PID=$(pgrep -f $JAR)

# Log the service PID
echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG
