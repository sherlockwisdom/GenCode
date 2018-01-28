#ifndef LOG_H_INCLUDED_
#define LOG_H_INCLUDED_
#include <iostream>
using namespace std;


class LOG {
public:
  static void Error(string input);
  static void State(string input);
};

void LOG::Error(string input) {
  cout << "\n[Error]: " << input << endl << endl;
}

void LOG::State(string input) {
  cout << "\n[State]: " << input << endl << endl;
}

#endif
