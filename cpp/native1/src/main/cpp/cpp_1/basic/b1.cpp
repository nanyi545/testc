//
// Created by wei wang on 2022-02-21.
//
// g++ b1.cpp -o b1
// g++ -std=c++11 -o b1 b1.cpp
// g++ -Wall -Werror -pedantic -std=c++11 -o b1 b1.cpp

#include <iostream>
#include <string>
using std::cout;
using std::endl;
using std::string;
const char newline = '\n';
#define NEWLINE '\n'

int main()
{
    cout << "---Hello World!\n";

    int a=5;       // initial value: 5
    int b(3);      // initial value: 3
    int c {2};     // initial value: 2
    int result;    // initial value undetermined
    a = a + b;
    result = a - c;
    cout << result << "\n";


    int foo = 0;
    auto bar = foo;  // the same as: int bar = foo;
    bar  = 99;
    cout << bar << newline;


    string mystring;
    mystring = "This is the initial string content";
    cout << mystring << endl;
    mystring = "This is a different string content";
    cout << mystring << NEWLINE;


    int i;
    float f = 3.14;
    i = (int) f;
    cout <<  "f:" << f <<  "  cast:" << i << NEWLINE;

}
