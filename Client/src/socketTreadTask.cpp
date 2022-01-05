//
// Created by USER on 2022-01-03.
//

#include "../include/socketTreadTask.h"
#include "../include/connectionHandler.h"


using namespace std;

socketTreadTask::socketTreadTask(ConnectionHandler &connectionHandler) : handler(
        connectionHandler){shouldTerminate=false;}

static short bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void socketTreadTask::operator()() {
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

                cout << NotificationType+ name.substr(0, name.length() - 1) + " " + content.substr(0, content.length() - 1) << endl;
                }


                else   if (optCode==10) { // ACK
                handler.getBytes(currBytes, 2);
                short messageOptcode = bytesToShort(currBytes);

                if (messageOptcode == 3) { //log out
                    shouldTerminate = true;
                    cout << "ACK " + std::string(std::to_string((int) messageOptcode)) << endl;
                    break;
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
                break;
            }
            else {
                std::cout << "received unreadable message from bgs" << std::endl;
                break;
            }
        }
    }





//    if (messageOptcode==4) { //follow / unfollow
//        char moreBytes[2];
//        handler.getBytes(moreBytes, 2);
//        short numofUsersToFollow = socketTreadTask::bytesToShort(moreBytes, 0, 1);
//        string collectingUsers = "";
//        string toAdd = "";
//        for (int i = 0; i < numofUsersToFollow - 1; i++) {
//            toAdd = "";
//            handler.getFrameAscii(toAdd, '\0');
//            toAdd = toAdd.substr(0, toAdd.length() - 1);
//            collectingUsers.append(toAdd + " ");
//        }
//        toAdd = "";
//        handler.getFrameAscii(toAdd, '\0');
//        toAdd = toAdd.substr(0, toAdd.length() - 1);
//        collectingUsers.append(toAdd);
//        cout << "ACK " + std::string(std::to_string((int) ackmessageOpcode)) + std::string(" ") +
//                std::string(std::to_string((int) numofUsersToFollow)) + " " + collectingUsers << endl;
//        break;
//    }
//    if (messageOptcode==7) { // logstat
//        char moreBytes[2];
//        handler.getBytes(moreBytes, 2);
//        short numofUsersToFollow = socketTreadTask::bytesToShort(moreBytes, 0, 1);
//        string collectingUsers = "";
//        string toAdd = "";
//        for (int i = 0; i < numofUsersToFollow - 1; i++) {
//            toAdd = "";
//            handler.getFrameAscii(toAdd, '\0');
//            toAdd = toAdd.substr(0, toAdd.length() - 1);
//            collectingUsers.append(toAdd + " ");
//        }
//        toAdd = "";
//        handler.getFrameAscii(toAdd, '\0');
//        toAdd = toAdd.substr(0, toAdd.length() - 1);
//        collectingUsers.append(toAdd);
//        cout << "ACK " + std::string(std::to_string((int) ackmessageOpcode)) + std::string(" ") +
//                std::string(std::to_string((int) numofUsersToFollow)) + " " + collectingUsers
//             << endl;
//        break;
//    }
//    if (messageOptcode==8) { //stat
//        char moreBytes[6];
//        handler.getBytes(moreBytes, 6);
//        short numofPosts = socketTreadTask::bytesToShort(moreBytes, 0, 1);
//        short numOfFollowers = socketTreadTask::bytesToShort(moreBytes, 2, 3);
//        short numOfFollowing = socketTreadTask::bytesToShort(moreBytes, 4, 5);
//        string str1 = (to_string((int) numofPosts));
//        string str2 = (to_string((int) numOfFollowers));
//        string str3 = (to_string((int) numOfFollowing));
//        cout << "ACK " + std::string(std::to_string((int) ackmessageOpcode)) + " " + str1 + " " + str2 +
//                " " + str3 << endl;
//        break;
//    }
//    else
//        cout << "ACK " + std::string(std::to_string((int) ackmessageOpcode)) << endl;
//    break;
//}