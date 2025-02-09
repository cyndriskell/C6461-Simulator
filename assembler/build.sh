#! /usr/bin/env sh

if [[ $1 == "run" ]]; then
    ./build.sh build
    java C6461Assembler $@
fi

if [[ $1 == "build" ]]; then
    javac {.,*}/*.java
fi

if [[ $1 == "clean" ]]; then
    rm {.,*}/*.{class,jar} 2>/dev/null
    rm *out* 2>/dev/null
fi

if [[ $1 == "package" ]]; then
    ./build.sh build
    jar --create --file C6461Assembler.jar --main-class C6461Assembler {.,*}/*.class
fi