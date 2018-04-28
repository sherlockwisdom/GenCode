// #include "src/Php.hpp"
#include <iostream>
#include <vector>
#include <fstream>
using namespace std;

string pipe_terminal(string command) {
  string data;
  FILE * stream;
  const int max_buffer = 1024;
  char buffer[max_buffer];
  command.append(" 2>&1");

  stream = popen(command.c_str(), "r");
  if (stream) {
    while (!feof(stream)) if (fgets(buffer, max_buffer, stream) != NULL) data.append(buffer);
    pclose(stream);
  }
  return data;
}


class Database {
protected:
  string name;

  vector<string> split(string input) {
    vector<string> ret;

    ret.push_back(input.substr(0, input.find(":")));
    input.erase(0, input.find(":")+1);
    ret.push_back(input);
    return ret;
  }
  

  class Table {
    string name;
  private:
    vector<string> split(string input) {
      vector<string> ret;

      ret.push_back(input.substr(0, input.find(":")));
      input.erase(0, input.find(":")+1);
      ret.push_back(input);
      return ret;
    }
    string tableInfo;
    vector<string> tables;
  public:
    Table(string tableInfo) {
      this->tableInfo = tableInfo;
      cout << "_table-info: " << this->tableInfo << endl;
    }

    void parse() {
      // vector<string> tables;
      while(this->tableInfo.find("/")!=string::npos) {
        tables.push_back(this->tableInfo.substr(0, tableInfo.find("/")));
        this->tableInfo.erase(0, tableInfo.find("/")+1);
        // generate();
      }
      this->tables.push_back(this->tableInfo);
      generate();
      for(auto i: this->tables) cout << "_table: " << i << endl;
    }

    vector<string> seperate(string input) {
      vector<string> ret;

      while(input.find(",") !=string::npos) {
        ret.push_back(input.substr(0, input.find(",")));
        input.erase(0, input.find(",")+1);
      }
      ret.push_back(input);

      for(auto& i: ret){
        while(i.find(" ")!=string::npos)
          i.erase(i.find(" "), i.find(" ")+1);
      }
      return ret;
    }

    string database_name;

    bool check_table(string name) {
      cout << "_log: Checking table" << endl;
      string command = "php php/database.php --database " + this->database_name + " --is_table " + name;
      cout << "_phpquery: " << command << endl;
      // string command = "";
      string output = pipe_terminal(command);
      cout << "_php output: " << output << endl;
      if(output.find("true")==string::npos) {
        cout << "Creating table - " << name << endl;
        command = "php php/database.php --database " + this->database_name + " --create_table " + name;
        string output = pipe_terminal(command);
        cout << output << endl;
        if(output.find("true")==string::npos) {
          cout << "Creating table failed" << endl;
        }
      }
    }


    void bind(Database* pdatabase) {
      Database database = *pdatabase;
      this->database_name = database.get_name();
    }

    void create(string name) {
      string command = "php php/database --database " + this->database_name + " --create " + name;
      string output = pipe_terminal(command);
      if(output == "true") cout << "Created Table!" << endl;
      else cout << "Failed to create table" << endl;
    }

    bool check_fields(vector<string> fields) {
      for(auto field: fields) {
        string command = "php php/database.php --database " + this->database_name + " --is_field " + field;
        cout << "_phpquery: " << command << endl;
        string output = pipe_terminal(command);
        if(output.find("true")==string::npos) {
          cout << "Creating Field[" << field << "]" << endl;
          command = "php php/database.php --database " + this->database_name + " --create_field " + field + " " + this->name;
          output = pipe_terminal(command);
          cout << output << endl;
          if(output.find("true")==string::npos) 
            cout << "Failed to create fields" << endl;
        }
      }
    }

    void generate() {
      for(auto table: this->tables) {
        if(table.find(":")!=string::npos) {
          vector<string> splitted = split(table);
          this->name = splitted[0];
          string properties = splitted[1].substr(splitted[1].find("[")+1, (splitted[1].find("]") -1) - splitted[1].find("["));
          cout << "_property[" << name << "]: " << properties << endl;
          vector<string> solo_props = seperate(properties);
          for(auto i: solo_props) cout << "_solo prop: " << i << endl;
          check_table(this->name);
          check_fields(solo_props);
        }
      }
    }
  };

  vector<Table> tables;

public:
  string get_name() {
    return this->name;
  }

  void check_database(string name) {
    string command = "php php/database.php --database " + name + " --is_database";
    cout << "_phpquery: " << command << endl;
    string output = pipe_terminal(command);
    cout << output << endl;
    if(output.find("true")==string::npos) {
      cout << "Creating database" << endl;
      command = "php php/database.php --database " + name + " --make";
      cout << "_phpquery: " << command << endl;
      if(pipe_terminal(command) != "true") {
        cout << "Failed to create database" << endl;
      }
    }

    else
      cout << "Database found!" << endl;
  }

  Database(vector<string> lines) {
    //locate database
    vector<string> splitted;
    for(auto line: lines) {
      splitted = split(line);
      if(splitted[0] == "database") {
        this->name = splitted[1];
        check_database(this->name);
      }
      else
        if(splitted[0] == "tables") {
          Table table(splitted[1]);
          table.bind(this);
          table.parse();
          this->tables.push_back(table);
        }
    }
    cout << "_db-name: " << this->name << endl;
  }

  void log() {

  }
};


int main() {
  ifstream databaseFile("afkanerd.db");
  if(databaseFile.good()) cout << "File found!" << endl;
  else {
    cout << "File not found" << endl;
    return 1;
  }
  string tmp;
  vector<string> lines;
  while(getline(databaseFile, tmp)) lines.push_back(tmp);
  databaseFile.close();

  //line 0 - name of database
  //line 1 - tables and their properties

  Database database(lines);
  database.log();
}
