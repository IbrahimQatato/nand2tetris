import java.util.Hashtable;

public class SymbolTable {
	private Hashtable<String, String[]> classScope = new Hashtable<String, String[]>();
	private Hashtable<String, String[]> subroutineScope;
	int staticC = 0, fieldC = 0, argC = 0, varC = 0;
	public static final String STATIC = "static", FIELD = "field", ARG = "arg", VAR = "var", NONE = "none";
	

	public SymbolTable() {
		subroutineScope = new Hashtable<String, String[]>();
	}

	public void startSubroutine() {
		subroutineScope = new Hashtable<String, String[]>();
		argC = 0;
		varC = 0;
	}

	public void define(String name, String type, String kind) throws Exception {
		if (kind.equals(STATIC)) {
			classScope.put(name, new String[] { type, kind, staticC++ + "" });
		} else if (kind.equals(FIELD)) {
			classScope.put(name, new String[] { type, kind, fieldC++ + "" });
		} else if (kind.equals(VAR)) {
			subroutineScope.put(name, new String[] { type, kind, varC++ + "" });
		} else if (kind.equals(ARG)) {
			subroutineScope.put(name, new String[] { type, kind, argC++ + "" });
		} else
			throw new Exception("no mathing kind");
	}

	public int varCount(String kind) {
		if (kind.equals(STATIC)) {
			return staticC;
		} else if (kind.equals(FIELD)) {
			return fieldC;
		} else if (kind.equals(VAR)) {
			return varC;
		} else if (kind.equals(ARG)) {
			return argC;
		} else {
			return -1;
		}
		
	}

	public String kindOf(String name) {
		if (subroutineScope.containsKey(name)) {
			return subroutineScope.get(name)[1];
		} else if (classScope.containsKey(name)) {
			return classScope.get(name)[1];
		} else {
			return NONE;
		}
	}

	public String typeOf(String name) {
		if (subroutineScope.containsKey(name)) {
			return subroutineScope.get(name)[0];
		} else
			return classScope.get(name)[0];
	}

	public int indexOf(String name) {
		if (subroutineScope.containsKey(name)) {
			return Integer.parseInt(subroutineScope.get(name)[2]);
		} else
			return Integer.parseInt(classScope.get(name)[2]);
	}
}
