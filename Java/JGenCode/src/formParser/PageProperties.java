package formParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageProperties {
	
	protected String corpus = new String();
	
	protected Map<String, String> attributeValueStore = new HashMap<>();
	protected List<PageProperties> pageProperties = new ArrayList<>();
	protected String method = new String(), tabString = "\t";
	protected Integer tabSize = 1;
	
	public void add(String attribute, String value) {
		this.attributeValueStore.put(attribute, value);
	}
	
	public void add(PageProperties pageProperties) {
		for(PageProperties pageProperty : this.pageProperties) {
			String value = pageProperties.attributeValueStore.get("name");
			if(pageProperty.attributeValueStore.containsValue(value)) {
//				System.out.println("Already listed, skipping...");
				return;
			}
				
		}
		this.pageProperties.add(pageProperties);
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public String log() {
		return "";
	}
	
	public void setIndent(Integer tabSize) {
		this.tabSize = tabSize;
		this.tabString = new String();
		for(int i=0;i<tabSize;++i) 
			this.tabString += "\t";
	}
	
	public boolean find(String type, String value) {
		if(this.attributeValueStore.get(type).equals(value)) 
			return true;
		else
			return false;
	}

}
