package html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	private static String htmlFile = "/home/maestro/Desktop/index.php";

	public static void main(String[] args) throws IOException {
		GenCode genCode = new GenCode(htmlFile);
		genCode.getForms();
	}

}
