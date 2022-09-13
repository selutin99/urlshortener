FROM openjdk:8
ADD ./target/*.jar /usr/src/urlshortener.jar
WORKDIR usr/src
ENTRYPOINT ["java", "-jar", "urlshortener.jar"]