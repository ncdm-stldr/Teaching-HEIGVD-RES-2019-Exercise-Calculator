#!/bin/bash

# Ask maven to build the executable jar file from the source files
mvn clean package --file ../Client/pom.xml

# Copy the executable jar file in the current directory
cp ../Client/target/TCP-Client-1.0-SNAPSHOT.jar .

# Build the Docker image locally
docker build --tag java-client .
