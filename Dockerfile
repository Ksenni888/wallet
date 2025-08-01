FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar wallet.jar
ENTRYPOINT ["java","-jar","/wallet.jar"]