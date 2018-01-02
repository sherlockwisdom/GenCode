package html;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Forms {
	public String name;
	public Map<String, Set<String>> inputList = new HashMap<>();
	private Forms form;
	
	public Forms() {
		//inputList = new HashMap<>();
	}
	
	public void set(Map<String, Set<String>> inputList, String formName) {
		this.form = new Forms();
		this.form.name = formName;
		if(inputList != null) {
			//System.out.println("New Form created with size: " + inputList.size());
			this.form.inputList = inputList;
		} else {
			System.out.println("Null input size");
		}
	}
	
	public Forms get() {
		//System.out.println("Returning this: " + form.inputList.size());
		return this.form;
	}
	
	public int size() {
		return this.inputList.size();
	}
	
	
}
