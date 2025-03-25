; strlen(string: [char], out: *mut u16)

; StrLen: Returns the length of a null terminated string
; Arg:
;   str: memory location of the string to get the length of
;   out; memory location to put the length
StrLen: PUSH(3)                 ; sget(2) store ret addr 
    LRAO(0,2,0)
    PUSH(2)                     ; sget(1) store str addr
    LRAO(1,2,0)
    PUSH(2)                     ; sget(0) store out addr
    
    SGET(0,1)                   ; load str addr into r0
    RTX(0,2)                    ; load str addr into x2
    ldr 2,0,0                   ; r2 = count

    StrLenLoop: ldr 0,2,0       ; load char from str into r0
        JUMPZ(0,EndStrLenAddr)  ; if char is null, exit loop
        INCX(2)                 ; increment char pointer
        air 2,1                 ; increment count
        JUMP(StrLenLoopAddr)
    EndStrLen: SGET(0,0)        ; get out addr into r0


    RTX(0,2)                    ; load out addr into x2
    str 2,2,0                   ; store count into out
    POPC(2)
    POP(3)  ; pop ret addr
    rfs