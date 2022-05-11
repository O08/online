FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/*.jar /opt/jenkins_volume/app/app.jar
ENTRYPOINT ["java","-jar","/opt/jenkins_volume/app/app.jar","&"]
