#!/usr/bin/env sh
cpp $1 | perl -p -e 's/^(\h*.*:)\n/\1 ldr 1,0,0\n/gm' - | sed 's/\h*\/\/.*//g' | sed 's/ __NL__ /\n/g' | sed 's/^[ |\t]*//' | sed 's/^#.*//' | sed '/^[[:space:]]*$/d' > "$1.proc"
