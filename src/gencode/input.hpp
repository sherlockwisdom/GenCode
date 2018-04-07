#ifndef INPUT_H_INCLUDED_
#define INPUT_H_INCLUDED_
#include <iostream>
#include <vector>
#include <map>
using namespace std;


class Input {
	vector<string> inputs;

	string replace_spaces(string input) {
		//cout << "Started replacing..." << endl;
		while(input.find(" ")!=string::npos and input.find(" ") < input.size()) input[input.find(" ")] = '_';
		while(input.find("-")!=string::npos and input.find("-") < input.size()) input[input.find("-")] = '_';
		//cout << "Done replacing..." << endl;
		return input;
	}
public:
	Input() {}

	void add(auto i) {
		this->inputs.push_back(i);
	}

	static Input get() {
		Input input;
		return input;
	}

	void write(string& conditions) {
		conditions += "/* Inputs without a form come below */\n";
		for(auto i: this->inputs) {
			conditions += "\t$" + replace_spaces(i) + " = $_GET[" + replace_spaces(i) + "];\n";
		}

		conditions += "\n/* Your code goes here... */\n\n";
	}
};

#endif