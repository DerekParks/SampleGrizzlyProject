#!/bin/sh
mvn clean && mvn install
java -jar target/com.company.someWebService-0.1-jar-with-dependencies.jar