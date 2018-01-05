#include <iostream>
#include <vector>
#include <map>

using namespace std;

class Form {
	vector<string> components, inputs, submits;
	bool tsubmits = false, inputsComing = false;
	string name, method, action;

	string replace_spaces(string input) {
		//cout << "Started replacing..." << endl;
		while(input.find(" ")!=string::npos and input.find(" ") < input.size()) input[input.find(" ")] = '_';
		while(input.find("-")!=string::npos and input.find("-") < input.size()) input[input.find("-")] = '_';
		//cout << "Done replacing..." << endl;
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
		//cout << "Writing to new form" << endl;
		conditions += "/* Form action - " + this->action + " */\n";
		string mainMethod;
		if(method == "post") mainMethod = "$_POST";
		else if(method == "get") mainMethod = "$_GET";
		if(this->submits.size() > 0) {
			cout << "submits present!" << endl;
			conditions += "if(isset(" + mainMethod + "['" + this->submits[0] + "'])) {\n";
			
			for(auto i: this->inputs) {
				conditions += "\t$" + replace_spaces(i) + " = " + mainMethod + "['" + i + "'];\n";
				cout << "Added some conditions" << endl;
			}
			cout << "Done in submits" << endl;
			//conditions += "\n}";
		}
		else {
			cout << "No submits" << endl;
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