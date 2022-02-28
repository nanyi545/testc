//
// Created by wei wang on 2022-02-25.
//

#ifndef TESTC2_C1_H
#define TESTC2_C1_H

// a circle class ...

class C1 {
private:
    double radius;
public:
    // you can choose to put the implementation in header file or in implementation ....

    // method1
//    C1(double r){radius = r;};

    // method2
    C1(double r):radius(r) { };    //  member initialization
    // This is done by inserting, before the constructor's body, a colon (:) and a list of initializations for class members.

    // no param constructor
    C1():radius(0){};

    double circum();

    C1 operator + (const C1& o2) {
        C1 temp;
        temp.radius =  temp.radius + o2.radius;
        return temp;
    }

};


#endif //TESTC2_C1_H
