FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/ImageUploader.jar ImageUploader.jar
# Set environment variables
ENV ACCESS_KEY=${secrets.ACCESS_KEY}
ENV SECRET_KEY=${secrets.SECRET_KEY}
EXPOSE 8080
CMD ["java","-jar","ImageUploader.jar"]