FROM openjdk:17
EXPOSE 8080
ADD target/writerapp.jar writerapp.jar
ENTRYPOINT ["java","-jar","/writerapp.jar"]