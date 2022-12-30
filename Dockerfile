FROM openjdk:17-alpine
EXPOSE 8080
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*
ADD target/binar-air-rest-api-0.0.1-SNAPSHOT.jar binar-air-rest-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "binar-air-rest-api-0.0.1-SNAPSHOT.jar"]