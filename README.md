This project is a `SpringBoot` project. (maven)
Uses `MySQL` as database

Instructions to run the project:
-> open application.properties file
-> change the following properties (see the current fields for reference)

spring.datasource.url= {local mysql url}
spring.datasource.username= {local mysql user}
spring.datasource.password= {local mysql password}


Commands:
`mvn clean package -DskipTests`
This will build a jar file, which can be found in target folder

`java -jar ./target/humbleBootMain-0.0.1-SNAPSHOT.jar`
Excecute the JAR file which was built earlier



Patterns  implemented:

Builder Pattern


Factory Pattern


Singleton Pattern


Facade Pattern

Observer& Command

Strategy


