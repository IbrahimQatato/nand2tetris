import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CompilationEngine {
	Scanner input;
	PrintWriter output;
	JackTokenizer tokenizer;
	SymbolTable symTable;
	VMWriter writer;
	private int ifIndex = 0;
	private int loopIndex = 0;
	private String filename;

	public CompilationEngine(File jackFile, File VMfile, File XMLfile) {
		filename = jackFile.getName();
		filename = filename.substring(0, filename.length() - 5);
		tokenizer = new JackTokenizer(jackFile);
		symTable = new SymbolTable();
		writer = new VMWriter(VMfile);
		try {
			output = new PrintWriter(XMLfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		tokenizer.advance();
	}

	public void compileClass() throws Exception {// this one should close the output in the end
		output.println("<class>");
		this.eat("class");
		this.output.print("class: ");
		this.eatIdentifier();// <----------------------------- identifier is always a className
		this.eat("{");
		while (this.getToken().equals("static") || this.getToken().equals("field")) {
			this.compileClassVarDec();
		}
		while (this.getToken().equals("method") || this.getToken().equals("function")
				|| this.getToken().equals("constructor")) {
			this.compileSubroutineDec();
		}
		this.eat("}");
		writer.close();
		output.println("</class>");
		// while (tokenizer.hasMoreTokens())// in case EOF is not reached
		// tokenizer.advance();
		output.close();

	}

	public void compileClassVarDec() throws Exception {
		String kind;
		String type;
		output.println("<classVarDec>");
		try {
			this.eat("field");
			kind = "field";
		} catch (Exception e) {
			this.eat("static");
			kind = "static";
		}
		type = this.getToken();
		this.eatType();
		symTable.define(this.getToken(), type, kind);
		this.output.print(symTable.kindOf(this.getToken()) + "(def) --index--> " + symTable.indexOf(this.getToken()));
		this.eatIdentifier();
		while (this.getToken().equals(",")) {
			this.eat(",");
			symTable.define(this.getToken(), type, kind);
			this.output
					.print(symTable.kindOf(this.getToken()) + "(def) --index--> " + symTable.indexOf(this.getToken()));
			this.eatIdentifier();
		}
		this.eat(";");
		output.println("</classVarDec>");

	}

	public void compileSubroutineDec() throws Exception {
		boolean isVoid = false;
		boolean isCons = false;
		boolean isMeth = false;
		symTable.startSubroutine();
		output.println("<subroutineDec>");
		try {
			this.eat("method");
			isMeth = true;
		} catch (Exception e) {
			try {
				this.eat("function");
			} catch (Exception f) {
				this.eat("constructor");
				isCons = true;
			}
		}
		try {// ('void'|type)
			this.eat("void");
			isVoid = true;
		} catch (Exception e) {
			this.eatType();
		}

		this.output.print("subroutine: ");
		String func = this.getToken();
		this.eatIdentifier();
		this.eat("(");
		this.compileParameterList(isMeth);// ------------------->all the args
		this.eat(")");
		this.compileSubroutineBody(func, isCons, isMeth);// ------------------->includes locals
		if (isVoid)
			writer.writePush(VMWriter.CONST, 0);
		/*
		 * else if (isCons) writer.writePush(VMWriter.POINTER, 0);
		 */
		writer.writeReturn();
		output.println("</subroutineDec>");
	}

	public void compileParameterList(boolean isMeth) throws Exception {
		if(isMeth)
			symTable.argC++;//needs revising
		output.println("<parameterList>");
		if (!(this.getToken().equals(")"))) {
			String type = this.getToken();
			this.eatType();
			symTable.define(this.getToken(), type, "arg");
			this.output
					.print(symTable.kindOf(this.getToken()) + "(def) --index--> " + symTable.indexOf(this.getToken()));
			this.eatIdentifier();
			while (this.getToken().equals(",")) {
				this.eat(",");
				this.eatType();
				symTable.define(this.getToken(), type, "arg");
				this.output.print(
						symTable.kindOf(this.getToken()) + "(def) --index--> " + symTable.indexOf(this.getToken()));

				this.eatIdentifier();
			}
		}
		output.println("</parameterList>");
	}

	public void compileSubroutineBody(String func, boolean isCons, boolean isMeth) throws Exception {
		int varCount = 0;
		output.println("<subroutineBody>");
		this.eat("{");
		while (this.getToken().equals("var")) {
			varCount += this.compileVarDec();// --------------------->LCL VARS
		}
		writer.writeFunction(func, varCount);
		if (isCons) {
			writer.writePush(VMWriter.CONST, symTable.varCount(SymbolTable.FIELD));
			writer.writeCall("Memory.alloc", 1);
			writer.writePop(VMWriter.POINTER, 0);
		} else if (isMeth) {
			writer.writePush(VMWriter.ARG, 0);
			writer.writePop(VMWriter.POINTER, 0);
		}
		this.compileStatements();
		this.eat("}");
		output.println("</subroutineBody>");

	}

	public int compileVarDec() throws Exception {
		int count = 0;
		output.println("<varDec>");
		this.eat("var");
		String type = this.getToken();
		this.eatType();
		symTable.define(this.getToken(), type, "var");
		this.output.print(symTable.kindOf(this.getToken()) + " (def)--index--> " + symTable.indexOf(this.getToken()));
		this.eatIdentifier();
		count++;
		while (this.getToken().equals(",")) {
			this.eat(",");
			count++;
			symTable.define(this.getToken(), type, "var");
			this.output
					.print(symTable.kindOf(this.getToken()) + " (def)--index--> " + symTable.indexOf(this.getToken()));
			this.eatIdentifier();
		}
		this.eat(";");
		output.println("</varDec>");
		return count;

	}

	public void compileStatements() throws Exception {
		output.println("<statements>");// ----------------------> symbols are used in all statements
		while (this.getToken().equals("let") || this.getToken().equals("if") || this.getToken().equals("while")
				|| this.getToken().equals("do") || this.getToken().equals("return")) {
			if (this.getToken().equals("let"))
				this.compileLet();
			if (this.getToken().equals("if"))
				this.compileIf();
			if (this.getToken().equals("while"))
				this.compileWhile();
			if (this.getToken().equals("do"))
				this.compileDo();
			if (this.getToken().equals("return"))
				this.compileReturn();
		}
		output.println("</statements>");

	}

	public void compileLet() throws Exception {
		boolean isArr = false;
		output.println("<letStatement>");
		this.eat("let");
		String name = this.getToken();
		this.output.print(symTable.kindOf(this.getToken()) + " --index--> " + symTable.indexOf(this.getToken())
				+ "type-----" + symTable.typeOf(this.getToken()));
		this.eatIdentifier();
		if (this.getToken().equals("[")) {
			writer.writePush(symTable.kindOf(name), symTable.indexOf(name));
			this.eat("[");
			this.compileExpression();
			this.eat("]");
			writer.writeArithmetic("+");
			isArr = true;
		}
		this.eat("=");
		this.compileExpression();
		if (isArr) {
			writer.writePop(VMWriter.TEMP, 0);
			writer.writePop(VMWriter.POINTER, 1);
			writer.writePush(VMWriter.TEMP, 0);
			writer.writePop(VMWriter.THAT, 0);
		} else {
			writer.writePop(symTable.kindOf(name), symTable.indexOf(name));
		}
		this.eat(";");
		output.println("</letStatement>");
	}

	private void compileIf() throws Exception {
		int index = ifIndex++;
		output.println("<ifStatement>");
		this.eat("if");
		this.eat("(");
		this.compileExpression();
		this.eat(")");
		writer.writeArithmetic("~");
		writer.writeIf("FALSE." + index);
		this.eat("{");
		this.compileStatements();
		writer.writeGoto("CONTINUE." + index);
		this.eat("}");
		writer.writeLabel("FALSE." + index);
		if (this.getToken().equals("else")) {
			this.eat("else");
			this.eat("{");
			this.compileStatements();
			this.eat("}");
		}
		writer.writeLabel("CONTINUE." + index);
		output.println("</ifStatement>");
	}

	private void compileWhile() throws Exception {
		int index = loopIndex++;
		output.println("<whileStatement>");
		writer.writeLabel("BEGINLOOP" + index);
		this.eat("while");
		this.eat("(");
		this.compileExpression();
		this.eat(")");
		writer.writeArithmetic("~");
		writer.writeIf("ENDLOOP." + index);
		this.eat("{");
		this.compileStatements();
		this.eat("}");
		writer.writeGoto("BEGINLOOP" + index);
		writer.writeLabel("ENDLOOP." + index);
		output.println("</whileStatement>");
	}

	private void compileDo() throws Exception {
		int count = 0;
		String routineName;
		boolean cName = true;
		output.println("<doStatement>");
		this.eat("do");
		if (!(this.symTable.kindOf(getToken()).equals(SymbolTable.NONE))) {
			this.output.print(symTable.kindOf(this.getToken()) + " --index--> " + symTable.indexOf(this.getToken()));
			cName = false;
		}
		routineName = this.getToken();
		this.eatIdentifier();
		if (this.getToken().equals("(")) {
			this.output.print("-----subroutine: ");// -----------> always a method
			writer.writePush(VMWriter.POINTER, 0);
			count++;
			this.eat("(");
			count += this.compileExpressionList();
			this.eat(")");
			routineName = filename + "." + routineName;
			
			writer.writeCall(routineName, count);
			writer.writePop(VMWriter.TEMP, 0);
		} else {
			if (cName) {
				this.output.print("------class");
			} else {
				writer.writePush(symTable.kindOf(routineName), symTable.indexOf(routineName));
				count++;
				routineName = symTable.typeOf(routineName);
			}
			this.eat(".");
			routineName = routineName + "." + this.getToken();
			this.output.print("subroutine: ");
			this.eatIdentifier();
			this.eat("(");
			count += this.compileExpressionList();
			this.eat(")");
			writer.writeCall(routineName, count);
			writer.writePop(VMWriter.TEMP, 0);
		}
		this.eat(";");
		output.println("</doStatement>");

	}

	private void compileReturn() throws Exception {
		output.println("<returnStatement>");
		this.eat("return");
		if (!(this.getToken().equals(";"))) {
			this.compileExpression();
		}
		this.eat(";");
		output.println("</returnStatement>");
	}

	private int compileExpressionList() throws Exception {// needs to handle the THIS keyword
		int count = 0;
		output.println("<expressionList>");
		if (!(this.getToken().equals(")"))) {
			this.compileExpression();
			count++;
		}
		while (this.getToken().equals(",")) {
			this.eat(",");
			count++;
			this.compileExpression();
		}
		output.println("</expressionList>");
		return count;

	}

	private void compileExpression() throws Exception {
		output.println("<expression>");
		this.compileTerm();
		while (this.getToken().equals("/") || this.getToken().equals("*") || this.getToken().equals("&")
				|| this.getToken().equals("|") || this.getToken().equals("<") || this.getToken().equals(">")
				|| this.getToken().equals("=") || this.getToken().equals("-") || this.getToken().equals("+")) {
			String biOp = this.getToken();
			output.println(tokenizer.output());// to be replaced with more reliable code
			tokenizer.advance();
			this.compileTerm();
			writer.writeArithmetic(biOp);
		}
		output.println("</expression>");
	}

	public void compileTerm() throws Exception {
		boolean cName = false;
		output.println("<term>");
		if (!(this.symTable.kindOf(getToken()).equals(SymbolTable.NONE))) {
			this.output.print(symTable.kindOf(this.getToken()) + " --index--> " + symTable.indexOf(this.getToken()));
			writer.writePush(symTable.kindOf(this.getToken()), symTable.indexOf(this.getToken()));
		} else if (tokenizer.tokenType() == JackTokenizer.INT_CONST) {
			writer.writePush(VMWriter.CONST, Integer.parseInt(this.getToken()));
		} else if (tokenizer.tokenType() == JackTokenizer.KEYWORD) {
			if (this.getToken().equals(JackTokenizer.FALSE) || this.getToken().equals(JackTokenizer.NULL)
					|| this.getToken().equals(JackTokenizer.TRUE)) {
				writer.writePush(VMWriter.CONST, 0);
			}
			if (this.getToken().equals(JackTokenizer.TRUE)) {
				writer.writeArithmetic("~");
			} // THIS is the remaining const keyword ^|^
			if (this.getToken().equals(JackTokenizer.THIS)) {
				writer.writePush(VMWriter.POINTER, 0);
			}
		} else if (tokenizer.tokenType() == JackTokenizer.STRING_CONST) {
			String string = this.tokenizer.stringVal();
			writer.writePush(VMWriter.CONST, string.length());
			writer.writeCall("String.new", 1);
			for (int i = 0; i < string.length(); i++) {
				writer.writePush(VMWriter.CONST, (int)string.charAt(i));
				writer.writeCall("String.appendChar", 2);
			}
		} else {
			cName = true;
		}
		if (this.getToken().equals("-") || this.getToken().equals("~")) {
			String unOp = this.getToken();

			output.println(tokenizer.output());// eats a unary operator(symbol)
			tokenizer.advance();
			this.compileTerm();
			if (unOp.equals("-"))
				writer.writeArithmetic("neg");
			else {
				writer.writeArithmetic("~");
			}
		} else if (this.getToken().equals("(")) {
			this.eat("(");
			this.compileExpression();// ----------------->should be given priority
			this.eat(")");
		} else {
			int nArgs = 0;
			String routineName = this.getToken();
			output.println(tokenizer.output());
			tokenizer.advance();
			String next = this.getToken();
			if (next.equals("[")) {// ---->add array access in VM
				this.eat("[");
				this.compileExpression();
				this.eat("]");
				writer.writeArithmetic("+");
				writer.writePop(VMWriter.POINTER, 1);
				writer.writePush(VMWriter.THAT, 0);
				
			} else if (next.equals(".") || next.equals("(")) {
				if (this.getToken().equals("(")) {
					this.output.print("subroutine: ");// --->might need some work when compiling the Pong code
					this.eat("(");
					this.compileExpressionList();
					this.eat(")");
				} else {

					if (cName) {
						this.output.print("--------class");// --->add fun call here in VM
					} else {
						writer.writePush(symTable.kindOf(routineName), symTable.indexOf(routineName));
						nArgs++;
						routineName = symTable.typeOf(routineName);
					}
					this.eat(".");
					routineName = routineName + "." + this.getToken();
					this.eatIdentifier();
					this.output.print("subroutine: ");
					this.eat("(");
					nArgs += this.compileExpressionList();
					this.eat(")");
					writer.writeCall(routineName, nArgs);
				}
			}
		}
		output.println("</term>");
	}

	private void eat(String s) throws Exception {
		if (!(this.getToken().equals(s)))
			throw new Exception("element not found");
		else {
			output.println(tokenizer.output());
			if (tokenizer.hasMoreTokens())// remove this to check whether EOF is reached
				tokenizer.advance();
		}
	}

	private String getToken() {
		return tokenizer.currToken;
	}

	private void eatIdentifier() throws Exception {
		if (tokenizer.tokenType() != JackTokenizer.IDENTIFIER)
			throw new Exception("no identifier");
		output.println(tokenizer.output());
		tokenizer.advance();
	}

	private void eatType() throws Exception {
		try {
			this.eat("int");
		} catch (Exception e) {
			try {
				this.eat("char");
			} catch (Exception f) {
				try {
					this.eat("boolean");
				} catch (Exception g) {
					this.output.print("class: ");
					this.eatIdentifier();
				}
			}
		}
	}

}
