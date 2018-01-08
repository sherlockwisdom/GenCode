#include <fstream>
#include "Class/Form.hpp"

using namespace std;

int main(int argc, char** argv) {
	Form form;
	vector<Form> forms;
	vector<string> file;
	string input_file, output_file, project_folder = "./";

	/* * Start of input check
	//input length checks
	if(argc != 5) {
		cout << "Number of args: " << argc << endl;
		cout << "Error with input... please check all syntax!" << endl;
		return 1;
	}
	*/
	//CLI argument extraction
	for(int i=1;i<argc;++i) {
		if(string(argv[i]) == "-f") {
			input_file = argv[i+1];
			++i;
		} else if(string(argv[i]) == "-o") {
			output_file = argv[i+1];
			++i;
		} else if(string(argv[i]) == "-p") {
			project_folder = argv[i+1];
			++i;
		}
	}

	if(input_file.empty()) {
		cout << "No input file... code's gonna break now!" << endl;
		return 1;
	}

	cout << "INPUT FILE: " << input_file << endl;

	/* * Start running Java parser **/
	cout << "RUNNING JAVA CODES - [.][.][.]" << endl;
	string javaCommand = project_folder != "./" ? "java -jar genCode.jar -f " + input_file + " -p " + project_folder : "java -jar genCode.jar -f " + input_file;

	cout << "JAVA COMMAND: " << javaCommand << endl;
	system(javaCommand.c_str());

	ifstream jarGeneratedFiles(string(input_file + ".gcd").c_str());
	if(jarGeneratedFiles.good()) cout << "JAVA GENERATED SUCCESSFULLY!" << endl;
	else {
		cout << "NO OUTPUT GENERATED!..." << endl;
		cout << "you could try again!..." << endl;
		return 2;
	}
	/** Java Parser ended here  */

	/* Creating controller files */
	ofstream mainController(project_folder + "maincontroller.php");
	string temp, conditions = "<?php\n\n";

	while(getline(jarGeneratedFiles, temp)) file.push_back(temp);
	jarGeneratedFiles.close();
	system(string("rm " + input_file + ".gcd").c_str());
	cout << "GENERATED FILE REMOVED!" << endl;

	for (auto i: file) {
		if(i.find("Start-Form") != string::npos)  {
			form = Form::get();
			continue;
		}
		else if(i.find("End-Form") != string::npos) {
			forms.push_back(form);
			continue;
		}
		else form.add(i);
	}
	cout << endl;
	cout << "NUMBER OF FORMS " << forms.size() << endl;
	for(auto i: forms) i.write(conditions);
	cout << "DONE WRITING!" << endl;
	conditions += "\n\n?>\n";

	mainController << conditions;
	mainController.close();

	return 0;
}
