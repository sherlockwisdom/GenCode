package html;

import java.io.IOException;

public class Main {
	
	private static String htmlFile = "/home/maestro/Desktop/index.php";

	public static void main(String[] args) throws IOException {
		String outPutFile = new String();
		for(int i=0;i<args.length; ++i) {
			if(args[i].equals("-f")) {
				htmlFile = args[i+1];
			} else if(args[i].equals("-o")) {
				outPutFile = args[i+1];
			}
		}
		GenCode genCode = new GenCode(htmlFile);
		//genCode.getForms();
		genCode.writeFile(outPutFile);
	}

}
