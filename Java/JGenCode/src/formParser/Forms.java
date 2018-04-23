package formParser;


//TODO: if input contains the required tag, then make sure it's not empty
public class Forms extends PageProperties {
	
	public Forms() {}
	
	public String log() {
		String method = this.attributeValueStore.get("method").toUpperCase(); //TODO: change to upper
		
		this.corpus = "\n\tif($_SERVER['REQUEST_METHOD'] == '" + method + "' ";
		for(PageProperties pageProperties : this.pageProperties) {
			if(pageProperties.find("type", "submit")) {
				this.corpus += " && isset($_" + method + "['" + pageProperties.attributeValueStore.get("name") + "'])";
				break;
			}
		}
		this.corpus += ") {\n";
		for(PageProperties pageProperties : this.pageProperties) {
			pageProperties.setMethod(method);
			pageProperties.setIndent(2);
			this.corpus += pageProperties.log();	
		}
		this.corpus += "\t}";
		
		return this.corpus;
	}
	
}