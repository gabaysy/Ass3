//
// Created by USER on 2022-01-03.
//

#ifndef UNTITLED3_keyboardThreadTask_H
#define UNTITLED3_keyboardThreadTask_H


#include "connectionHandler.h"

class keyboardThreadTask{
public:
    keyboardThreadTask(ConnectionHandler &connectionHandler);
    void operator()();

private:
    ConnectionHandler &handler;
    bool *shouldTerminate; //shouldTerminate
};




#endif //UNTITLED3_keyboardThreadTask_H
