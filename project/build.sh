#!/bin/bash
mkdir -p build
javac -cp "lib/*:build" src/*.java -d build/
