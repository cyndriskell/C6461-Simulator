#!/usr/bin/env sh
./preproc.sh program2.S
cd ..
java -jar out/C6461Assembler.jar --listing Program2/program2.S.proc
mv a.out Program2/wordsearch
mv a.out.list Program2/wordsearch.list
cd -

