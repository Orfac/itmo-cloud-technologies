FROM tomcat:9-jdk11-openjdk

#COPY out/artifacts/build_war/build_war.war /usr/local/tomcat/webapps/
COPY build/libs/hiring-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/app.war
EXPOSE 8080
CMD ["catalina.sh", "run"]