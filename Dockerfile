# Java 17: проект собран под Java 17 (java.version=17 в pom.xml).
# Образ openjdk:8-jdk-alpine из задания не подойдёт: Java 8 не запустит классы, собранные под Java 17.
FROM eclipse-temurin:17-jre-alpine

# Параметры образа. По умолчанию получается dev-образ.
#   docker build -t devapp .
#   docker build -t prodapp --build-arg APP_PORT=8081 --build-arg PROFILE_DEV=false .
ARG APP_PORT=8080
ARG PROFILE_DEV=true

# Переменные окружения сильнее application.properties внутри jar,
# поэтому один и тот же jar работает и как dev, и как prod, и пересобирать его между образами не нужно.
ENV SERVER_PORT=${APP_PORT}
ENV NETOLOGY_PROFILE_DEV=${PROFILE_DEV}

EXPOSE ${APP_PORT}

ADD target/springBootDemo-0.0.1-SNAPSHOT.jar myapp.jar
ENTRYPOINT ["java","-jar","/myapp.jar"]
