; parse_nums(number: [char], dst: *mut i16)
ParseNums:
    PUSH(3)     ; sget(8) ret addr
    ; unpack args passed through pointer in r0 onto stack
    ; ARGS
    LRAO(0,2,0) ; load address of the num char array (arg number)
    PUSH(2) ; sget(7) =                ; number: [char]
    LRAO(1,2,0) ; load address of the result location (arg dst)
    PUSH(2) ; sget(6) =                ; dst: *mut i16

    ; LOCAL VARS
    ldr 0,0,0   ; load addr(0) = 0 into r0 (for zero initalizing local vars)
    PUSH(0) ; sget(5) =                ; is_neg: bool (= false (0))
    PUSH(0) ; sget(4) =                ; num_digits: u16 (= 0)
    PUSH(0) ; sget(3) =                ; idx: u16 = 0
    PUSH(0) ; sget(2) =                ; res: i16 = 0
    PUSH(0) ; sget(1) =                ; place: u16 (= 0)
    PUSH(0) ; sget(0) =                ; len: u16 (= 0)


    ;; Start of StrLen Arg Stack Setup
    SGET(2,7)
    PUSH(0) ; strlen_args: [*c_void] = &strlen_string
    PUSH(2) ; strlen_string: [char] = number
    PUSH(0) ; strlen_out: *mut u16 = &len
    SPTR(0,1) ; &strlen_string
    SPUT(0,2)

    SPTR(0,3) ; &len
    SPUT(0,0)
    ;; End of StrLen Arg Stack Setup

    SPTR(0,2)
    CALLRO(SRSTRLEN,0) ; (*StrLenAddr)(&strlen_args) ; strlen(number,&len)
    ;; StrLen Arg Stack Teardown
    POPC(3)

    SGET(2,3)
    SGETI(0,7,2) ; r0 =                                     ; local0: char = number[idx]

    sir 0,HYPHEN_1
    sir 0,HYPHEN_2
    JUMPZ(0,CharIsNegAddr)                                  ; 
    JUMP(CharNotNegAddr)                                    ; 
    CharIsNeg:
                                                            ; if local0 == '-' {
        LRI(0,1)
        SPUT(0,5)                                           ;    is_neg = true
        SGET(0,0)
        sir 0,1
        SPUT(0,4)                                           ;    num_digits = len-1
        LRI(0,1)
        SPUT(0,3)                                           ;    idx = 1

        JUMP(AfterCharIfAddr)
                                                            ; }
        
    CharNotNeg:
                                                            ; else {
        SGET(0,0)
        SPUT(0,4)                                           ;    num_digits = len
                                                            ; }
    AfterCharIf:
    ;; Start Pow Arg Stack Setup
    SPTR(2,1)   ; r2 = &place
    SGET(3,4)   ; r3 = num_digits
    LRI(0,10)
    PUSH(0)     ; base     = 10                 5
    sir 3,1
    PUSH(3)     ; exp      = num_digits - 1     4
    LRI(0,0)
    PUSH(0)     ; pow_args                      3
    PUSH(0)     ; pow_num                       2
    PUSH(0)     ; pow_exp                       1
    PUSH(2)     ; pow_out  = &place             0

    SPTR(0,2)
    SPUT(0,3)   ; pow_args = &pow_num

    SPTR(0,5)
    SPUT(0,2)   ; pow_num  = &base

    SPTR(0,4)
    SPUT(0,1)   ; pow_exp  = &exp
    ;; End Pow Arg Stack Setup

    SPTR(0,3)
    CALLRO(SRPOW,0) ; (*PowAddr)(&pow_args)
    ;; Pow Arg Stack Teardown
    POPC(6)

                                                            ; place = 10.pow((num_digits-1))
    ParseNumLoop:
        SGET(0,3)
        sir 0,6
        JUMPGE(0,ParseNumLoopEndAddr)                       ; while idx < 6 {
        SGET(2,3)
        SGETI(0,7,2)                                        ;   local0: char = number[idx]
        JUMPZ(0,ParseNumLoopEndAddr)                        ;   if local0 == 0 { break }
        sir 0,ASCII_HALF_NUM_OFFSET
        sir 0,ASCII_HALF_NUM_OFFSET                         ;   local0 -= 0o60
        SGET(2,1)
        mlt 0,2                                             ;   local1 = local0 * place; local0 = 0
        SPTR(0,2)
        RTX(0,1)
        amr 1,1,0                                           ;   local1 += res
        RTR(1,0)
        SPUT(0,2)                                           ;   res = local1

        LRI(0,10)
        dvd 2,0
        SPUT(2,1)                                           ;   place /= 10

        SGET(0,3)
        air 0,1
        SPUT(0,3)                                           ;   idx += 1
        JUMP(ParseNumLoopAddr)                              ; }
        
    ParseNumLoopEnd:
    SGET(0,5)
    JUMPZ(0,NotNegAddr)                                     ; if !is_neg {
        SGET(0,2)
        not 0
        air 0,1
        SGET(2,6)
        RTX(2,1)
        str 0,1,0                                           ;   *dst = (!res)+1   
    JUMP(ParseNumReturnAddr)                                ; }
    
    NotNeg:
                                                            ; else {
        SGET(0,6)
        RTX(0,1)
        SGET(0,2)
        str 0,1,0                                           ;   *dst = res
                                                            ; }
    
    ParseNumReturn:
    POPC(8)
    POP(3)
    rfs