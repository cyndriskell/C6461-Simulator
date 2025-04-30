#include "macros.inc.S"

LOC 0                 ; .data section
Data 0                ; Always zero
Data 0                ; Swap temp
StackPtr: Data Stack
StackBase: Data Stack

LOC 4
StrOffsetTable: Data ParagraphAddr
SubroutineOffsetTable: Data PrintAddr

PrintLoopAddr: Data printLoop
EndPrintAddr: Data EndPrint

gwtmLoopAddr: Data gwtmLoop
EndgwtmAddr: Data Endgwtm

StrMatchLoopAddr: Data StrMatchLoop
EndStrMatchAddr: Data EndStrMatch
StrMatchDiffAddr: Data StrMatchDiff
FirstCharNullAddr: Data FirstCharNull

FindWordLoopAddr: Data FindWordLoop
CharNullAddr: Data CharNull
CharSpaceAddr: Data CharSpace
CharPeriodAddr: Data CharPeriod
FWCheckWordAddr: Data FWCheckWord
FWFoundAddr: Data FWFound
FWNotFoundAddr: Data FWNotFound

PRNotZeroAddr: Data PRNotZero
PRNotNegAddr: Data PRNotNeg
ReverseLoopAddr: Data ReverseLoop


// String offset
LOC 32
ParagraphAddr: Data Paragaph        ; 0
PromptStrAddr: Data PromptStr       ; 1
NotFoundStrAddr: Data NotFoundStr   ; 2
FoundStr1Addr: Data FoundStr1       ; 3
FoundStr2Addr: Data FoundStr2       ; 4

// Subroutine offset
LOC 40
PrintAddr: Data Print                       ; 0
PrintNumberAddr: Data PrintNumber           ; 1
GetWordToMemmoryAddr: Data GetWordToMemory  ; 2
StrMatchAddr: Data StrMatch                 ; 3
FindWordAddr: Data FindWord                 ; 4

LOC 100               ; segment .text
#include "print.inc.S"
#include "print_number.inc.S"
#include "get_word_to_memory.inc.S"
#include "str_match.inc.S"
#include "find_word.inc.S"

LOC 1200           ; Main program

; Program2: Search for a word in paragraph uploaded into memory.

ldr 0,0,StrOffsetTable
CALLRO(SRPRINT,0)

ldr 0,0,StrOffsetTable
air 0,1
CALLRO(SRPRINT,0)

ldr 0,0,0
PUSH(0)             ; sentence_count            0
PUSH(0)             ; sentence_word_count       1
PUSH(0)             ; word: [char]              2

SALLOC(0,20)
SPUTB(0,2)      ; word = salloc(20)

;; Start of GetWordToMemory Arg Stack Setup
ldr 0,0,0
PUSH(0)         ; gwtm_args         1
PUSH(0)         ; gwtm_word         0
SPTR(0,0)       
SPUT(0,1)       ; gwtm_args = &gwtm_word
SGETB(2,2)      ; r2 = word 
SPUT(2,0)       ; gwtm_word = r2
;; End of GetWordToMemory Arg Stack Setup

SPTR(0,1)
CALLRO(SRGWTM, 0)


;; Start of FindWord Arg Stack Setup
SALLOC(2,20)
ldr 0,0,0
PUSH(0)        ; fw_args	; 5
PUSH(0)        ; fw_sent_idx	; 4
PUSH(0)        ; fw_word_idx	; 3
PUSH(0)        ; fw_word	; 2
PUSH(0)        ; fw_in		; 1
PUSH(0)        ; fw_buf		; 0

SPTR(0,4)
SPUT(0,5)
SGETB(0,2)
SPUT(0,2)
ldr 0,0,StrOffsetTable,1
SPUT(0,1)
SPUT(2,0)

;; End of FindWord Arg Stack Setup
SPTR(0,5)
CALLRO(SRFW, 0)



LOC 1350           ; Constants
#include "constants.inc.S"

LOC 1950           ; Stack
Stack: Data 0
