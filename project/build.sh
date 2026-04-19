#!/bin/bash
mkdir -p build
mkdir -p lib

# Download MySQL Connector if it doesn't exist
if [ ! -f "lib/mysql-connector-j.jar" ]; then
    echo "Downloading MySQL JDBC driver..."
    curl -o lib/mysql-connector-j.jar https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar
fi

echo "Compiling..."
javac -cp "lib/mysql-connector-j.jar:build" src/*.java -d build/
echo "Build complete."
