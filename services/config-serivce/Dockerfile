FROM gradle:6.3.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon


FROM openjdk:11

EXPOSE 8082

COPY --from=build /home/gradle/src/build/libs/*.jar reeco-core-backend.jar

COPY entrypoint.sh entrypoint.sh

RUN chmod 500 ./entrypoint.sh

ENTRYPOINT [ "./entrypoint.sh" ]

CMD ["java","-jar","reeco-core-backend.jar"]