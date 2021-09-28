FROM openjdk:11

USER root
ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
