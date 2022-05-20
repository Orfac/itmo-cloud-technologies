FROM openjdk:17-alpine
VOLUME /tmp
WORKDIR /home/runner/work/itmo-cloud-technologies/itmo-cloud-technologies
RUN file="$(ls -1 /home/runner/work/itmo-cloud-technologies/itmo-cloud-technologies)" && echo $file
COPY build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
