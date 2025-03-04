#! /usr/bin/env sh

SCRIPT=`readlink -f "$0"`
SCRIPTPATH=`dirname "$SCRIPT"`

if [[ $1 == "clean" ]]; then
    rm -rf $SCRIPTPATH/out
    exit
fi

rm -rf out >/dev/null 2>/dev/null
mkdir out >/dev/null 2>/dev/null

./assembler/build.sh package
./simulator/build.sh package
 mv ./assembler/out/* ./out/
 mv ./simulator/out/* ./out/
 ./assembler/build.sh clean
 ./simulator/build.sh clean

echo Done!