import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class VMWriter {
	PrintWriter output;
	private String filename;
	// segments
	public static final String CONST = "constant ", ARG = "argument ", LOCAL = "local ", STATIC = "static ",
			THIS = "this ", THAT = "that ", POINTER = "pointer ", TEMP = "temp ";
	// ops:
	public static final String ADD = "add", SUB = "sub", NEG = "neg", EQ = "eq", GT = "gt", LT = "lt", AND = "and",
			OR = "or", NOT = "not";

	public VMWriter(File VMfile) {
		filename = VMfile.getName();
		filename = filename.substring(0, filename.length() - 3);
		try {
			output = new PrintWriter(VMfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void writePush(String segment, int index) {
		if (segment.equals("var")) {
			segment = LOCAL;
		}else if (segment.equals("arg")) {
			segment = ARG;
		}else if (segment.equals("field")) {
			segment = THIS;
		}else if (segment.equals("static")) {
			segment = STATIC;
		}
		output.println("push " + segment +  index);
	}

	public void writePop(String segment, int index) {
		if (segment.equals("var")) {
			segment = LOCAL;
		}else if (segment.equals("arg")) {
			segment = ARG;
		}else if (segment.equals("field")) {
			segment = THIS;
		}else if (segment.equals("static")) {
			segment = STATIC;
		}
		output.println("pop " + segment +  index);
	}

	public void writeArithmetic(String command) {
		if (command.equals("+"))
			output.println(ADD);
		else if (command.equals("-"))
			output.println(SUB);
		else if (command.equals("*"))
			output.println("call Math.multiply 2");
		else if (command.equals("/"))
			output.println("call Math.divide 2");
		else if (command.equals("neg"))
			output.println(NEG);
		else if (command.equals("="))
			output.println(EQ);
		else if (command.equals("<"))
			output.println(LT);
		else if (command.equals(">"))
			output.println(GT);
		else if (command.equals("&"))
			output.println(AND);
		else if (command.equals("|"))
			output.println(OR);
		else if (command.equals("~"))
			output.println(NOT);
		else
			output.println("command not found");
	}

	public void writeLabel(String label) {
		output.println("label " + label);
	}

	public void writeGoto(String label) {
		output.println("goto " + label);
	}

	public void writeIf(String label) {
		output.println("if-goto " + label);
	}

	public void writeCall(String name, int nArgs) {
		output.println("call " + name + " " + nArgs);
	}

	public void writeFunction(String name, int nLocals) {
		output.println("function " + filename + "." + name + " " + nLocals);
	}

	public void writeReturn() {
		output.println("return");
	}

	public void close() {
		output.close();
	}

}
