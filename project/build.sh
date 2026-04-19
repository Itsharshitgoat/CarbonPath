#!/bin/bash
mkdir -p build
javac -cp "lib/mysql-connector-j.jar:build" src/*.java -d build/
