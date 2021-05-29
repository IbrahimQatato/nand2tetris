import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CodeWriter {
	private PrintWriter output;
	private String filename;
	private int branchC = 0;
	private int returnC = 0;
	private String functionName = "";
	public CodeWriter(File f) {
		filename = f.getName();
		if (filename.substring(filename.length()-3, filename.length()).equals("asm")) {
			filename = filename.substring(0, filename.length()-4);
		}
		try {
			System.out.println(f.getAbsolutePath());
			output = new PrintWriter(f.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	public void setFileName(String fileName) {
		this.filename = fileName;
	}
	//TODO: SP = 256
	//Call Sys.init (should be written in Assembly)
	public void writeInit() {
		output.print("@256\n"+
				"D=A\n"+
				"@SP\n"+
				"M=D\n");
		this.functionName = "BootStrap";
		writeCall("Sys.init", 0);
	}

	public void writeArithmetic(Parser p) throws Exception {
		if (p.commandType() != 1)
			throw new Exception();
		String command = p.arg1();
		output.println("//"+command);
		output.print("@SP\n" + "M=M-1\n" + "A=M\n");// POP 1ST
		if (command.equals("neg") || command.equals("not")) {
			output.print("M=-M\n"); // negate 2's complement
			if (command.equals("not")) {
				output.println("M=M-1");// 1's complement
			}
			output.print("@SP\n" + "M=M+1\n");// SP++
			return;
		}
		output.print("D=M\n" + "@SP\n" + "M=M-1\n" + "A=M\n");// POP 2ND
		if(command.equals("add")) {
			output.print("M=D+M\n");
		}else if(command.equals("sub")){
			output.print("M=M-D\n");
		}else if(command.equals("and")){
			output.print("M=M&D\n");
		}
		else if(command.equals("or")){
			output.print("M=M|D\n");
		}else {
			output.print("D=M-D;\n"+
					"@TRUE."+branchC+"\n");
			if (command.equals("eq")) {
				output.println("D;JEQ");									
			}else if(command.equals("gt")){
				output.println("D;JGT");
			}else if(command.equals("lt")){ 
				output.println("D;JLT");
			}
			else {
				output.println("can't read");
				}
			output.print("@SP\n"+
						"A=M\n"+
						"M=0\n"+
						"@CONTINUE."+branchC+"\n"+
						"0;JMP\n"+
						"(TRUE."+branchC+")\n"+
						"@SP\n"+
						"A=M\n"+
						"M=-1\n"+
						"(CONTINUE."+branchC+")\n");
			branchC++;
		}
		output.print("@SP\n" + "M=M+1\n");// SP++
	}
	
	public void writePushPop(int command, String arg1, int arg2)throws Exception{
		if (!(command==2 || command == 3)) {
			throw new Exception();
		}
		output.println("//"+	 " "+arg1+" "+arg2);
		if(command==2) {
			if(arg1.equals("this")||arg1.equals("that")||arg1.equals("local")||arg1.equals("argument")) {
				switch (arg1) {
				case "this":
				case "that":
					arg1=arg1.toUpperCase();
					break;
				case "local":
					arg1 = "LCL";
					break;
				default:
					arg1 = "ARG";
					break;
				}
				output.print("@"+arg2+"\n"+
						"D=A\n"+//similar to constant
						"@"+arg1+"\n"+
						"D=M+D\n");//address = segment+i
				output.print("A=D\n"+
						"D=M\n");//choose D to equal value @address		
			}else if(arg1.equals("constant")){
				output.print("@"+arg2+"\n"+
						"D=A\n");
			}else if(arg1.equals("static")) {
				output.print("@"+filename+"."+arg2+"\n"+
						"D=M\n");
				//statics.put(arg2, staticC);
				//staticC++;
			}else if(arg1.equals("temp")) {
				output.print("@"+arg2+"\n"+
						"D=A\n"+//similar to constant
						"@5"+"\n"+
						"D=A+D\n");//address = 5+i
				output.print("A=D\n"+
						"D=M\n");//choose D to equal value @address
			}else {//pointer
				String tort;
				tort = arg2==0 ? "THIS" : "THAT";
				output.print("@"+tort+"\n"+
						"D=M\n");
			}
			output.print("@SP\n"+
					"A=M\n"+
					"M=D\n"+
					"@SP\n"+
					"M=M+1\n");//push D onto stack
		}else {
			if(arg1.equals("this")||arg1.equals("that")||arg1.equals("local")||arg1.equals("argument")) {///HAY FOR POP
				switch (arg1) {
				case "this":
				case "that":
					arg1=arg1.toUpperCase();
					break;
				case "local":
					arg1 = "LCL";
					break;
				default:
					arg1 = "ARG";
					break;
				}
				output.print("@"+arg2+"\n"+
						"D=A\n"+
						"@"+arg1+"\n"+
						"D=M+D\n");//address = segment+i
				output.print("@R13\n"+
						"M=D\n");//store address in R13
				output.print("@SP\n"+
						"M=M-1\n"+
						"A=M\n"+
						"D=M\n");//pop SP
				output.print("@R13\n"+
						"A=M\n"+
						"M=D\n");//address contains popped value
			}else if(arg1.equals("static")) {
				output.print("@SP\n"+
						"M=M-1\n"+
						"A=M\n"+
						"D=M\n");//pop SP
				output.print("@"+filename+"."+arg2+"\n"+
						"M=D\n");
			}else if(arg1.equals("temp")) {
				output.print("@"+arg2+"\n"+
						"D=A\n"+
						"@5"+"\n"+
						"D=A+D\n");//address = 5+i
				output.print("@R13\n"+
						"M=D\n");//store address in R13
				output.print("@SP\n"+
						"M=M-1\n"+
						"A=M\n"+
						"D=M\n");//pop SP
				output.print("@R13\n"+
						"A=M\n"+
						"M=D\n");//address contains popped value
			}else {//pointer
				String tort;
				tort = arg2==0 ? "THIS" : "THAT";
				output.print("@SP\n"+
						"M=M-1\n"+
						"A=M\n"+
						"D=M\n");//pop SP
				output.print("@"+tort+"\n"+
						"M=D\n");//THIS/THAT= *SP
			}
		}
	}
	
	public void writeLabel(String label) {
		if (functionName.equals(""))	
			output.println("("+filename+"$"+label+")");
		else
			output.println("("+functionName+"$"+label+")");
	}
	public void writeGoto(String label) {
		if (functionName.equals(""))	
			output.println("@"+filename+"$"+label);
		else
			output.println("@"+functionName+"$"+label);
		output.println("0;JMP");
	}
	public void writeIf(String label) {
		output.print("@SP\n"+
				"M=M-1\n"+
				"A=M\n"+
				"D=M\n");
		if (functionName.equals(""))	
			output.println("@"+filename+"$"+label);
		else
			output.println("@"+functionName+"$"+label);
		output.println("D;JNE");
	}
	public void writeFunction(String functionName, int numVars) {
		this.functionName = functionName;
		output.println("("+functionName+")"+"   //FunctionDeclaration "+this.functionName);
		output.print("@R13\n"+
				"M=0\n");
		output.print("("+functionName+"$LOADVARS)\n"+
				"@R13\n"+
				"D=M\n"+
				"@"+numVars+"\n"+
				"D=A-D\n"+//changed it to A
				"@"+functionName+"$END\n"+
				"D;JEQ\n"+
				"@SP\n"+
				"A=M\n"+
				"M=0\n"+
				"@SP\n"+
				"M=M+1\n"+
				"@R13\n"+
				"M=M+1\n"+
				"@"+functionName+"$LOADVARS\n"+
				"0;JMP\n"+
				"("+functionName+"$END)\n");
	}
	public void writeCall(String functionName, int numArgs) {
		String rAddress = this.functionName+"$ret."+returnC++;
		SPpushReturn(rAddress, functionName);// push returnAddress
		SPpushD("LCL");
		SPpushD("ARG");
		SPpushD("THIS");
		SPpushD("THAT");
		output.print("@5\n"+//new ARG pointer
				"D=A\n"+
				"@SP\n"+
				"D=M-D\n"+
				"@"+numArgs+"\n"+
				"D=D-A\n"+
				"@ARG\n"+
				"M=D\n");
		output.print("@SP\n"+//new LCL pointer
				"D=M\n"+
				"@LCL\n"+
				"M=D\n");
		output.print("@"+functionName+"\n");//goto function// changed from label to address
		output.println("0;JMP");
		output.println("("+rAddress+")");//return address label declaration
	}
	public void writeReturn() {
		output.print("@LCL   //Return "+this.functionName+"\n"+
				"D=M\n"+
				"@R13\n"+
				"M=D\n"+
				"@5\n"+
				"A=D-A\n"+//it was M
				"D=M\n"+
				"@R14\n"+
				"M=D\n");
		output.print("@SP\n"+//pop ARG	
				"M=M-1\n"+
				"A=M\n"+
				"D=M\n"+
				"@ARG\n"+
				"A=M\n"+
				"M=D\n"+
				"@ARG\n"+//SP=ARG+1
				"D=M\n"+
				"@SP\n"+
				"M=D\n"+
				"M=M+1\n");
		output.print("@R13\n"+
				"M=M-1\n"+
				"A=M\n"+
				"D=M\n"+
				"@THAT\n"+
				"M=D\n"+
				"@R13\n"+
				"M=M-1\n"+
				"A=M\n"+
				"D=M\n"+
				"@THIS\n"+
				"M=D\n"+
				"@R13\n"+
				"M=M-1\n"+
				"A=M\n"+
				"D=M\n"+
				"@ARG\n"+
				"M=D\n"+
				"@R13\n"+
				"M=M-1\n"+
				"A=M\n"+
				"D=M\n"+
				"@LCL\n"+
				"M=D\n"+
				"@R14\n"+
				"A=M\n"+
				"0;JMP\n");
	}
	public void close() {
		output.close();
	}
	private void SPpushD(String address) {
		output.print("@"+address+"\n"+
				"D=M\n"+
				"@SP\n"+
				"A=M\n"+
				"M=D\n"+
				"@SP\n"+
				"M=M+1\n");
	}
	private void SPpushReturn(String address, String func) {
		output.print("@"+address+"      //Call "+func+"\n"+
				"D=A\n"+
				"@SP\n"+
				"A=M\n"+
				"M=D\n"+
				"@SP\n"+
				"M=M+1\n");
	}
}
