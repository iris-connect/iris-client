FROM openjdk:11-jdk-slim
ENV SPRING_PROFILES_ACTIVE docker
ARG JAR_FILE
COPY ${JAR_FILE} iris.jar
ENTRYPOINT ["java",\
			"-Djava.security.egd=file:/dev/./urandom",\
			"-jar",\
			"/iris.jar"]