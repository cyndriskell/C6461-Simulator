; GetNumberToMemory: Reads from console input into `buf` until a newline is recieved. Then places the parsed number at `dst`
; ArgsArray:
;   buf:    buffer to store the read numbers, the buffer must hold 7 chars
;   dst:    memory location to put the parsed number
GetNumberToMemory: PUSH(3)  ; sget(1) = return addr
    PUSH(0) ;sget(0) = r0

    ; call print
    ldr 0,0,StrOffsetTable
    air 0,1
    CALLRO(SRPRINT,0)

    ; load ret addr and args from stack
    POP(0) ; r0
   
    LRAO(0,3,0)      ; load address of buf into x3
    PUSH(3)          ; buf: [char]
    LRAO(1,3,0)
    PUSH(3)          ; dst: *mut i16
    LXAO(0,3,0)
    gntmLoop: in 2,0                ; read char from keyboard into r2
        out 2,1
        sir 2,10                    ; check if register 2 contains a new line (decimal 10)
        JUMPZ(2,EndgntmAddr)        ; if so, exit loop
        air 2,10
    
        str 2,3,0,0                 ; store the char into buf
        INCX(3)                     ; increment x3
        JUMP(gntmLoopAddr)

    Endgntm:
        ldr 0,0,0
        str 0,3,0
        ; parse the number
        ;; Start of ParseNums Arg Stack Setup
        ldr 0,0,0
        SGET(2,1)
        SGET(3,0)
        PUSH(0) ; parsenums_args: [*const c_void]
        PUSH(2) ; parsenums_number: [char]
        PUSH(3) ; parsenums_dst: *mut i16
        SPTR(0,1)
        SPUT(0,2)   ; parsenums_args = &parsenums_number
        ;; End of ParseNums Arg Stack Setup
        SPTR(0,2)
        CALLRO(SRPARSE,0)

        POPC(5)
        POP(3)
        rfs  




