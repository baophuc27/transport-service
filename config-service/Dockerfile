FROM openjdk:11-jre-slim

USER root
EXPOSE 8986
ADD ./build/libs/config-service.jar config-service.jar

ENTRYPOINT ["java","-jar","config-service.jar"]

