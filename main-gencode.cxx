//TODO: import and parase GCD file

#include "src/gencode/gencode.hpp"
using namespace std;

int main(int argc, char** argv) {
  GENCODE gencode;

  if(argc > 1) {
    // cout << "Commands are found" << endl;
  }

  if(gencode.launchParser()) {
    if(gencode.parseGCD()) {
      LOG::State("Parsed good!");
    }
  } else {
    LOG::Error("PROBLEM WITH JAVA PARSER");
    cout << "Code is exitiing here\n" << endl;
    return 1;
  }

  return 0;
}


/*





*/ 
