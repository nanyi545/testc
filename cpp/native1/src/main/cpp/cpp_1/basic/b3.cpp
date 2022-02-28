//
// Created by wei wang on 2022-02-21.
//
// g++ b3.cpp -o b3
// g++ -std=c++11 -o b3 b3.cpp
// g++ -Wall -Werror -pedantic -std=c++11 -o b3 b3.cpp
// g++ -Wall -Werror -pedantic -std=c++11 -o b3 b3.cpp C1.cpp   // multiple files ...
// g++ -Wall -Werror -pedantic -std=c++11 -o b3 b3.cpp C1_1.cpp   // use a different implementation  !!!


#include <iostream>
#include <string>
#include "C1.h"

using std::cout;
using std::string;
using std::nothrow;

#define NEWLINE '\n'


int main()
{

    int * p1;
    p1 = new (nothrow) int [5];
    if (p1 == nullptr) {
        // error assigning memory. Take measures.
    }
    cout << "i[0]=" << *p1 << NEWLINE;
    cout << "i[1]=" << *(p1+1) << NEWLINE;  // not properly inited...
    *(p1+1) = 10;
    cout << "after reset i[1]=" << *(p1+1) << NEWLINE;

    delete[] p1;


    C1 c (10.0);
    cout << "radius 10: has circum =" << c.circum() << NEWLINE;

    C1 *p  = &c;  //
    p->circum();


    /**
     * The keyword struct, generally used to declare plain data structures, can also be used to declare classes that have member functions,
     * with the same syntax as with keyword class.
     *
     * The only difference between both is that members of classes declared with the keyword struct have public access by default,
     * while members of classes declared with the keyword class have private access by default. For all other purposes both keywords
     * are equivalent in this context.
     *
     */

    C1 c1 (2.0);
//    C1 c2 = c1 + c;
    cout << "radius 12: has circum =" << c1.circum() << NEWLINE;



}
