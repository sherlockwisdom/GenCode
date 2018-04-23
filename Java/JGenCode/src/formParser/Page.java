package formParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Page {
	
	private String inputSource = new String();
	private List<String> selectors = new ArrayList<String>();
	private Map<String, String> page = new HashMap<String, String>();
	
	public Page() {}
	
	public Page(String inputSource) {
		this.inputSource = inputSource;
	}
	
	public Page add(String selector) {
		this.selectors.add(selector);
		return this;
	}
	
	public boolean make() throws IOException {
		System.out.println("[Making] Pages");
		
		if(this.inputSource.isEmpty())
			return false;
		
//		TODO check if file or URL - add Connector for that
		File inputSource = new File(this.inputSource);
		Document doc = Jsoup.parse(inputSource, "UTF-8", "");
		
		System.out.println("[Extracting] Selectors");
		for(String selector : selectors) {
//			I think putting some conditioners in here could make work more effective
			if(selector.equals("form")) 
				parseForms(doc.body().select(selector));
			else if(selector.equals("input")) 
				parseInputs(doc.body().children());
			else if(selector.equals("gencode")) 
				parseGencodes(doc.select(selector));
			else 
				System.out.println("[Invalid] Selector type: " + selector);
		}
		
		System.out.println("Forms---\n" + page.get("form"));
		System.out.println("Inputs---\n" + page.get("input"));
		
		return true;
	}
	
	private void parseForms(Elements elements) {
		for(Element element : elements) {
			Forms form = new Forms();
			
			String method = element.attr("method");
			String action = element.attr("action");
			
			form.add("method", method);
			form.add("action", action);
			
//			Make sense of the form information
			for(Element inputs : element.getElementsByTag("input")) {
				String type = inputs.attr("type");
				String name = inputs.attr("name");
				
//				TODO Special case if value is set
//				String value = input.attr("value");
				
//				registerToFile("input", "type", type);
//				registerToFile("input", "name", name);
				Inputs input = new Inputs();
				input.add("type", type);
				input.add("name", name);
				
				form.add(input);
			}
			
			for(Element selects : element.getElementsByTag("select")) {
				Select select = new Select();
				
				String name = selects.attr("name");
				select.add("name", name);
				
				for(Element option : selects.getElementsByAttribute("option")) {
					String value = option.attr("value");
					
					if(value.isEmpty()) 
						value = option.text();
					
					select.add("option", "value", value);
				}
				
				form.add(select);
			}
			
			for(Element buttons: element.getElementsByTag("button")) {
				Button button = new Button();
				
				String name = buttons.attr("name");
				String value = buttons.attr("value");
				
				button.add("name", name);
				button.add("value", value);
				
				form.add(button);
			}
			
//			System.out.print("<?php\n" + form.log() + "\n\n?>");
			page.put("form", form.log());
		}
	}
	
	private void parseInputs(Elements elements) {
		Inputs input = new Inputs();
		for(Element element: elements) {
			if(element.select("form").isEmpty()) {
				Elements inputs = element.select("input");
				if(!inputs.isEmpty()) {
//					System.out.println("Things are showing");
					input.add("type", inputs.attr("type"));
					input.add("name", inputs.attr("name"));
					input.setMethod("GET");
//					System.out.println(input.log());
				}
			}
		}
		page.put("input", input.log());
	}
	
	private void parseGencodes(Elements elements) {
		
	}

}
