# 도커가 사용할 기본 이미지
FROM openjdk:17-jdk-slim

# 컨테이너 내부에서 /app이라는 작업 디렉토리를 설정. 이후 명령어들은 이 디렉토리를 기준으로 실행됨.
WORKDIR /app

# war파일을 컨테이너 내부의 /app 디렉토리로 복사
COPY build/libs/test-0.0.1-SNAPSHOT.jar /app/test.jar

# timezone 환경설정
ENV TZ=Asia/Seoul

# Jar 파일 실행
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul","test.jar"]
