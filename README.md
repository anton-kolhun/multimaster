### N-master data processi g application

## Required Prerequisites

* JDK 8

## Building and Running

```
./mvnw  package spring-boot:run
```

## Usage Instructions

KeyValueProcessorControllerTest.java  can be taken as an example how to call application endpoints

### data reading

```
curl localhost:8080/data
```

### data saving

curl -v -H "Content-Type: application/json" -d "[\"key\", "\value\"]" localhost:8080/data

etc...