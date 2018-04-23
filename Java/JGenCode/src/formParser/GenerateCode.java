package formParser;

import java.util.ArrayList;
import java.util.List;

public class GenerateCode {
	
	private List<Page> pages = new ArrayList<Page>();
	
	public GenerateCode() {
	}
	
	public GenerateCode add(Page page) {
		this.pages.add(page);
		return this;
	}
	
	public void make() {
		
	}

}
