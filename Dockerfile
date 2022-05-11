FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/*.jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","/opt/app/app.jar","&"]