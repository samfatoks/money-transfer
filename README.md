### Money Transfer API

A basic REST API for money transfer between accounts.


### Technology Stack
- Java (Language)
- Jetty (Embedded web server)
- Jersey (JAX-RS implementation)
- Guice (Dependency Injection)
- H2 (Embedded/In-memory database)
- HikariCP (DB Connection pool)
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

### Test
In project root directory, type the command below to run the unit test and integration test included in the project
```
./gradlew test
```

### Rest Endpoints
1. `GET /account` - get all accounts
2. `GET /account/{username}` - get account details for **username**
3. `POST /account` - Create new account
4. `DELETE /account/{username}` - delete account by **username**
5. `GET /account/{username}/credit?amount=100` - Credit or Contribute 100 to account owned by **username**.
6. `GET /account/{username}/debit?amount=100` - Debit or Withdraw 100 from account owned by **username**.
7. `GET /account/{username1}/transfer?to={username2}&amount=100` - Transfer 100 from account owned by **username1** to account owned by **username2**.


