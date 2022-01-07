//
// Created by USER on 2022-01-03.
//

#include "../include/socketTreadTask.h"
#include "../include/connectionHandler.h"


using namespace std;

socketTreadTask::socketTreadTask(ConnectionHandler &connectionHandler, bool* _amILogin) :
        handler(connectionHandler),
        amILogin(_amILogin){};

static short bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void socketTreadTask::operator()() {
    bool shouldTerminate=false;
    while (!shouldTerminate) {
        char currBytes[2];
        handler.getBytes(currBytes, 2);
        short optCode = bytesToShort(currBytes);

                if (optCode==9){ //Notification
                char optcodbyte[1];
                handler.getBytes(optcodbyte, 1);
                char type = optcodbyte[0];
                string NotificationType;
                    if (type == 0)
                        NotificationType="NOTIFICATION PM ";
                    else
                        NotificationType="NOTIFICATION Public ";

//                short notificationtype = (short) type;

                string name;
                handler.getFrameAscii(name, '\0');

                string content;
                handler.getFrameAscii(content, '\0');

                cout << NotificationType+ name.substr(1, name.length() - 1) + " " + content.substr(0, content.length() - 1) << endl;
                }


           else if (optCode==10) { // ACK
                handler.getBytes(currBytes, 2);
                short messageOptcode = bytesToShort(currBytes);

                if (messageOptcode == 2) //login
                        *amILogin=true;
                if (messageOptcode == 3) { //log out
                    shouldTerminate = true;
                    cout << "ACK " + std::string(std::to_string((int) messageOptcode)) << endl; // unnecessary?
                }

                else {
                    string optionalInfo;
                    handler.getFrameAscii(optionalInfo, ';');
                    cout << "ACK " + std::string(std::to_string((int) messageOptcode) )<< optionalInfo << endl;

                }
            }

        else if (optCode==11) { //Error
                handler.getBytes(currBytes, 2);
                short optcodeOfCommendThatFail = bytesToShort(currBytes);

                cout << "ERROR " + std::string(std::to_string((int) optcodeOfCommendThatFail)) << endl;

            }
            else {
                std::cout << "received unreadable message from bgs :"+optCode << std::endl;

            }
        }
    std::cout << "thread socket is closing..." << std::endl;
    }
