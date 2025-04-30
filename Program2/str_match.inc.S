; StrMatch(str word1, str word2, bool result)
; Args:
;   word1
;   word2
;   result: mem location of result
StrMatch:
    PUSH(3)                     ; sget(3) store ret addr
    LRAO(0,2,0)                 
    PUSH(2)                     ; sget(2) store word1 addr
    LRAO(1,2,0)                 
    PUSH(2)                     ; sget(1) store word2 addr
    LRAO(2,2,0)                 
    PUSH(2)                     ; sget(0) store result addr

    SGET(0,2)                   ; load word1 addr into r0
    RTX(0,2)                    ; load word1 addr into x2
    SGET(0,1)                   ; load word2 addr into r0
    RTX(0,3)                    ; load word2 addr into x3
    ldr 0,0,0                   ; r0 = result
    air 0,1

    ldr 2,2,0       ; load char from word1 into r2
    ldr 3,3,0       ; load char from word2 into r3
    StrMatchLoop:
        str 2,0,1       ; str w1 in scratch
        smr 3,0,1       ; subtract c1 from c2
        JUMPNZ(3, StrMatchDiffAddr)     ; if different, return false

        INCX(2)         ; increment word1 pointer
        INCX(3)         ; increment word2 pointer

        ldr 2,2,0
        ldr 3,3,0
        JUMPZ(2,FirstCharNullAddr)
        JUMP(StrMatchLoopAddr)

    FirstCharNull:
        JUMPZ(3,EndStrMatchAddr)
        JUMP(StrMatchLoopAddr)

    StrMatchDiff:
        sir 0,1
        SGET(3,0)           ; get result addr into r3
        RTX(3,2)            ; load result addr into x2
        str 0,2,0           ; store result into result
        POPC(3)
        POP(3)
        rfs

    EndStrMatch:
        SGET(3,0)           ; get result addr into r3
        RTX(3,2)            ; load result addr into x2
        str 0,2,0           ; store result into result
        POPC(3)
        POP(3)
        rfs
