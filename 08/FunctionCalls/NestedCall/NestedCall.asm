@256
D=A
@SP
M=D
@BootStrap$ret.0      //Call BootStrap
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@5
D=A
@SP
D=M-D
@+0
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.init
0;JMP
(BootStrap$ret.0)
(Sys.init)   //FunctionDeclaration Sys.init
@R13
M=0
(Sys.init$LOADVARS)
@R13
D=M
@0
D=A-D
@Sys.init$END
D;JEQ
@SP
A=M
M=0
@SP
M=M+1
@R13
M=M+1
@Sys.init$LOADVARS
0;JMP
(Sys.init$END)
// constant 4000
@4000
D=A
@SP
A=M
M=D
@SP
M=M+1
// pointer 0
@SP
M=M-1
A=M
D=M
@THIS
M=D
// constant 5000
@5000
D=A
@SP
A=M
M=D
@SP
M=M+1
// pointer 1
@SP
M=M-1
A=M
D=M
@THAT
M=D
@Sys.init$ret.1      //Call Sys.init
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@5
D=A
@SP
D=M-D
@+0
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.main
0;JMP
(Sys.init$ret.1)
// temp 1
@1
D=A
@5
D=A+D
@R13
M=D
@SP
M=M-1
A=M
D=M
@R13
A=M
M=D
(Sys.init$LOOP)
@Sys.init$LOOP
0;JMP
(Sys.main)   //FunctionDeclaration Sys.main
@R13
M=0
(Sys.main$LOADVARS)
@R13
D=M
@5
D=A-D
@Sys.main$END
D;JEQ
@SP
A=M
M=0
@SP
M=M+1
@R13
M=M+1
@Sys.main$LOADVARS
0;JMP
(Sys.main$END)
// constant 4001
@4001
D=A
@SP
A=M
M=D
@SP
M=M+1
// pointer 0
@SP
M=M-1
A=M
D=M
@THIS
M=D
// constant 5001
@5001
D=A
@SP
A=M
M=D
@SP
M=M+1
// pointer 1
@SP
M=M-1
A=M
D=M
@THAT
M=D
// constant 200
@200
D=A
@SP
A=M
M=D
@SP
M=M+1
// local 1
@1
D=A
@LCL
D=M+D
@R13
M=D
@SP
M=M-1
A=M
D=M
@R13
A=M
M=D
// constant 40
@40
D=A
@SP
A=M
M=D
@SP
M=M+1
// local 2
@2
D=A
@LCL
D=M+D
@R13
M=D
@SP
M=M-1
A=M
D=M
@R13
A=M
M=D
// constant 6
@6
D=A
@SP
A=M
M=D
@SP
M=M+1
// local 3
@3
D=A
@LCL
D=M+D
@R13
M=D
@SP
M=M-1
A=M
D=M
@R13
A=M
M=D
// constant 123
@123
D=A
@SP
A=M
M=D
@SP
M=M+1
@Sys.main$ret.2      //Call Sys.main
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@5
D=A
@SP
D=M-D
@+1
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.add12
0;JMP
(Sys.main$ret.2)
// temp 0
@0
D=A
@5
D=A+D
@R13
M=D
@SP
M=M-1
A=M
D=M
@R13
A=M
M=D
// local 0
@0
D=A
@LCL
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// local 1
@1
D=A
@LCL
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// local 2
@2
D=A
@LCL
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// local 3
@3
D=A
@LCL
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// local 4
@4
D=A
@LCL
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
//add
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
M=D+M
@SP
M=M+1
//add
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
M=D+M
@SP
M=M+1
//add
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
M=D+M
@SP
M=M+1
//add
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
M=D+M
@SP
M=M+1
@LCL   //Return Sys.main
D=M
@R13
M=D
@5
A=D-A
D=M
@R14
M=D
@SP
M=M-1
A=M
D=M
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D
M=M+1
@R13
M=M-1
A=M
D=M
@THAT
M=D
@R13
M=M-1
A=M
D=M
@THIS
M=D
@R13
M=M-1
A=M
D=M
@ARG
M=D
@R13
M=M-1
A=M
D=M
@LCL
M=D
@R14
A=M
0;JMP
(Sys.add12)   //FunctionDeclaration Sys.add12
@R13
M=0
(Sys.add12$LOADVARS)
@R13
D=M
@0
D=A-D
@Sys.add12$END
D;JEQ
@SP
A=M
M=0
@SP
M=M+1
@R13
M=M+1
@Sys.add12$LOADVARS
0;JMP
(Sys.add12$END)
// constant 4002
@4002
D=A
@SP
A=M
M=D
@SP
M=M+1
// pointer 0
@SP
M=M-1
A=M
D=M
@THIS
M=D
// constant 5002
@5002
D=A
@SP
A=M
M=D
@SP
M=M+1
// pointer 1
@SP
M=M-1
A=M
D=M
@THAT
M=D
// argument 0
@0
D=A
@ARG
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// constant 12
@12
D=A
@SP
A=M
M=D
@SP
M=M+1
//add
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
M=D+M
@SP
M=M+1
@LCL   //Return Sys.add12
D=M
@R13
M=D
@5
A=D-A
D=M
@R14
M=D
@SP
M=M-1
A=M
D=M
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D
M=M+1
@R13
M=M-1
A=M
D=M
@THAT
M=D
@R13
M=M-1
A=M
D=M
@THIS
M=D
@R13
M=M-1
A=M
D=M
@ARG
M=D
@R13
M=M-1
A=M
D=M
@LCL
M=D
@R14
A=M
0;JMP
