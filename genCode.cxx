#include <iostream>
#include <vector>
#include <map>
#include <fstream>

using namespace std;

class Form {
	vector<string> components, inputs, submits;
	bool tsubmits = false, inputsComing = false;
	string name, method, action;

	string replace_spaces(string input) {
		while(input.find(" ")!=string::npos) input[input.find(" ")] = '_';
		while(input.find("-")!=string::npos) input[input.find(" ")] = '_';
		return input;
	}
public:
	void add(auto i) {
		//components.push_back(i);
		if(tsubmits) {
			if(i.find(':') !=string::npos) tsubmits = false;
			else {
				if(!i.empty()) { 
					submits.push_back(i);
					cout << "values: " << i << endl;
				}
			}
		}
		else if(inputsComing) {
			if(i.find(':') !=string::npos) inputsComing = false;
			else {
				if(!i.empty()) { 
					inputs.push_back(i);
					cout << "values: " << i << endl;
				}
			}
		}
		
		if(i.find("submits:") != string::npos) this->tsubmits = true;
		else if(i.find("name:") != string::npos) {
			this->name = i.substr(i.find(" ")+1, i.size()-1);
			cout << "name: " << this->name << endl;
		}
		else if(i.find("method:") != string::npos) {
			this->method = i.substr(i.find(" ")+1, i.size()-1);
			cout << "method: " << this->method << endl;
		}
		else if(i.find("action:") != string::npos) {
			this->action = i.substr(i.find(" ") +1, i.size() -1);
			cout << "action: " << this->action << endl;
		}
		else if(i.find("inputs:") != string::npos) this->inputsComing = true;
	}

	static Form get() {
		Form form;
		return form;	
	}

	void write(string& conditions) {
		conditions += "/* Form action - " + this->action + " */\n";
		string mainMethod;
		if(method == "post") mainMethod = "$_POST";
		else if(method == "get") mainMethod = "$_GET";
		if(this->submits.size() > 0) {
			conditions += "if(isset(" + mainMethod + "['" + this->submits[0] + "'])) {\n";
			
			for(auto i: this->inputs) {
				conditions += "\t$" + replace_spaces(i) + " = " + mainMethod + "['" + i + "'];\n";
			}
			//conditions += "\n}";
		}
		else {
			if(method == "post") {
				conditions += "if($_SERVER['REQUEST_METHOD'] === 'POST') {\n";
				for(auto i: this->inputs) {
					conditions += "\t$" + replace_spaces(i) + " = " + mainMethod + "['" + i + "'];\n";
				}
				//conditions += "\n}";
			}
		}
		conditions += "\n\t/* your code goes here... */\n\n}\n\n";
		//ofstream writefile("maincontroller.php", ios::app);
		//writefile << conditions;
		//writefile.close();
	}
};


int main(int argc, char** argv) {
	if(argc != 5) {
		cout << "Error with input... please check all syntax!" << endl;
		return 1;
	}
	string input_file, output_file;

	for(int i=1;i<argc;++i) {
		if(string(argv[i]) == "-f") {
			input_file = argv[i+1];
			++i;
		} else if(string(argv[i]) == "-o") {
			output_file = argv[i+1];
			++i;
		}
	}

	cout << "Input file: " << input_file << "\nOutput file: " << output_file << endl;

	cout << "Running Java parser..." << endl;
	string javaCommand = "java -jar genCode.jar -f " + input_file + " -o Generated/" + output_file + ".gcd";
	system(javaCommand.c_str());

	string gen_filename = output_file + ".gcd";
	ifstream check_file(string("Generated/" + gen_filename).c_str());
	if(check_file.good()) cout << "Output generated!..." << endl;
	else cout << "No output generated!..." << endl;
	check_file.close();


	ifstream readfile(string("Generated/" + gen_filename).c_str());
	ofstream writefile(string("Generated/" + output_file).c_str());
	string temp, conditions = "<?php\n\n";

	vector<Form> forms;
	vector<string> file;

	if(!readfile.good()) {
		cout << "Input file can't be read! FILE[" << input_file << "]" << endl;
		return 2;
	}
	if(!writefile.good()) {
		cout << "Output file can't be read! FILE[" << output_file << "]" << endl;
		return 2;
	}

	while(getline(readfile, temp)) file.push_back(temp);
	readfile.close();
	system(string("rm Generated/" + gen_filename).c_str());

	Form form;
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
	cout << "Number of forms: " << forms.size() << endl;
	for(auto i: forms) i.write(conditions);
	conditions += "\n\n?>\n";
	
	writefile << conditions;
	writefile.close();

	return 0;
}