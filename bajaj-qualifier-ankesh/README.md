# Bajaj Qualifier Spring Boot Project (Auto-generated)

This project was generated for **Anikesh Kumar** with the following pre-filled details:

- **Name:** Anikesh Kumar
- **RegNo:** 112215021
- **Email:** 112215021@cse.iiitp.ac.in

## What it does
On application startup the app:
1. POSTs to the generateWebhook API to receive a `webhook` URL and `accessToken`.
2. Determines which SQL question to solve based on the last two digits of the registration number.
3. Sends the final SQL query to the received webhook URL using the `accessToken` in the `Authorization` header.

> Note: The project includes sample SQL strings for each question. Replace them with your final SQL query based on the Google Drive question.

## How to build & run

Make sure you have **Java 17+** and **Maven** installed.

```bash
# Build
mvn clean package

# Run
java -jar target/bajaj-qualifier-0.0.1-SNAPSHOT.jar
```

If you want to run via your IDE (IntelliJ/VSCode), open the project folder and run `DemoApplication`.

## About Maven Wrapper
You asked for Maven Wrapper, but the execution environment that created this ZIP did not include the maven-wrapper binaries. If you need the wrapper files, run this on a machine with Maven installed:

```bash
mvn -N io.takari:maven:wrapper
```

This will generate the `mvnw`, `mvnw.cmd` and `.mvn` folder for you.

## Replace the SQL
Open `src/main/java/com/example/demo/DemoApplication.java` and edit the `getFinalSQLQuery` method to put your final query.

## Submission
1. Push to GitHub (public repo)
2. Upload the JAR or use GitHub Releases
3. Fill the form: https://forms.office.com/r/ZbcqfgSeSw
