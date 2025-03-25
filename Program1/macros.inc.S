#define RTX(r,x) \
str r,0,1 ; macro RTX \
__NL__ ldx x,1 \
__NL__ ; end macro RTX

#define XTR(x,r) \
stx x,1 ; macro XTR \
__NL__ ldr r,0,1 \
__NL__ ; end macro XTR

#define XTX(x1,x2) \
stx x1,1 ; macro XTX \
__NL__ ldx x2,1 \
__NL__ ; end macro XTX

#define RTR(r1,r2) \
str r1,0,1 ; macro RTR \
__NL__ ldr r2,0,1 \
__NL__ ; end macro RTR

// clobbers r1
#define INCX(x) \
XTR(x,1) ; macro INCX \
__NL__ air 1,1 \
__NL__ RTX(1,x) \
__NL__ ; end macro INCX

// clobbers r1
#define DECX(x) \
XTR(x,1) ; macro DECX \
__NL__ sir 1,1 \
__NL__ RTX(1,x) \
__NL__ ; end macro DECX

#define LRI(r,i) \
ldr r,0,0 ; macro LRI \
__NL__ air r,i \
__NL__ ; end macro LRI
    
#define LRII(r,i) \
ldr r,0,0 ; macro LRII \
__NL__ ldr r,0,i,1 \
__NL__ ; end macro LRII

// clobbers r1
#define LXI(x,i) \
LRI(1,i) ; macro LXI \
__NL__ RTX(1,x) \
__NL__ ; end macro LXI
    
// clobbers r1
#define LXII(x,i) \
LRII(1,i) ; macro LXII \
__NL__ RTX(1,x) \
__NL__ ; end macro LXII
    
// clobbers r1, x1
#define JUMP(loc) \
LXI(1,loc) ; macro JUMP \
__NL__ jma 1,0,1 \
__NL__ ; end macro JUMP

// clobbers r1, x1
#define JUMPZ(r,loc) \
LXI(1,loc) ; macro JUMPZ \
__NL__ jz r,1,0,1 \
__NL__ ; end macro JUMPZ

// clobbers r1, x1
#define JUMPNZ(r,loc) \
LXI(1,loc) ; macro JUMPNZ \
__NL__ jne r,1,0,1 \
__NL__ ; end macro JUMPNZ

// clobbers r1, x1
#define JUMPGE(r,loc) \
LXI(1,loc) ; macro JUMPGE \
__NL__ jge r,1,0,1 \
__NL__ ; end macro JUMPGE

// Load index register with address at args[a]+offset
// clobbers r1
// output into x
#define LXAO(a,x,o) \
RTX(0,x) ; macro LXAO \
__NL__ lda 1,x,0,1 \
__NL__ air 1,a \
__NL__ RTX(1,x) \
__NL__ ldr 1,x,o \
__NL__ RTX(1,x) \
__NL__ ; end macro LXAO

// Add value at addr to value in x
// clobbers r1
#define AMX(x,addr) \
XTR(x,1); macro AMX \
__NL__ amr 1,0,addr \
__NL__ RTX(1,x) \
__NL__ ; end macro AMX

// Pushes value from register r onto stack
// clobbers r1, x1
#define PUSH(r) \
ldr 1,0,StackPtr ; macro PUSH \
__NL__ str r,0,StackPtr,1 \
__NL__ air 1,1 \
__NL__ str 1,0,StackPtr \
__NL__ ; end macro PUSH

// ldr 1,0,StackAddr ; macro PUSH \
// __NL__ amr 1,0,StackOffset \
// __NL__ str 1,0,1 \
// __NL__ str r,0,1,1 \
// __NL__ ldr 1,0,StackOffset \
// __NL__ air 1,1 \
// __NL__ str 1,0,StackOffset \
// __NL__ ; end macro PUSH

// Pops value from stack into register r
// clobbers r1, x1
#define POP(r) \
__NL__ ldr 1,0,StackPtr \
__NL__ sir 1,1 \
__NL__ str 1,0,StackPtr \
__NL__ ldr r,0,StackPtr,1 \
__NL__ ; end macro POP

// ldx 1,StackAddr ; macro POP \
// __NL__ ldr 1,0,StackOffset \
// __NL__ sir 1,1 \
// __NL__ str 1,0,StackOffset \
// __NL__ AMX(1,StackOffset) \
// __NL__ ldr r,1,0 \
// __NL__ ; end macro POP

// Sets stack ptr to StackPtr - c
#define POPC(c) \
__NL__ ldr 1,0,StackPtr ; macro POPC \
__NL__ sir 1,c \
__NL__ str 1,0,StackPtr \
__NL__ ; end macro POPC

// ldr 1,0,StackOffset ; macro POPC \
// __NL__ sir 1,c \
// __NL__ str 1,0,StackOffset \
// __NL__ ; end macro POPC

// Load from the stack into r with StackPtr - offset
#define SGET(r,o) \
ldr r,0,StackPtr ; macro SGET \
__NL__ sir r,1 \
__NL__ sir r,o \
__NL__ str r,0,1 \
__NL__ ldr r,0,1,1 \
__NL__ ; end macro SGET 

// ldr r,0,StackAddr ; macro SGET \
// __NL__ amr r,0,StackOffset \
// __NL__ sir r,1 \
// __NL__ sir r,o \
// __NL__ str r,0,1 \
// __NL__ ldr r,0,1,1 \
// __NL__ ; end macro SGET

// Load from the stack into r with StackBase + offset
#define SGETB(r,o) \
ldr r,0,StackBase ; macro SGETB \
__NL__ air r,o \
__NL__ str r,0,1 \
__NL__ ldr r,0,1,1 \
__NL__ ; end macro SGETB

// Store into the stack from r with StackPtr - offset
// clobbers r1
#define SPUT(r,o) \
ldr 1,0,StackPtr ; macro SPUT \
__NL__ sir 1,1 \
__NL__ sir 1,o \
__NL__ str 1,0,1 \
__NL__ str r,0,1,1 \
__NL__ ; end macro SPUT

// ldr 1,0,StackAddr ; macro SPUT \
// __NL__ amr 1,0,StackOffset \
// __NL__ sir 1,1 \
// __NL__ sir 1,o \
// __NL__ str 1,0,1 \
// __NL__ str r,0,1,1 \
// __NL__ ; end macro SPUT

// Store into the stack from r with StackBase + offset
// clobers r1
#define SPUTB(r,o) \
ldr 1,0,StackBase ; macro SPUTB \
__NL__ air 1,o \
__NL__ str 1,0,1 \
__NL__ str r,0,1,1 \
__NL__ ; end macro SPUTB

// Place StackPtr - offset into r
#define SPTR(r,o) \
ldr r,0,StackPtr ; macro SPTR \
__NL__ sir r,1 \
__NL__ sir r,o \
__NL__ ; end macro SPTR

// ldr r,0,StackAddr ; macro SPTR \
// __NL__ amr r,0,StackOffset \
// __NL__ sir r,1 \
// __NL__ sir r,o \
// __NL__ ; end macro SPTR

// Place StackBase + offset into r
#define SPTRB(r,o) \
ldr r,0,StackBase ; macro SPTRB \
__NL__ air r,o \
__NL__ str r,0,1 \
__NL__ ldr r,0,1,0 \
__NL__ ; end macro SPTRB

// Load register with address at args[a]+offset
// clobbers x1
#define LRAO(a,r,o) \
RTX(0,1) ; macro LRAO \
__NL__ lda r,1,0,1 \
__NL__ air r,a \
__NL__ RTX(r,1) \
__NL__ ldr r,1,o \
__NL__ ; end macro LRAO

#define CALL(routinePtr,argPtr) \
LRI(0,argPtr) ; macro CALL \
__NL__ LXI(1,routinePtr) \
__NL__ jsr 1,0,1 \
__NL__ ; end macro CALL

// #define CALLO(o,argPtr) \
// LRI(0,argPtr) ; macro CALLO \
// __NL__ LXI(1,routinePtr) \
// __NL__ jsr 1,0,1 \
// __NL__ ; end macro CALLO

#define CALLR(routinePtr,argReg) \
RTR(argReg,0) ; macro CALLR \
__NL__ LXI(1,routinePtr) \
__NL__ jsr 1,0,1 \
__NL__ ; end macro CALLR

// clobbers r1, x1
#define CALLRO(routineOffset, argReg) \
RTR(argReg,0) ; macro CALLRO \
__NL__ ldr 1,0,SubroutineOffsetTable \
__NL__ air 1,routineOffset \
__NL__ str 1,0,1 \
__NL__ ldr 1,0,1,1 \
__NL__ str 1,0,1 \
__NL__ jsr 0,1,1 \
__NL__ ; end macro CALLRO 

// add r1 to r2
#define ARR(r1,r2) \
str r1,0,1 ; macro ARR \
__NL__ amr r2,0,1 \
__NL__ ; end macro ARR

// add r to x
// clobbers r1
#define ARX(r,x) \
stx x,1 ; macro ARX \
__NL__ RTR(r,1) \
__NL__ amr 1,0,1 \
__NL__ RTX(1,x) \
__NL__ ; end macro ARX

// get data from stack with offset in r2 into r1
// r1 = content of (content of (StackOffset - offset) + r2)
// r1 = *(*(SP-o) + r2)
#define SGETI(r1,o,r2) \
SGET(r1,o) ; macro SGETI \
__NL__ ARR(r2,r1) \
__NL__ str r1,0,1 \
__NL__ ldr r1,0,1,1 \
__NL__ ; end macro SGETI

// put data into stack with offset in r2 from r1
// content of (content of (StackOffset - offset) + r2 = r1
// *(*(SP-o) + r2) = r1
// clobers actual register 1
#define SPUTI(r1,o,r2) \
SGET(1,o) ; macro SPUTI \
__NL__ ARR (r2,1) \
__NL__ str 1,0,1 \
__NL__ str r1,0,1,1 \
__NL__ ; end macro SPUTI

// copy SP to r, then add c to SP
#define SALLOC(r,c) \
ldr r,0,StackPtr ; macro SALLOC \
__NL__ air r,c \
__NL__ str r,0,StackPtr \
__NL__ sir r,c \
__NL__ ; end macro SALLOC


//ldr r,0,StackAddr ; MACRO SALLOC \
//__NL__ ldr 1,0,StackOffset \
//__NL__ ARR(1,r) \
//__NL__ air 1,c \
//__NL__ str 1,0,StackOffset \


// Constants
#define HYPHEN_1 20
#define HYPHEN_2 25
#define ASCII_HALF_NUM_OFFSET 24

#define SRPRINT 0
#define SRGNTM 1
#define SRPARSE 2
#define SRSTRLEN 3
#define SRPOW 4
#define SRPRINTN 5