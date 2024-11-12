FROM gradle:8.0.2-jdk17 AS build

WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY src /app/src

# Gradle을 사용하여 애플리케이션 빌드, 테스트는 무시
RUN gradle build -x test

# 실행 단계
FROM openjdk:17-jdk

WORKDIR /app

# 빌드된 JAR 파일을 실행 단계로 복사
COPY --from=build /app/build/libs/*.jar /app/lavanderia.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/lavanderia.jar"]

# 8080 포트로 애플리케이션 노출
EXPOSE 8080