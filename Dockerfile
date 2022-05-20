FROM openjdk:17-alpine
VOLUME /tmp

RUN file="$(ls -1 )" && echo $file
COPY /home/runner/work/itmo-cloud-technologies/itmo-cloud-technologies/build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
