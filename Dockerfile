FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
RUN mkdir deploy
COPY ${JAR_FILE} ./deploy/
RUN cp $(find /deploy/*.jar) app.jar
RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir ./logs
RUN touch ./logs/cms-test.log
RUN chown -R spring:spring ./logs/
RUN chown -R spring:spring ./deploy/*
USER spring:spring
ENTRYPOINT ["java","-jar", "/app.jar"]