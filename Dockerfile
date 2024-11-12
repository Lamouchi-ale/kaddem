FROM openjdk:11-jre-slim
EXPOSE 8083
ADD target/kaddem-0.0.2-SNAPSHOT.jar /kaddem-0.0.2-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/kaddem-0.0.1-SNAPSHOT.jar"]
