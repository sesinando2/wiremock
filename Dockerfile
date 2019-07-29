FROM openjdk:8-jdk-slim
ENV PORT 8888
EXPOSE 8888
COPY wiremock.jar /opt/app.jar
WORKDIR /opt
CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-jar", "app.jar", "--port", "8888"]