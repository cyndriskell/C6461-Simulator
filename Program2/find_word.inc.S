#define PERIOD_HALF 23
#define SPACE_HALF 16
#define PUSHREGS() \
SPUT(idx,idx_rg) \
__NL__ XTR(curr_char,idx) \
__NL__ SPUT(idx,curr_char_rg) \
__NL__

#define POPREGS() \
SGET(idx,curr_char_rg) \
__NL__ RTX(idx,curr_char) \
__NL__ SGET(idx,idx_rg) \
__NL__

; find_word(sentence_idx: *u16, word_idx: *u16, word: *char, in: *char, buf: *char)
FindWord:
        ; Prologue
        PUSH(3)
        SALLOC(2,3)
        PUSH(3)                ; sentence_idx_old     u16        ; 15
        #define sentence_idx_old 15
        PUSH(3)                ; word_idx_old         u16        ; 14
        #define word_idx_old 14
        PUSH(3)                ; print_number_args    *args      ; 13
        #define print_number_args 13
        PUSH(3)                ; print_number_args_p  **args     ; 12
        #define print_number_args_p 12
        PUSH(3)                ; idx_rg:              reg        ; 11
        #define idx_rg 11
        PUSH(3)                ; curr_char_rg:        xreg       ; 10
        #define curr_char_rg 10
        PUSH(2)                ; match_str_args:      *args      ; 9
        #define match_str_args 9
        PUSH(2)                ; match_str_args_p:    **args     ; 8
        #define match_str_args_p 8
        LRI(2,1)
        PUSH(2)                ; match_str_res:       bool       ; 7
        #define match_str_res 7
        PUSH(2)                ; sentence_idx:        u16        ; 6
        #define sentence_idx 6
        PUSH(2)                ; word_idx:            u16        : 5
        #define word_idx 5
        LRAO(0,2,0)
        PUSH(2)                ; out_sentence_idx:    *u16       ; 4
        #define out_sentence_idx 4
        LRAO(1,2,0)
        PUSH(2)                ; out_word_idx:        *u16       ; 3
        #define out_word_idx 3
        LRAO(2,2,0)
        PUSH(2)                ; word:                *char      ; 2
        #define word 2
        LRAO(3,2,0)
        PUSH(2)                ; in:                  *char      ; 1
        #define in 1
        RTX(2,3)               ; curr_char:           *char      ; x3
        #define curr_char 3
        LRAO(4,2,0)
        PUSH(2)                ; buf:                 *char      ; 0
        #define buf 0
        LRI(2,0)               ; idx:                 u16        ; r2
	#define idx 2
                               ; char:                char       ; r0
        #define char 0

        ; Body
        SPTR(0,print_number_args)
        SPUT(0,print_number_args_p)
        
        SPTR(0,match_str_args)
        SPUT(0,match_str_args_p)
        SGET(0,buf)
        ldr 3,0,0
        SPUTI(0,match_str_args,3)
        SGET(0,word)
        LRI(3,1)
        SPUTI(0,match_str_args,3)
        SPTR(0,match_str_res)
        LRI(3,2)
        SPUTI(0,match_str_args,3)
        FindWordLoop:
                ldr char,curr_char,0            ; char = *curr_char
                JUMPZ(char,CharNullAddr)        ; if char == 0 {
                                                ;        goto CharNull;
                                                ; }
                                                ; 
                sir char,SPACE_HALF             ; if char == ' ' {
                sir char,SPACE_HALF             ;        goto CharSpace; 
                JUMPZ(char,CharSpaceAddr)       ; }
                air char,SPACE_HALF             ;
                air char,SPACE_HALF             ;
                                                ;
                sir char,PERIOD_HALF            ; if char == '.' {
                sir char,PERIOD_HALF            ;        goto CharPeriod;
                JUMPZ(char,CharPeriodAddr)      ; }
                air char,PERIOD_HALF            ;
                air char,PERIOD_HALF            ;

                SGET(3,buf)                     ; r3 = buf+idx;
                ARR(idx,3)                      ;
                                                ;
                str 3,0,1                       ; *r3 = char;
                str char,0,1,1                  ;
                                                ;
                air idx,1                       ; idx += 1;
                INCX(curr_char)                 ; curr_char += 1;
                JUMP(FindWordLoopAddr)          ; goto FindWordLoop;

        CharNull:
                JUMPZ(idx,FWNotFoundAddr)       ; if idx == 0 {
                SGET(0,sentence_idx)
                SPUT(0,sentence_idx_old)
                SGET(0,word_idx)                ;        goto FWNotFound;
                                                ; } else {
                SPUT(0,word_idx_old)            ;        goto FWCheckWord;
                JUMP(FWCheckWordAddr)           ; }
        CharSpace:
                                                ; if idx == 0 {
                INCX(curr_char)                 ;         curr_char += 1;
                JUMPZ(idx,FindWordLoopAddr)     ;         goto FindWordLoop;
                                                ; } else {
                SGET(0,sentence_idx)
                SPUT(0,sentence_idx_old)
                SGET(0,word_idx)
                SPUT(0,word_idx_old)
                SGET(3,word_idx)                ;         word_idx += 1;
                air 3,1                         ;
                SPUT(3,word_idx)                ;
                JUMP(FWCheckWordAddr)           ;         goto FWCheckWord;
                                                ; }
        CharPeriod: 
                                                ; if idx == 0 {
                INCX(curr_char)                 ;         curr_char += 1;
                JUMPZ(idx,FindWordLoopAddr)     ;         goto FindWordLoop;
                                                ; } else {
                SGET(0,word_idx)
                SPUT(0,word_idx_old)
                LRI(3,1)                        ;         word_idx = 1;
                SPUT(3,word_idx)                ;
                SGET(3,sentence_idx)            ;         sentence_idx += 1;
                SPUT(3,sentence_idx_old)
                air 3,1                         ;
                SPUT(3,sentence_idx)            ;
                JUMP(FWCheckWordAddr)           ;         goto FWCheckWord;
                                                ; }
        
        FWCheckWord:
                SGET(3,buf)                     ; r3 = buf+idx;
                ARR(idx,3)                      ;                          
                str 3,0,1                       ; *r3 = 0;
                ldr 1,0,0                       ;
                str 1,0,1,1                     ;
                PUSHREGS()                      ; StrMatch(buf, in, &match_str_res)
                SGET(0,match_str_args_p)        ;
                CALLRO(SRSTRMATCH,0)            ;
                POPREGS()                       ;
		SGET(0,match_str_res)           ; if match_str_res == 0 {
                ldr idx,0,0                     ;         idx = 0;
                JUMPZ(0,FindWordLoopAddr)       ;         goto FindWordLoop;
                                                ; } else {
                JUMP(FWFoundAddr)               ;         goto FWFound;
                                                ; }


        FWNotFound:
                ldr 0,0,StrOffsetTable          ; print(NotFoundStr)
                air 0,2                         ;
                CALLRO(SRPRINT,0)               ;
                hlt                             ; hlt

        FWFound:
                ldr 0,0,StrOffsetTable          ; print(FoundStr1)
                air 0,3                         ;
                CALLRO(SRPRINT,0)               ;
                SPTR(0,sentence_idx_old)
                SPUT(0,print_number_args)
                SGET(0,print_number_args_p)
                CALLRO(SRPRINTNUM,0)
                ldr 0,0,StrOffsetTable
                air 0,4
                CALLRO(SRPRINT,0)
                SPTR(0,word_idx_old)
                SPUT(0,print_number_args)
                SGET(0,print_number_args_p)
                CALLRO(SRPRINTNUM,0)
                hlt                             ; hlt
