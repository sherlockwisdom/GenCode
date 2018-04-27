package formParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Database {
	
	protected File databaseFile = null;
	private Map<String, String> databases = new HashMap<>();
	
	public Database() {
		
	}
	
	public Boolean file(String dbFile) {
		//TODO: authenticate file
		this.databaseFile = new File(dbFile);
		if(this.databaseFile.isFile())
			return true;
		return false;
	}
	
	public void parse() throws FileNotFoundException {
		String sample = "user {\nprops:name\n}\n databse {\nprops:other things\n}";
		String[] objects = sample.split(" ");
		for(Integer i=0;i<objects.length-1;i+=2) {
			this.databases.put(objects[i], objects[i+1].split("/\\({[^}]+)\\}/")[0]);
		}
	}
	
	public String log() {
		return null;
	}
}
