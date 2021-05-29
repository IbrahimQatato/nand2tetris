import java.io.File;

public class JackAnalyzer {

	public static void main(String[] args) {
		File input;
		File[] files;
		CompilationEngine parser;
		// if (args.length < 1) { System.out.println("missing argument"); return; }

		args = new String[1];
		args[0] = "C:\\Users\\DELL\\Desktop\\nand2tetris\\projects\\11\\Pong";
		// use for debugging
		input = new File(args[0]);
		if (input.isDirectory()) {
			files = input.listFiles();

		} else {
			if (!args[0].substring(args[0].length() - 4, args[0].length()).equals("jack")) {// checks if input is indeed
																							// a jack file
				System.out.println("wrong argument");
				return;
			}
			files = new File[1];
			files[0] = input;
			// int length = files[0].getAbsolutePath().length();
			// System.out.println(files[0].getAbsolutePath().substring(0, length - 4) +
			// "xml");
			// output = new File(files[0].getAbsolutePath().substring(0, length - 4) +
			// "xml");
		}

		// goes thru all files
		for (int i = 0; i < files.length; i++) {
			int namelength = files[i].getName().length();
			if (!(files[i].getName().substring(namelength - 4, namelength).equalsIgnoreCase("jack")))
				continue;
			System.out.println(files[i].getAbsolutePath());
			int length = files[i].getAbsolutePath().length();
			parser = new CompilationEngine(files[i],
					new File(files[i].getAbsolutePath().substring(0, length - 5) + ".vm"),
					new File(files[i].getAbsolutePath().substring(0, length - 5) + "My.xml"));
			try {
				parser.compileClass();
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * while(tokenizer.hasMoreTokens()) { tokenizer.advance(); }
			 */
			// useful loop for CEngine ^|^

		}
	}

}
