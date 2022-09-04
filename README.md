# Credit Suisse Technical Test

## Description
This Spring Boot Command Line project takes one parameter, path to a text file which contains log and application log entries in **valid** JSON format. 
When project is run, it populates HSQLDB database, named `event`, under `db` directory.

## Requirements
* Java 8

## Running tests
```
cd PROJECT_DIR
mvn clean install
``` 

## Running project
* Unix
```
cd PROJECT_DIR
mvn clean install
java -jar target/logingestion-service.jar path/to/input/file
``` 
