import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class JackTokenizer {
	Scanner input;
	ArrayList<String> symbols;
	ArrayList<String> keywords;
	private boolean validInput;
	private String currString;
	String currToken;
	private char nextChar;
	private int index;
	public static final int KEYWORD = 1, SYMBOL = 2, IDENTIFIER = 3, INT_CONST = 4, STRING_CONST = 5;
	public static final String CLASS = "class", CONSTRUCTOR = "constructor", FUNCTION = "function", METHOD = "method",
			FIELD = "field", STATIC = "static", VAR = "var", INT = "int", CHAR = "char", BOOLEAN = "boolean",
			VOID = "void", TRUE = "true", FALSE = "false", NULL = "null", THIS = "this", LET = "let", DO = "do",
			IF = "if", ELSE = "else", WHILE = "while", RETURN = "return";

	public JackTokenizer(File jackFile) {
		// TODO open Input stream
		index = 0;
		currToken = "";
		String[] temp = { "{", "}", "(", ")", "[", "]", ".", ",", ";", "+", "-", "/", "*", "&", "|", "<", ">", "=",
				"~" };
		symbols = new ArrayList<String>(Arrays.asList(temp));
		String[] temp2 = { CLASS, CONSTRUCTOR, FUNCTION, METHOD, FIELD, STATIC, VAR, INT, CHAR, BOOLEAN, VOID, TRUE,
				FALSE, NULL, THIS, LET, DO, IF, ELSE, WHILE, RETURN };
		keywords = new ArrayList<String>(Arrays.asList(temp2));
		try {
			input = new Scanner(jackFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean hasMoreTokens() {
		if (input.hasNext() || index != 0)
			return true;
		input.close();
		return false;
	}

	public void advance() {
		currToken = "";
		do {
			if (index == 0 || index == currString.length()) {
				currString = input.next();
			}
			if (currString.length() > 1 && currString.substring(0, 2).equals("//")) {
				currString = input.nextLine();
				validInput = false;
				continue;
			}
			if (currString.length() > 2 && currString.substring(0, 3).equals("/**")) {
				validInput = false;
				while (!validInput) {
					if (currString.length() >= 2
							&& currString.substring(currString.length() - 2, currString.length()).equals("*/"))
						validInput = true;
					currString = input.next();
				}
			}
			validInput = true;
		} while (!validInput);
		while ((index != currString.length())) {
			nextChar = currString.charAt(index);
			if (this.isSymbol(nextChar)) {
				if (currToken.isEmpty()) {
					currToken = nextChar + "";
					index++;
				}
				break;
			}
			if(nextChar=='\"') {
				currToken = nextChar+"";
				index++;
				while(currToken.length()==1||currToken.charAt(currToken.length()-1)!='\"') {
					if (index==currString.length()) {
						index = 0;
						currToken = currToken+" ";
						currString = input.next();
					}
					nextChar = currString.charAt(index);
					if(nextChar=='\"') {
						currToken = currToken +nextChar;
						index++;
						break;
					}
					currToken = currToken + nextChar;
					index++;
				}
				break;
			}
			currToken = currToken + nextChar;
			index++;
		}
		if (index == currString.length())
			index = 0;
		System.out.println(currToken);

	}

	public int tokenType() {
		if (symbols.contains(currToken))
			return SYMBOL;
		else if (keywords.contains(currToken))
			return KEYWORD;
		else if (currToken.charAt(0) == '\"')
			return STRING_CONST;
		else if (currToken.charAt(0) <= '9' && currToken.charAt(0) >= '0')
			return INT_CONST;
		else
			return IDENTIFIER;
	}

	public String keyword() {
		return keywords.get(keywords.indexOf(currToken));
	}

	public char symbol() {
		return currToken.charAt(0);
	}

	public String identifier() {
		return currToken;
	}

	public int intVal() {
		return Integer.parseInt(currToken);
	}

	public String stringVal() {
		return currToken.substring(1, currToken.length() - 1);
	}

	private boolean isSymbol(char c) {
		return symbols.contains(c + "");
	}

	public String output() {
		int type = this.tokenType();
		if (type == IDENTIFIER) {
			return "<identifier> " + this.identifier() + " </identifier>";
		} else if (type == KEYWORD) {
			return "<keyword> " + this.keyword() + " </keyword>";
		} else if (type == SYMBOL) {
			char symbol = currToken.charAt(0);
			String symval;
			if (symbol == '<')
				symval = "&lt;";
			else if (symbol == '>')
				symval = "&gt;";
			else if (symbol == '\"')
				symval = "&quot;";
			else if (symbol == '&')
				symval = "&amp;";
			else {
				symval = symbol + "";
			}
			return "<symbol> " + symval + " </symbol>";
		} else if (type == INT_CONST) {
			return "<integerConstant> " + this.intVal() + " </integerConstant>";
		} else if (type == STRING_CONST) {
			return "<stringConstant> " + this.stringVal() + " </stringConstant>";
		}
		else
			return "type doesn't match any of the 5 type";

	}
}
