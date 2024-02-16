FROM openjdk:17-ea-33-slim

ENV JAVA_OPTS " -Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

WORKDIR application

COPY ./target/spring6-reactive-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java", "-jar", "spring6-reactive-0.0.1-SNAPSHOT.jar"]