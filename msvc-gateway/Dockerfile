ARG MSVC_NAME=msvc-gateway
FROM openjdk:17-jdk-alpine AS builder
ARG MSVC_NAME
WORKDIR /app/$MSVC_NAME
COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot-repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline
COPY ./$MSVC_NAME/src ./src
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/msvc-gateway/target/msvc-gateway-0.0.1-SNAPSHOT.jar .
EXPOSE 8090
CMD ["java", "-jar", "msvc-gateway-0.0.1-SNAPSHOT.jar"]