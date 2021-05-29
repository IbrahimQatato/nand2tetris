import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {
	static final int C_ARITHMETIC = 1;
	static final int C_PUSH = 2;
	static final int C_POP = 3;
	static final int C_LABEL = 4;
	static final int C_GOTO = 5;
	static final int C_IF = 6;
	static final int C_FUNCTION = 7;
	static final int C_RETURN = 8;
	static final int C_CALL = 9;
	String command;// make private later
	private Scanner input;

	public Parser(File VMfile) {
		// TODO open Input stream
		try {
			input = new Scanner(VMfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean hasMoreCommands() {
		if (input.hasNextLine()) {
			return true;
		} else {
			input.close();
		}
		return false;
	}

	public void advance() {
		if (this.hasMoreCommands()) {
			command = input.nextLine();
			while (command.isEmpty() || command.substring(0, 2).equals("//")) {
				command = input.nextLine();

			}
		}
	}

	public int commandType() {
		String[] constituents = command.split(" ");
		if (constituents[0].equals("return"))
			return C_RETURN;
		else if (constituents[0].equals("push"))
			return C_PUSH;
		else if (constituents[0].equals("pop"))
			return C_POP;
		else if (constituents[0].equals("goto"))
			return C_GOTO;
		else if (constituents[0].equals("if-goto"))
			return C_IF;
		else if (constituents[0].equals("label"))
			return C_LABEL;
		else if (constituents[0].equals("call"))
			return C_CALL;
		else if (constituents[0].equals("function"))
			return C_FUNCTION;
		else
			return C_ARITHMETIC;
		
	}

	public String arg1() throws Exception {
		int type = this.commandType();
		if (type == C_RETURN)
			throw new Exception();// TODO might need to raise exception
		else if (type == C_ARITHMETIC)
			return command.split(" ")[0];
		else {
			String[] tings = command.split(" ");
			return tings[1];
		}
	}

	public int arg2() throws Exception {
		int type = this.commandType();
		if (!(type == C_POP || type == C_PUSH || type == C_FUNCTION || type == C_CALL))
			throw new Exception();// TODO might need to raise exception
		else {
			String[] tings = command.split(" ");
			return Integer.parseInt(tings[2]);
		}
	}
}
