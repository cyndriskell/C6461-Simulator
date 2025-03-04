#! /usr/bin/env sh

SCRIPT=`readlink -f "$0"`
SCRIPTPATH=`dirname "$SCRIPT"`


if [[ $1 == "run" ]]; then
    $SCRIPT build
    java C6461SimulatorCLI
fi

if [[ $1 == "build" ]]; then
    javac $SCRIPTPATH/*.java
fi

if [[ $1 == "clean" ]]; then
    rm $SCRIPTPATH/*.{class,jar} 2>/dev/null
    rm -rf $SCRIPTPATH/out
fi

if [[ $1 == "package" ]]; then
    $SCRIPT clean
    $SCRIPT build
    mkdir $SCRIPTPATH/out >/dev/null 2>/dev/null
    cd $SCRIPTPATH
    jar --create --file $SCRIPTPATH/out/C6461SimulatorGUI.jar --main-class C6461SimulatorGUI ./*.class
    jar --create --file $SCRIPTPATH/out/C6461SimulatorCLI.jar --main-class C6461SimulatorCLI ./*.class
    cd - >/dev/null 2>/dev/null
    rm $SCRIPTPATH/*.class
fi