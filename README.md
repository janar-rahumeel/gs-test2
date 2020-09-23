## Testing
To launch your application's tests, run:
```
mvnw verify
```
To launch integration tests included, run:
```
mvnw integration-test
```
## Building
To build jar package, run:
```
mvnw package
```
## Dockerizing
To dockerize application, run:
```
mvnw compile jib:dockerBuild
```
## Usage
To execute jar from command line, run: 
```
>java -jar test-0.0.1-SNAPSHOT.jar
```
####In Shell
Type *help* for help
```
shell:>help
```
Type *coindesk EUR* for rates information
```
shell:>coindesk EUR
```
... outputs ...
```
---------------------------------------------------
Currency EUR CoinDesk bitcoin rates
Current bitcoin rate 8903.1215
Lowest bitcoin rate 10032.5 on 09.09.2020
Highest bitcoin rate 11795.7 on 02.09.2020
---------------------------------------------------
```
