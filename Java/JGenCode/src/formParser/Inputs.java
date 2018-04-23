package formParser;

public class Inputs extends PageProperties {
	
	public Inputs() {}
	
	public String log() {
		
		if(!this.attributeValueStore.get("name").isEmpty()) {
			String name = this.attributeValueStore.get("name");
			this.corpus = this.tabString + "$" + name + " = $_" + this.method + "['" + name + "'];\n";
			return this.corpus;
		}
		
		return "";	
	}
}
