package formParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Forms {
	public String formID = new String(), formMethod = new String(), formAction = new String();
	public Map<String, Set<String>> inputList = new HashMap<>();
	private Forms form;
	
	public Forms() {
		//inputList = new HashMap<>();
	}
	
	public void set(Map<String, Set<String>> inputList, String formMethod, String formAction, String formID) {
		this.form = new Forms();
		this.form.formMethod = formMethod;
		this.form.formAction = formAction;
		this.form.formID = formID;
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
