#!/usr/bin/env sh
./preproc.sh program1.S
cd ..
java -jar out/C6461Assembler.jar --listing Program1/program1.S.proc
mv a.out Program1/mindiffcalc
mv a.out.list Program1/mindiffcalc.list
cd -

