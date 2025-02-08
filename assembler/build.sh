#! /usr/bin/env sh

if [[ $1 == "run" ]]; then
    ./build.sh build
    java C6461Assembler $@
fi

if [[ $1 == "build" ]]; then
    javac {.,*}/*.java
fi

if [[ $1 == "clean" ]]; then
    rm {.,*}/*.class
fi