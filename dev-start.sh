#!/bin/bash

# Start the PostgreSQL container in the background
./postgresql.sh &

# Wait for a few seconds to ensure the PostgreSQL container has started
sleep 3

# Start the Spring Boot application
./gradlew bootRun