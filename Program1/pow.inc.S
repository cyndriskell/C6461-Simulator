; pow(number: *const u16, exp: *const u16, out: *mut u16)

; Arg: 
;   num : mem location of the base
;   exp : mem location of the exponent
;   out : mem location to put the result
Pow: PUSH(3)                        ; sget(5) store ret addr
    LRAO(0,2,0)           
    PUSH(2)                         ; sget(4) store num addr
    LRAO(1,2,0)
    PUSH(2)                         ; sget(3) store exp addr
    LRAO(2,2,0)           
    PUSH(2)                         ; sget(2) store out addr

    ldr 0,0,0
    PUSH(0)                         ; sget(1) store unmutable num later
    PUSH(0)                         ; sget(0) store unmutable exp later

    SGET(0,3)                       ; load exp addr into r0
    RTX(0,2)                        ; load exp addr into x2
    ldr 0,2,0                       ; load exp into r0
    JUMPZ(0,PowZeroAddr)                ; if exp == 0, out = 1
    SPUT(0,0)                       ; store exp in mem

    SGET(2,4)                       ; load num addr into r2
    RTX(2,2)                        ; load num addr into x2
    ldr 2,2,0                       ; load num into r2 
    SPUT(2,1)                       ; store num in mem


    SGET(0,1)                       ; move num to where mlt wants
    SGET(2,1)                       ; it to be
    SGET(3,0)                       ; move exp to where it wont get clobbered

    sir 3,1                         ; num of mults is exp - 1
    PowLoop: JUMPZ(3,PowLoopEndAddr)    ; tracks number of mults left
        mlt 0,2
        RTR(1,0)                   ; copy result back to r0
        sir 3,1                    ; decrement exp
        JUMP(PowLoopAddr)


    PowZero: ldr 0,0,0              ; put val zero in r0
        air 0,1                     ; put val one in r0
        JUMP(PowLoopEndAddr)

    PowLoopEnd: SGET(1,2)           ; get out addr into r1
        RTX(1,2)                    ; load out addr into x2
        str 0,2,0                   ; store result into out

    POPC(5)
    POP(3)                          ; pop ret
    rfs
