FROM gradle:7.1.1-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon


FROM openjdk:11
ARG VERSION
EXPOSE 8986

COPY --from=build /home/gradle/src/build/libs/config-service-${VERSION}.jar reeco-config-service.jar

COPY entrypoint.sh entrypoint.sh

RUN chmod 500 ./entrypoint.sh

ENTRYPOINT [ "./entrypoint.sh" ]

CMD ["java","-jar","reeco-config-service.jar"]