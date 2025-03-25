; print_number(number: i16)
PrintNumber:
    PUSH(3)
    SALLOC(2,7)
    SALLOC(3,7)
    PUSH(3)                     ; reverse_buffer: [char; 7]         4
    ldr 3,0,0
    PUSH(2)                     ; buffer: [char; 7]                 3
    LRAO(0,2,0)
    PUSH(2)                     ; number: i16                       2
    PUSH(3)                     ; idx: u16 = 0                      1
    air 3,6
    PUSH(3)                     ; r_idx: u16 = 6                    0

    SGET(0,2)
    JUMPNZ(0,PRNotZeroAddr)
    LRI(2,ASCII_HALF_NUM_OFFSET)
    air 2,ASCII_HALF_NUM_OFFSET
    LRI(0,0)
    SPUTI(2,3,0)
    LRI(2,0)
    LRI(0,1)
    SPUTI(2,3,0)
    SPTR(0,3)
    CALLRO(SRPRINT,0)
    rfs

    PRNotZero:
    SGET(0,2)          
    JUMPGE(0,PRNotNegAddr)      ; if number < 0 {
    LRI(0,HYPHEN_1)
    air 0,HYPHEN_2
    LRI(2,0)
    SPUTI(0,3,2)                ;   buffer[0] = '-'
    LRI(0,1)
    SPUT(0,1)                   ;   idx = 1
    SGET(0,2)
    not 0
    air 0,1
    SPUT(0,2)                   ;   number = (!number)+1
                                ; }
    PRNotNeg:
                                ; do {
    SGET(2,2)                   ;   local2 = number
    LRI(0,10)
    dvd 2,0                     ;   local2 = number / 10
                                ;   local3 = number % 10
    SPUT(2,2)                   ;   number = local2
    air 3,ASCII_HALF_NUM_OFFSET
    air 3,ASCII_HALF_NUM_OFFSET ;   local3 + ASCII_HALF_NUM_OFFSET*2
    SGET(0,0)                   
    SPUTI(3,4,0)                ;   reverse_buffer[r_idx] = local3
    sir 0,1
    SPUT(0,0)                   ;   r_idx -= 1
    JUMPNZ(2,PRNotNegAddr)      ; } while number != 0
                                ;
    air 0,1
    SPUT(0,0)

    ReverseLoop:
                                ; do {
    SGET(0,0)
    SGETI(2,4,0)
    SGET(0,1)
    SPUTI(2,3,0)                ;   buffer[idx] = reverse_buffer[r_idx]
    
    SGET(0,0)
    air 0,1
    SPUT(0,0)                   ; r_idx += 1
    SGET(0,1)
    air 0,1
    SPUT(0,1)                   ; idx += 1
    LRI(2,6)
    SGET(0,0)
    str 0,0,1
    smr 2,0,1
    JUMPGE(2,ReverseLoopAddr)   ; } while r_idx <= 6
    
    SGET(0,1)
    LRI(2,0)
    SPUTI(2,3,0)

    SPTR(0,3)
    CALLRO(SRPRINT,0)
    POPC(19)
    POP(3)
    rfs
