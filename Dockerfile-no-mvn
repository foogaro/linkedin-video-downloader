FROM adoptopenjdk/openjdk11:alpine as release
WORKDIR /app
COPY target/linkedin-video-downloader-1.0.0.jar /app/app.jar
ENTRYPOINT ["java", "-jar"]
CMD ["/app/app.jar"]