#Money Transfer API

A basic REST API for money transfer between accounts.


### Technology Stack
- Java (Language)
- Jetty (Embedded web server)
- Jersey (JAX-RS implementation)
- Guice (Dependency Injection)
- H2 (Embedded database)
- HikariCP (DB Connection Pool)
- Logback (Logging)

### Requirement
> Java 8+
### Build
In project root directory, type the command below to build fat jar</br>
```
./gradlew shawdowJar
```
### Run
In project root directory, type the command below to run </br>
```
java -jar build/libs/money_transfer-0.1-all.jar
```
or
```
./gradlew run
```