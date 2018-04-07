package formParser;

import java.io.File;
import java.io.IOException;


//TODO make form attributes in files have unique openings e.g formMethod -> #formMethod

public class Main {
	
	private static String inputFile = new String();
	private static String outputFile = new String();
	private static String projectFolderName = "./";
	private static boolean urlState = false;

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
			} else if(args[i].equals("-url")) {
				urlState = true;
				inputFile = args[i+1];
			}
		}
		
		//TODO be sure to change file name if url is present
		if(outputFile.isEmpty()) outputFile = inputFile + ".gcd";
		
		GenCode genCode = new GenCode(inputFile, outputFile, projectFolderName, urlState);
		genCode.writeFile();
//		if(!urlState) genCode.editHtml(); //Edits the main file to cus a redirect
	}

}
