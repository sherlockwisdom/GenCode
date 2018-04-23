package formParser;

import java.util.ArrayList;
import java.util.List;

public class Select extends PageProperties {
	
	private List<String> options = new ArrayList<String>();

	public Select() {}
	
	public void add(String tag, String attribute, String value) {
		if(tag.equals("option")) 
			if(attribute.equals("value")) 
				options.add(value);
	}
	
	
	public String log() {
		if(!this.attributeValueStore.get("name").isEmpty()) {
			String name = this.attributeValueStore.get("name").replace("[]", "");
			this.corpus = this.tabString + "$" + name + " = $_" + this.method + "['" + name + "'];\n";
			return this.corpus;
		}
		return "";
	}
		
}
