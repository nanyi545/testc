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
    C1(double r){
        radius = r;
    };
    double circum();
};


#endif //TESTC2_C1_H
