import java.io.File;

public class VMTranslator {

	public static void main(String[] args) throws Exception {
		File input;
		File[] files;
		File output;
		CodeWriter writer;
		/*if (args.length < 1) {
			System.out.println("missing argument");
			return;
		}*/
		args = new String[1];
		args[0] = "C:\\Users\\DELL\\Desktop\\nand2tetris\\projects\\08\\FunctionCalls\\StaticsTest";
		//use for debugging
		input = new File(args[0]);
		if (input.isDirectory()) {
			files = input.listFiles();
			//String[] s = args[0].split("//");
			String[] t = args[0].split("\\\\");
			String fileName = t[t.length - 1];
			output = new File(args[0]+"//"+fileName+".asm");
			writer = new CodeWriter(output);
			writer.writeInit();//Release me when doing multiple filed directories

		} else {
			if (!args[0].substring(args[0].length() - 3, args[0].length()).equals(".vm")) {
				System.out.println("wrong argument");
				return;
			}
			files = new File[1];
			files[0] = input;
			int length = files[0].getAbsolutePath().length();
			System.out.println(files[0].getAbsolutePath().substring(0, length-2)+"asm");
			output = new File(files[0].getAbsolutePath().substring(0, length-2)+"asm");
			writer = new CodeWriter(output);
		}
		// "C:\\Users\\DELL\\Desktop\\nand2tetris\\projects\\07\\StackArithmetic\\StackTest//StackTest.vm"
		// String argf =
		// "C:\\Users\\DELL\\Desktop\\nand2tetris\\projects\\07\\MemoryAccess\\BasicTest//BasicTest.vm";
		// String[] s = args[0].split("//");
		// String[1]
		for (int i = 0; i < files.length; i++) {
			int namelength = files[i].getName().length();
			if (!(files[i].getName().substring(namelength-3, namelength).equalsIgnoreCase(".vm")))
				continue;
			Parser parser = new Parser(files[i]);
			writer.setFileName(getFileName(files[i]));
			// get files ready:
			while (parser.hasMoreCommands()) {
				parser.advance();
				System.out.println(parser.command);
				if (parser.commandType() == 1) {
					writer.writeArithmetic(parser);
				} else if (parser.commandType() == 2 || parser.commandType() == 3){
					writer.writePushPop(parser.commandType(),parser.arg1(),parser.arg2());
				}
				 else if (parser.commandType() == 4){
						writer.writeLabel(parser.arg1());
					}
				 else if (parser.commandType() == 5){
						writer.writeGoto(parser.arg1());
					}
				 else if (parser.commandType() == 6){
						writer.writeIf(parser.arg1());
					}
				 else if (parser.commandType() == 7){
						writer.writeFunction(parser.arg1(),parser.arg2());
					}
				 else if (parser.commandType() == 8){
						writer.writeReturn();
					}else {
						/*if (parser.arg1().equals("Sys.init")) {
							continue;
						}*/
						writer.writeCall(parser.arg1(),parser.arg2());
					}

			}
			
		}
		writer.close();
	}
	public static String getFileName(File f) {
		String[] s = f.getName().split("//");
		s[0] = s[0].substring(0, s[0].length()-3);
		return s[0];
	}
}
