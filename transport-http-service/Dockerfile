FROM openjdk:11

USER root
ADD ./build/libs/transport-http-service.jar transport-http-service.jar

ENTRYPOINT ["java","-jar","transport-http-service.jar"]

