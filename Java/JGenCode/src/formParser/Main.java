package formParser;

import java.io.File;
import java.io.IOException;

public class Main {
	
	//private static String htmlFile = "/home/maestro/Desktop/index.php";
	private static String inputFile = new String();
	private static String outputFile = new String();
	private static String projectFolderName = "./";

	public static void main(String[] args) throws IOException {
		for(int i=0;i<args.length; ++i) {
			if(args[i].equals("-f")) {
				inputFile = args[i+1];
			} else if(args[i].equals("-o")) {
				outputFile = args[i+1] + ".gcd";
			} else if(args[i].equals("-p")) {
				projectFolderName = args[i+1];
				File projectFolder = new File(projectFolderName);
				projectFolder.mkdir();
			}
		}
		
		if(outputFile.isEmpty()) outputFile = inputFile + ".gcd";
		
		GenCode genCode = new GenCode(inputFile, outputFile, projectFolderName);
		genCode.writeFile();
		genCode.editHtml();
	}

}
