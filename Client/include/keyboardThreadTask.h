//
// Created by USER on 2022-01-03.
//

#ifndef UNTITLED3_keyboardThreadTask_H
#define UNTITLED3_keyboardThreadTask_H


#include "connectionHandler.h"

class keyboardThreadTask{
public:
    keyboardThreadTask(ConnectionHandler &connectionHandler , bool* amILogin);
    void operator()();

private:
    ConnectionHandler &handler;
    bool *amILogin;
};




#endif //UNTITLED3_keyboardThreadTask_H
