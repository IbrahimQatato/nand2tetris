// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.
@R0
D=M
@n
M=D			//n = R0

@R1
D=M
@multiplied
M=D			//multiplied = R1

@sum
M=0			// sum initialized to 0

(LOOP)
@n
D=M
@STOP
D;JEQ		//If n = 0 stop

@multiplied
D=M
@sum
M=M+D		//sum = sum+multiplied

@n
M=M-1		//n--
@LOOP
0;JMP

(STOP)
@sum
D=M
@R2
M=D

(END)
@END
0;JMP