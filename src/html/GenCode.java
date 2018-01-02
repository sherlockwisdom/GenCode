package html;

import java.io.File;
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
	private List<Forms> formList = new ArrayList();
	
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
			Set<String> lInputs = new HashSet();
			Set<String> lSubmits = new HashSet();
			Elements formElements = form.getAllElements();
			
			for (Element formElement : formElements) {
				Elements inputs = formElement.getElementsByTag("input");
				//System.out.println("Number of inputs: " + inputs.size());
				for (Element tagInput : inputs) { 
					if(!tagInput.attr("name").isEmpty()) lInputs.add(tagInput.attr("name"));
					if(!tagInput.attr("select").isEmpty()) lInputs.add(tagInput.attr("select"));
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
				cform.set(inputList, form.attr("id"));
				this.formList.add(cform.get());
				//System.out.println("Form added\n");
			}
		}
	}
	
	public void getForms() {
		for (Forms form : this.formList) {
			System.out.println("Form name: " + form.name);
			
			for (Map.Entry<String, Set<String>> input : form.inputList.entrySet()) {
				System.out.println("For - " + input.getKey() + " -");
				
				for (String vals : input.getValue()) {
					System.out.println(vals);
				}
			}
			System.out.println("");
		}
	}

}
