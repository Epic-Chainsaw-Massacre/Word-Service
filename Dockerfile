FROM openjdk:11
ADD target/Word-Service.jar Word-Service.jar
ENTRYPOINT ["java", "-jar", "Word-Service.jar"]
