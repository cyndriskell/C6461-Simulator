; Print: Prints a null terminated string
; Arg:
;   str: memory location of the string to print = *[char]
Print: RTX(0,3) ; load addr of str into x3
    lda 0,3,0,1 ; r0 = *str
    RTX(0,3) ; x3 = r0
    printLoop: ldr 2,3,0            ; load char from str into r2
        JUMPZ(2,EndPrintAddr)           ; if so, exit loop
        out 2,1                     ; output the char to console
        INCX(3)                     ; increment x3
        JUMP(PrintLoopAddr)

    EndPrint: rfs