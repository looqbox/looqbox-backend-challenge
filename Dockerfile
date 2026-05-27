FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradle gradle
COPY gradlew ./
COPY gradlew.bat ./
COPY build.gradle.kts settings.gradle.kts ./
COPY src src

RUN chmod +x gradlew \
	&& ./gradlew bootJar --no-daemon -x test \
	&& JAR="$(ls build/libs/*.jar | grep -v plain)" \
	&& cp "$JAR" /app/application.jar

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/application.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
