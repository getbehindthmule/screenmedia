FROM openjdk:8-jdk-alpine AS builder
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR build/docker
ARG APPJAR=build/libs/greenhills-fuel-manager-0.1.0.jar
COPY ${APPJAR} app.jar
RUN jar -xf ./app.jar

FROM openjdk:8-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=build/docker
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.greenhills.fuel.Application"]