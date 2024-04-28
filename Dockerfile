FROM openjdk:17
EXPOSE 8080
ADD target/writerapp.jar writerapp.jar
ENV API_KEY_FILE=/run/secrets/open_ai_api_key
ENTRYPOINT ["sh", "-c", "export API_KEY=$(cat $API_KEY_FILE) && java -jar /writerapp.jar"]