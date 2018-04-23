package formParser;

import java.io.IOException;

public class Main {
	
	private static String inputSource = new String();
	private static GenerateCode generateCode = new GenerateCode();
	
	//Terminal arguments would produce this values
	//TODO put values


	public static void main(String[] args) throws IOException {
		inputSource = "/home/sherlock/Desktop/StephanieTestFile.html";
		if(args.length > 1)
			parseTerminalArguments(args);
		
		Page page = new Page(inputSource);
		
		page.add("input")
			.add("form")
			.add("gencode");
		page.make();
		
		generateCode.add(page)
			.make();
	}
	
	private static void parseTerminalArguments(String[] args) {
		
	}

}
