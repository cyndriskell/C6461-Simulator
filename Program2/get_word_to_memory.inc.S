; GetWordToMemory:
; ArgsArray:
;   buf:    buffer to store the read word
GetWordToMemory: PUSH(3)  ; sget(0) = return addr
   
    LRAO(0,3,0)      ; load address of buf into x3
    PUSH(3)          ; buf: [char]
    LXAO(0,3,0)

    gwtmLoop: in 2,0                ; read char from keyboard into r2
        out 2,1
        sir 2,10                    ; check if register 2 contains a new line (decimal 10)
        JUMPZ(2,EndgwtmAddr)        ; if so, exit loop
        air 2,10
        str 2,3,0,0                 ; store the char into buf
        INCX(3)                     ; increment x3
        JUMP(gwtmLoopAddr)

    Endgwtm:
        str 2,3,0,0                 ; store the null term into buf
        INCX(3)
        POP(0)
        POP(3)
        rfs  




