#!/bin/bash
mkdir -p executables
cd server
./mvnw clean package -DskipTests
cp target/server3.war ../executables/
cd ../client
./mvnw clean package -DskipTests
cp target/client3.jar ../executables/
