FROM openjdk:11

USER root
ADD ./build/libs/transport-service-0.1.0.jar transport-service.jar

ENTRYPOINT ["java","-jar","transport-service.jar"]

