package html;

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
	private String htmlFile;
	private List<Forms> formList = new ArrayList<Forms>();
	
	public GenCode() {}
	
	public GenCode(String htmlFile) throws IOException {
		this.htmlFile = htmlFile;
		
		parse();
	}
	
	private void parse() throws IOException {
		File input = new File(this.htmlFile);
		Document doc = Jsoup.parse(input, "UTF-8", "");
		
		List<FormElement> forms = doc.getAllElements().forms();
		System.out.println("Found: " + forms.size() + " forms\n");
		
		for (Element form : forms) {
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
	}
	
	public void getForms() {
		for (Forms form : this.formList) {
			System.out.println("Form name: " + form.formID);
			
			for (Map.Entry<String, Set<String>> input : form.inputList.entrySet()) {
				System.out.println("For - " + input.getKey() + " -");
				
				for (String vals : input.getValue()) {
					System.out.println(vals);
				}
			}
			System.out.println("");
		}
	}
	
	public void writeFile(String fileName) throws IOException {
		//write php file
		
		/*
		 * if submit == found, create post checkers -> then input all other inputs
		 */
		//this.newHtmlFile = fileName;
		FileWriter outPutFile = new FileWriter(fileName, false);
		for (Forms form : this.formList) {
			System.out.println("Form-name: \nname: " + form.formID + "\nmethod: " + form.formMethod);
			
			outPutFile.write("Start-Form\n");
			outPutFile.write("Form-name: " + form.formID + "\nmethod: " + form.formMethod + "\naction: " + form.formAction + "\n");
			
			for (Map.Entry<String, Set<String>> input : form.inputList.entrySet()) {
				System.out.println("For - " + input.getKey() + " -");
				outPutFile.write(input.getKey() + ": \n");
				for (String vals : input.getValue()) {
					System.out.println(vals);
					outPutFile.write(vals + "\n");
				}
			}
			System.out.println("");
			outPutFile.write("End-Form\n\n");
		}
		outPutFile.close();
	}
	
	public void editHTML() throws IOException {
		//String input = "/home/maestro/Desktop/add.php";
		File file = new File(this.htmlFile);

		Document doc = Jsoup.parse(file, "UTF-8", "");

        //final File f = new File("filename.html");
		List<FormElement> forms = doc.getAllElements().forms();
		
		for (FormElement form : forms) form.attr("action", "maincontroller.php");
		
		//System.out.println("Output file: " + this.newHtmlFile);
        FileWriter writeFile = new FileWriter("Outputs/" + file.getName(), false);
        writeFile.write(doc.outerHtml());
        writeFile.close();
	}

}
