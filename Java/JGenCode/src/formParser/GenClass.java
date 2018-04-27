package formParser;

import java.util.List;

public class GenClass {
	
	protected String classFile = new String();
	protected String parsedString = new String();
	
	public GenClass() {}
	
	public void file(String classFile) {
		this.classFile = classFile;
		//TODO read file into String
	}
	
	public void generate() {
		String testClass = "class:patient {\nname, age, gender}";
//		List<String> classes = extClass(testClass);
	}
	
//	private List<String> extClass(String testClass) {
//		
//	}

	public String log() {
		
		
		return "";
	}

}

//TODO parse through class description with Json
//TODO build and create database model

