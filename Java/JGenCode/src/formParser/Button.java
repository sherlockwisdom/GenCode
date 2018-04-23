package formParser;

public class Button extends PageProperties {
	
	public Button() {}
	
	public String log() {
		this.corpus = "Attribute:Button\n";
		this.corpus += "Name:" + this.attributeValueStore.get("name") + "\n";
		if(!this.attributeValueStore.get("value").isEmpty())
			this.corpus += "Value:" + this.attributeValueStore.get("value") + "\n";
		this.corpus = "Attribute:~Button\n\n";
		
		return this.corpus;
	}

}
