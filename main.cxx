#include "src/Php.hpp"
#include <vector>
using namespace std;


class Database {
private:
	string name;
	vector<Table> tables;

	class Table {
	public:
		Table(string tableInfo) {
			this->tableInfo = tableInfo;
		}

		void parse() {
			vector<string> tables;
			while(this->tableInfo.find("/")!=string::npos) {
				tables.push_back(this->tableInfo.substr(0, tableInfo.find("/")));
				this->tableInfo.erase(0, tableInfo.find("/")+1);
			}
			for(auto i: tables) cout << "_table: " << i << endl;
		}
	};

	vector<string> split(string input) {
		vector<string> ret;

		while(input.find(":") != string::npos) {
			ret.push_back(input.substr(0, input.find(":")));
			input.erase(0, input.find(":")+1):
		} ret.push_back(input);
		return ret;
	}
public:
	Database(vector<string> lines) {
		//locate database
		vector<string> splitted;
		for(auto line: lines) {
			splitted = split(line);
			if(splitted[0] == "database")
				this->name = splitted[1];
			else
				if(splitted[0] == "tables") {
					Table table(splitted[1]);
					table.parse();
					this->tables.push_back(table);
				}
		}
	}

	void log() {

	}
};
int main() {
  ifstream databaseFile("afkanerd.db");
  string tmp;
  while(getline(databaseFile, tmp)) lines.push_back(tmp);
  databaseFile.close();

  //line 0 - name of database
  //line 1 - tables and their properties

  Database database(lines);
  database.log();
}
