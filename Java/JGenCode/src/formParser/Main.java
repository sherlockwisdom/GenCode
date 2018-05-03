package formParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

public class Main {
	
	private static String inputSource = new String();
	protected static String classFile = new String();
	private static GenerateCode generateCode = new GenerateCode();
	
	private static String file = new String(), folder = new String();
	
	//Terminal arguments would produce this values
	//TODO put values


	public static void main(String[] args) throws IOException {
		inputSource = "/var/www/html/edoctar-production/pages/Consultation/form.php";
		if(args.length > 1)
			parseTerminalArguments(args);
		
//		if(!folder.isEmpty()) {
//		    Files.find(Paths.get(sDir), 999, (p, bfa) -> bfa.isRegularFile()).forEach(System.out::println);
//		}
		
		Page page = new Page(inputSource);
		
		page.add("form");
		page.make();
		
		generateCode.add(page)
			.make();
	}
	
	private static void parseTerminalArguments(String[] args) {
		//file: works with file
		//folder: works with folder
		
		for(Integer i=0; i<args.length; ++i) {
			if(args[i] == "--file") {
				++i;
				file = args[i];
			}
			
			else
				if(args[i] == "--folder") {
					++i;
					folder = args[i];
				}
		}
	}

}

//TODO generate class
//TODO create class relationship
