<<<<<<< HEAD
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

=======
FROM eclipse-temurin:17-jdk AS
build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package
-DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar
app.jar

EXPOSE 8080

>>>>>>> 137d3a10c309e0fc9120dea4abf97d2e4b74870c
ENTRYPOINT ["java", "-jar", "app.jar"]