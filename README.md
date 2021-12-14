
## Info
Java version : Java 11 <br/>
Developed with : VSCode <br />
This project is a `SpringBoot` project. (maven) <br />
Uses `MySQL` as database

## Instructions to run the project: <br />
-> open `application.properties` file <br />
-> change the following properties (see the current fields for reference) <br />

spring.datasource.url= {local mysql url} <br />
spring.datasource.username= {local mysql user} <br />
spring.datasource.password= {local mysql password} <br />


## Commands:
`mvn clean package -DskipTests` <br />
This will build a jar file, which can be found in target folder

`java -jar ./target/humbleBootMain-0.0.1-SNAPSHOT.jar`  <br />
Excecute the JAR file which was built earlier



Patterns  implemented:

Builder Pattern


Factory Pattern


Singleton Pattern


Facade Pattern

Observer& Command

Strategy


