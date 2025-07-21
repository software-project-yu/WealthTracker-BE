FROM openjdk:11-jdk
ARG JAR_FILE="build/libs/adventCalendar-0.0.1-SNAPSHOT.jar"
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","-javaagent:./pinpoint/pinpoint-bootstrap-2.2.2.jar", "-Dpinpoint.agentId=adventcalendarDev","-Dpinpoint.applicationName=adventcalendar","-Dpinpoint.config=./pinpoint/pinpoint-root.config","-Dspring.profiles.active=${SERVER_MODE}","-Duser.timezone=Asia/Seoul", "/app.jar"]