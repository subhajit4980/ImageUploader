FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/ImageUploader.jar ImageUploader.jar
EXPOSE 8080
CMD ["java","-jar","ImageUploader.jar"]