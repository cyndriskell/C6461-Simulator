#include "macros.inc.S"

LOC 0                   ; segment .data
Data 0                  ; Always zero
Data 0                  ; Used for swapping register vals
StackPtr: Data Stack
StackBase: Data Stack

LOC 4
StrOffsetTable: Data IntroStrAddr
SubroutineOffsetTable: Data PrintAddr

PrintLoopAddr: Data printLoop
EndPrintAddr: Data EndPrint

gntmLoopAddr: Data gntmLoop
EndgntmAddr: Data Endgntm

CharIsNegAddr: Data CharIsNeg
CharNotNegAddr: Data CharNotNeg
AfterCharIfAddr: Data AfterCharIf
ParseNumLoopAddr: Data ParseNumLoop
ParseNumLoopEndAddr: Data ParseNumLoopEnd
NotNegAddr: Data NotNeg
ParseNumReturnAddr: Data ParseNumReturn

StrLenLoopAddr: Data StrLenLoop
EndStrLenAddr: Data EndStrLen

PowLoopAddr: Data PowLoop
PowZeroAddr: Data PowZero
PowLoopEndAddr: Data PowLoopEnd

PRNotZeroAddr: Data PRNotZero
PRNotNegAddr: Data PRNotNeg
ReverseLoopAddr: Data ReverseLoop

MainNumberLoopAddr: Data MainNumberLoop
MainNumberLoopEndAddr: Data MainNumberLoopEnd
MainCheckLoopAddr: Data MainCheckLoop
MainCheckLoopEndAddr: Data MainCheckLoopEnd
FoundCloserAddr: Data FoundCloser
IsPosAddr: Data IsPos

// String offset
LOC 32
IntroStrAddr: Data IntroStr         ; 0
PromptStrAddr: Data PromptStr       ; 1
TargetStrAddr: Data TargetStr       ; 2
ResultStr1Addr: Data ResultStr1     ; 3
ResultStr2Addr: Data ResultStr2     ; 4

// Subroutine offset
LOC 40
PrintAddr: Data Print                           ; 0
GetNumberToMemoryAddr: Data GetNumberToMemory   ; 1
ParseNumsAddr: Data ParseNums                   ; 2
StrLenAddr: Data StrLen                         ; 3
PowAddr: Data Pow                               ; 4
PrintNumberAddr: Data PrintNumber               ; 5



LOC 100             ; segment .text
#include "get_number_to_memory.inc.S"
#include "parse_nums.inc.S"
#include "pow.inc.S"
#include "print.inc.S"
#include "str_len.inc.S"
#include  "print_number.inc.S"


LOC 1400    ; 300 words for main
; Program1: Read 20 numbers from console input, a target number, and then calculate which number is closest to the target and print to console out
; For all subroutines: an arg (which can be an array of args) is placed into r0 as a memory address; return value goes into r0;

ldr 0,0,StrOffsetTable
CALLRO(SRPRINT,0)

ldr 0,0,0
PUSH(0)         ; number_buffer: [i16]          0
PUSH(0)         ; gntm_buf: [char]              1
PUSH(0)         ; target: i16                   2
PUSH(0)         ; idx: u16                      3
LRI(0,1)
src 0,15,1,1
not 0
PUSH(0)         ; closest_diff: i16 = i16::MAX       4
ldr 0,0,0
PUSH(0)         ; closest: i16             5
SALLOC(0,20)
SPUTB(0,0)      ; number_buffer = salloc(20)
SALLOC(0,7)
SPUTB(0,1)      ; gntm_buf = salloc(6)

;; Start of GetNumberToMemory Arg Stack Setup
ldr 0,0,0
PUSH(0)         ; gntm_args                     2
PUSH(0)         ; gntm_arg_buf                  1
PUSH(0)         ; gntm_dst                      0
SPTR(0,1)
SPUT(0,2)       ; gntm_args = &gntm_arg_buf
SGETB(2,1)      ; local2 = gntm_buf
SPUT(2,1)       ; gntm_arg_buf = local2

;; End of GetNumberToMemory Arg Stack Setup

MainNumberLoop:
    SGETB(0,3)
    sir 0,20
    JUMPGE(0,MainNumberLoopEndAddr)                     ; while idx < 20 {
    SGETB(0,0)                                          ;     local0 = number_buffer
    SGETB(2,3)                                            
    str 2,0,1                                             
    amr 0,0,1                                           ;     local0 += idx

    SPUT(0,0)                                           ;     gntm_dst = local0
    SPTR(0,2)
    CALLRO(SRGNTM,0)                      ;     (*GetNumberToMemoryAddr)(&gntm_args)    ; gntm(gntm_buf,number_buffer+idx)
    
    SGETB(0,3)
    air 0,1
    SPUTB(0,3)                                          ;     idx += 1
    JUMP(MainNumberLoopAddr)                            ; }
MainNumberLoopEnd:

ldr 0,0,StrOffsetTable
air 0,2
CALLRO(SRPRINT,0)
SPTRB(0,2)
SPUT(0,0)                                               ; gntm_dst = &target
SPTR(0,2)
CALLRO(SRGNTM,0)                          ; (*GetNumberToMemoryAddr)(&gntm_args)    ; gntm(gntm_buf,&target)
;; GetNumberToMemory Arg Stack Teardown
POPC(3)

ldr 0,0,0
SPUTB(0,3)                                              ; idx = 0
PUSH(0)
PUSH(0)
MainCheckLoop:
    POP(0)
    POP(0)
    SGETB(0,3)
    sir 0,20
    JUMPGE(0,MainCheckLoopEndAddr)                      ; while idx < 20 {
    SGETB(0,0)
    SGETB(2,3)
    str 2,0,1
    amr 0,0,1
    str 0,0,1
    ldr 0,0,1,1
    PUSH(0)                                             ;   checking = number_buffer[idx]       1
    SGETB(3,3)
    air 3,1
    SPUTB(3,3)                                          ;   idx += 1
    SGETB(2,2)
    SGET(0,0)
    str 2,0,1
    smr 0,0,1
    RTR(0,2)
    src 2,15,0,12
    JUMPZ(2,IsPosAddr)
    not 0
    air 0,1
                                                        ;   diff = target - checking            0
    IsPos:
    PUSH(0)                                             ;   diff = diff.abs()
    
    SGETB(2,4)
    str 0,0,1
    smr 2,0,1
    JUMPGE(2,FoundCloserAddr)
    JUMP(MainCheckLoopAddr)
    FoundCloser:
                                                        ;   if diff <= closest_diff {
    SGET(0,0)
    SPUTB(0,4)                                          ;       closest_diff = diff
    SGET(0,1)
    SPUTB(0,5)                                          ;       closest = checking 
                                                        ;   }
    JUMP(MainCheckLoopAddr)                             ; }
MainCheckLoopEnd:

ldr 0,0,StrOffsetTable
air 0,3
CALLRO(SRPRINT,0)

//print target from gntm_buf
SPTRB(0,1)
CALLRO(SRPRINT,0)

ldr 0,0,StrOffsetTable
air 0,4
CALLRO(SRPRINT,0)

//print closest
;; Print Number Arg Stack
PUSH(0)         ; prnnum_args: *c_void
PUSH(0)         ; prnnum_number: i16
SPTR(0,0)
SPUT(0,1)
SGETB(0,5)
SPUT(0,0)
;; End Print Number Arg Stack

SPTR(0,1)
CALLRO(SRPRINTN,0)

hlt

LOC 1800        ; 150 words for constants
#include "constants.inc.S"


LOC 1950        ; Stack can hold 98 entries
Stack: Data 0
