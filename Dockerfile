FROM openjdk:17-alpine
VOLUME /tmp
RUN file="$(ls -1 /tmp/dir)" && echo $file
COPY build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
