#include <iostream>
#include <stdlib.h>
#include <thread>
#include "../include/connectionHandler.h"
#include "../include/socketTreadTask.h"
#include "../include/keyboardThreadTask.h"
using boost::asio::ip::tcp;

/**
*/
int main (int argc, char *argv[]) {
    std::cerr << "hiii " << std::endl;
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    bool *amILogin = new bool ;
    *amILogin=false;
    socketTreadTask task1 = socketTreadTask(connectionHandler, amILogin);
    keyboardThreadTask task2 = keyboardThreadTask(connectionHandler, amILogin);
    std::thread t1(std::ref(task2));
    std::thread t2(std::ref(task1));

    t1.join();
    t2.join();
    delete amILogin;
    std::cout << "CLOSE PROGRAM PROPERLY" << std::endl; //DEBUG
    return 0;

}

    
    
