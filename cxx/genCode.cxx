#include <iostream>
#include <vector>
#include <map>
#include <fstream>

using namespace std;

class Form {
	vector<string> components, inputs, submits;
	bool tsubmits = false, inputsComing = false;
	string name, method;
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
		else if(i.find("inputs:") != string::npos) this->inputsComing = true;
	}

	static Form get() {
		Form form;
		return form;	
	}

	void write(string& conditions) {
		string mainMethod;
		if(method == "post") mainMethod = "$_POST";
		else if(method == "get") mainMethod = "$_GET";
		if(this->submits.size() > 0) {
			string mainMethod = "";
			conditions += "\nif(isset(" + mainMethod + "['" + this->submits[0] + "'])) {\n";
			
			for(auto i: this->inputs) {
				conditions += "\t$" + i + " = " + mainMethod + "['" + i + "'];\n";
			}
			conditions += "\n}";
		}
		else {
			for(auto i: this->inputs) {
				conditions += "$" + i + " = " + mainMethod + "['" + i + "'];\n";
			}
			conditions += "\n";
		}

		//ofstream writefile("maincontroller.php", ios::app);
		//writefile << conditions;
		//writefile.close();
	}
};


int main() {

	ifstream readfile("mainhtml.txt");
	ofstream writefile("Controller.php", ios::app);
	string temp, conditions = "<?php\n\n";

	vector<Form> forms;
	vector<string> file;

	while(getline(readfile, temp)) file.push_back(temp);
	readfile.close();

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