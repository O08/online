FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY /opt/jenkins_data/workspace/online/target/app.jar /opt/jenkins_volume/app/app.jar
ENTRYPOINT ["java","-jar","/opt/jenkins_volume/app/app.jar","&"]
