# ImageUploader

## Introduction

ImageUploader is a Spring Boot application that allows users to upload images or files to an AWS S3 bucket. It's designed to run on an AWS EC2 instance and can be easily dockerized for deployment. This README provides a brief overview of how to use the application, dockerize it, and set up Continuous Integration/Continuous Deployment (CI/CD) using GitHub Actions.

## Features

- Upload images or files to AWS S3 bucket.
- Easy deployment with Docker.
- Automated CI/CD pipeline using GitHub Actions.

## Requirements

- Java 11 or higher
- Docker
- AWS S3 bucket
- AWS EC2 instance

## Usage

1. **Clone the repository:**

    ```bash
    git clone https://github.com/subhajit4980/ImageUploader.git
    cd ImageUploader
    ```

2. **Configure AWS credentials:**

    Make sure you have AWS credentials configured either through environment variables, AWS CLI, or an IAM role attached to the EC2 instance.

3. **Build the application:**

    ```bash
    ./mvnw package
    ```

4. **Run the application:**

    ```bash
    java -jar target/ImageUploader.jar
    ```

5. **Access the application:**

    Open your web browser and go to [http://localhost:8080](http://localhost:8080).

## Dockerization

To dockerize the application:

1. **Build the Docker image:**

    ```bash
    docker build -t image-uploader .
    ```

2. **Run the Docker container:**

    ```bash
    docker run -p 8080:8080 -e AWS_ACCESS_KEY_ID=<your-access-key> -e AWS_SECRET_KEY=<your-secret-key> image-uploader
    ```

    Replace `<your-access-key>` and `<your-secret-key>` with your AWS access key and secret key.

## CI/CD with GitHub Actions

This repository includes a GitHub Actions workflow to automate CI/CD. The workflow file (`ci-cd.yml`) is configured to:

- Build the application.
- Run tests.
- Build the Docker image.
- Push the Docker image to a container registry (e.g., Docker Hub).
- Deploy the application to an AWS EC2 instance.

To set up CI/CD:
```
name: CICD

on:
  push:
    branches: [master]

jobs:
  build:
    runs-on: [ubuntu-latest]
    steps:
      - name: Checkout source
        uses: actions/checkout@v3
      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: '17'
      - name: Build Project
        env:
          ACCESS_KEY: ${{ secrets.ACCESS_KEY }}
          SECRET_KEY: ${{ secrets.SECRET_KEY }}
        run: mvn clean install -DskipTests
      - name: Login to docker hub
        run: docker login -u YOUR_DOCKER_USERNAME -p YOUR_DOCKER_PASSWORD
      - name: Build docker image
        run: docker build -t YOUR_DOCKER_USERNAME/image_upload .
      - name: Publish image to docker hub
        run: docker push YOUR_DOCKER_USERNAME/image_upload:latest

  deploy:
    needs: build
    runs-on: [AWS-EC2]
    steps:
      - name: Pull Image from docker hub
        run: docker pull YOUR_DOCKER_USERNAME/image_upload:latest
      - name: Delete old container
        run: docker rm -f image_upload-container
      - name: Run docker container
        run: docker run -e ACCESS_KEY=AWS_ACCESS_KEY -e SECRET_KEY=AWS_SECRET_KEY  -d -p 8080:8080 --name image_upload-container YOUR_DOCKER_USERNAME/image_upload

```

1. Replace the placeholder values in the workflow file with your AWS credentials and container registry information.

2. Enable GitHub Actions for your repository.

3. Push changes to trigger the CI/CD pipeline.
#### NOTE:

Before deploying the app please install docker in your virtual machine and configure Github Action Runner.
After configure Github Action Runner run the action-runner by `./run.sh`

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvement, please create a GitHub issue or submit a pull request.

