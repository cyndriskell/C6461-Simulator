LOC 6     ; BEGIN AT LOCATION 6
Data 10   ; PUT 10 AT LOCATION 6
Data 3    ; PUT 3 AT LOCATION 7
Data End  ; PUT 1024 AT LOCATION 8
Data 0    ; PUT 0 AT LOCATION 9
Data 12   ; PUT 12 AT LOCATION 10
Data 9    ; PUT 9 AT LOCATION 11
Data 18   ; PUT 18 AT LOCATION 12
Data 12   ; PUT 12 AT LOCATION 13
LDX 2,7     ; X2 GETS 3
LDR 3,0,10 ; R3 GETS 12
LDR 2,2,10 ; R2 GETS 12
LDR 1,2,10,1 ; R1 GETS 18
LDA 0,2,10  ; R0 GETS EA OF 13
LDX 1,8     ; X1 GETS 1024
LDR 0,0,12 ; R0 GETS 18
STR 0,0,9  ; STORE R0 (18) AT LOCATION 9
STX 2,10 ; STORE X2 (3) AT LOCATION 13

LOC 1024
End: HLT     ; STOP

