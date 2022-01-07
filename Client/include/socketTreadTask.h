//
// Created by USER on 2022-01-03.
//

#ifndef UNTITLED3_SOCKETTREADTASK_H
#define UNTITLED3_SOCKETTREADTASK_H


#include "connectionHandler.h"

class socketTreadTask{
public:
    socketTreadTask(ConnectionHandler &connectionHandler, bool* shouldTerminate );
    void operator()();

private:
    ConnectionHandler &handler;
    bool *amILogin;


};

#endif //UNTITLED3_SOCKETTREADTASK_H
