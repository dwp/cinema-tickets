FROM maven:3.8.1-openjdk-17-slim@sha256:a65cb519660f51b06f487a6f5de8c264e1e2bcdb32033f3f45b0899f7740ca0f
USER root

COPY pom.xml /usr/src/app/
COPY src /usr/src/app/src

WORKDIR "/usr/src/app/"

RUN mvn install
