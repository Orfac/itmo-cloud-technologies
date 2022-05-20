FROM openjdk:17-alpine
VOLUME /tmp

RUN file="$(ls -1 )" && echo $file
COPY build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
