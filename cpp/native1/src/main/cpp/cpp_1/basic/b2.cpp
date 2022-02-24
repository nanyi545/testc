//
// Created by wei wang on 2022-02-21.
//
// g++ b2.cpp -o b2
// g++ -std=c++11 -o b2 b2.cpp
// g++ -Wall -Werror -pedantic -std=c++11 -o b2 b2.cpp

#include <iostream>
#include <string>
using std::cout;
using std::string;
#define NEWLINE '\n'


void duplicate (int& a, int& b, int& c)
{
    a*=2;
    b*=2;
    c*=2;
}

// Default values in parameters
int divide (int a, int b=2)
{
    int r;
    r=a/b;
    return (r);
}

template <class T>
T sum (T a, T b)
{
    return a+b;
}


namespace foo
{
    int value() { return 5; }
}

namespace bar
{
    const double pi = 3.1416;
    double value() { return 2*pi; }
}



int main()
{
    string str {"Hello!"};
    for (char c : str)
    {
        cout << "[" << c << "]";
    }
    cout << '\n';


    int x=1, y=3, z=7;
    duplicate (x, y, z);
    cout << "x=" << x << ", y=" << y << ", z=" << z << NEWLINE;


    cout << divide (12) << '\n';
    cout << divide (20,4) << '\n';


    x = sum<int>(10,20);
    cout << "int sum=" << x << NEWLINE;


    cout << foo::value() << '\n';
    cout << bar::value() << '\n';

}
