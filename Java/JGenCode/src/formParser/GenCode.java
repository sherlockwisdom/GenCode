package formParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;


public class GenCode {
	private String htmlFile = new String();
	private String outputFile = new String();
	private String projectFolder = new String();
	private boolean url = false;
	private List<Forms> formList = new ArrayList<Forms>();
	private List<Inputs> inputList = new ArrayList<Inputs>();
	private List<GenCode> generatedCode = new ArrayList<GenCode>();
	
	public GenCode() {}
	
	public GenCode(String htmlFile, String outputFile, String projectFolderName, boolean url) throws IOException {
		this.htmlFile = htmlFile;
		this.outputFile = outputFile;
		this.projectFolder = projectFolderName;
		this.url = url;
		
		parse();
	}
	
	private void parse() throws IOException {
		File input = null;
		Document doc = null;
		if(this.url) {
			System.out.println("Found url: " + this.htmlFile);
			doc = Jsoup.connect(this.htmlFile).get();
		} else {
			input = new File(this.htmlFile);
			System.out.println("Fond file: " + this.htmlFile);
			doc = Jsoup.parse(input, "UTF-8", "");
		}
		
		List<FormElement> forms = doc.getAllElements().forms();
		Elements _inputs = doc.getAllElements().select("input");
		System.out.println("Found: " + forms.size() + " forms");
		System.out.println("Found: " + _inputs.size() + " inputs\n");
		
		
		
//		All form information goes in here
		for (Element form : forms) {
			// Find the last of how all this things fit in the main picture without changing a lot of other things
			//System.out.println("All Forms elements: " + form.getAllElements().html());
			Map<String, Set<String>> inputList = new HashMap<>();
			Set<String> lInputs = new HashSet<String>();
			Set<String> lSubmits = new HashSet<String>();
			Elements formElements = form.getAllElements();
			
			for (Element formElement : formElements) {
				Elements inputs = formElement.getElementsByTag("input");
				//System.out.println("Number of inputs: " + inputs.size());
				for (Element tagInput : inputs) { 
					if(!tagInput.attr("name").isEmpty()) lInputs.add(tagInput.attr("name"));
					//if(!tagInput.attr("select").isEmpty()) lInputs.add(tagInput.attr("select"));
					if(tagInput.attr("type").equals("submit")) {
						if(!tagInput.attr("name").isEmpty()) lSubmits.add(tagInput.attr("name"));
						else if(!tagInput.attr("id").isEmpty()) lSubmits.add(tagInput.attr("id"));
					}
					//System.out.println("Adding input: " + tagInput.attr("name"));
					//System.out.println("Adding select: " + tagInput.attr("select"));
					//System.out.println("Adding type: " + tagInput.attr("type"));
				}
				
				Elements buttons = formElement.getElementsByTag("button");
				for (Element button : buttons) {
					if(button.attr("type").equals("submit")) {
						if(!button.attr("name").isEmpty()) lSubmits.add(button.attr("name"));
						else if(!button.attr("id").isEmpty()) lSubmits.add(button.attr("id"));
					}
				}
				
				Elements selects = formElement.getElementsByTag("select");
				for (Element select : selects) {
					if(!select.attr("name").isEmpty()) lInputs.add(select.attr("name"));
				}
			}
			if(lInputs.size() >  0) { 
				//System.out.println("List of inputs is more than zero!");
				inputList.put("inputs", lInputs);
			}
			if(lSubmits.size() > 0) { 
				//System.out.print("List of sumits is more than zero!");
				inputList.put("submits", lSubmits);
			}
			//System.out.println("InputList size: " + inputList.size());
			if(inputList != null) { 
				Forms cform = new Forms();
				System.out.println("Method: " + form.attr("method"));
				cform.set(inputList, form.attr("method"), form.attr("action"), form.attr("id"));
				this.formList.add(cform.get());
				//System.out.println("Form added\n");
			}
		}
		
//		Begin extracting all rougue inputs and their forms
//		Select all element which is not in div
		Elements ___inputs = doc.body().children();
		Elements inp = new Elements();
		
		for(Element in : ___inputs) {
			if(in.select("form").size() < 1) {
				inp.addAll(in.select("input"));
			}
		}
//		doc.body().before(node).
		System.out.println("Div Inputs: " + inp.size());
		
		for(Element in : inp) {
			Inputs _input = new Inputs();
			String name = in.attr("name");
			if(name.length() > 0) {
				_input.set(name);
				System.out.println("[TAG]: " + in.toString());
				inputList.add(_input);
			}
		}
		
		Elements gencode = doc.select("gencode");
		
		for(Element gen : gencode) {
			//find everything with the gencode attributes in here`
			GenCode genCode = new GenCode();
			
		}
	}
	
//	public void getForms() {
//		for (Forms form : this.formList) {
////			System.out.println("Form name: " + form.formID);
//			
//			for (Map.Entry<String, Set<String>> input : form.inputList.entrySet()) {
////				System.out.println("For - " + input.getKey() + " -");
//				
//				for (String vals : input.getValue()) {
////					System.out.println(vals);
//				}
//			}
//			System.out.println("");
//		}
//	}
	
	public void writeFile() throws IOException {
		System.out.println("Writing to file");
		FileWriter outPutFile = new FileWriter(this.projectFolder + "/" + this.outputFile, false);
		System.out.println("Output file: " + this.projectFolder + "/" + this.outputFile);
		for (Forms form : this.formList) {
//			System.out.println("Form-name: \nname: " + form.formID + "\nmethod: " + form.formMethod);
			
			outPutFile.write("Start-Form\n");
			outPutFile.write("Form-name: " + form.formID + "\nmethod: " + form.formMethod + "\naction: " + form.formAction + "\n");
			
			for (Map.Entry<String, Set<String>> input : form.inputList.entrySet()) {
//				System.out.println("For - " + input.getKey() + " -");
				outPutFile.write(input.getKey() + ": \n");
				for (String vals : input.getValue()) {
//					System.out.println(vals);
					outPutFile.write(vals + "\n");
				}
			}
//			System.out.println("");
			outPutFile.write("End-Form\n\n");
		}
		
		if(inputList.size() > 0) {
			outPutFile.write("Input-Starts\n");
			for(Inputs input: inputList) {
				outPutFile.write(input.get() + "\n");
			}
			outPutFile.write("Input-Ends\n");
		}
		outPutFile.close();
	}
	
	public void editHtml() throws IOException {
		File file = new File(this.htmlFile);
		Document doc = Jsoup.parse(file, "UTF-8", "");
		List<FormElement> forms = doc.getAllElements().forms();
		
		for (FormElement form : forms) form.attr("action", this.projectFolder + this.outputFile);
        FileWriter writeFile = new FileWriter(this.projectFolder + file.getName(), false);
        
        writeFile.write(doc.outerHtml());
        writeFile.close();
	}

}
