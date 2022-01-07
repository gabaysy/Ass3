//
// Created by USER on 2022-01-03.
//

#include "../include/keyboardThreadTask.h"
#include "../include/connectionHandler.h"
#include <boost/algorithm/string.hpp>

using boost::asio::ip::tcp;


keyboardThreadTask::keyboardThreadTask(ConnectionHandler &connectionHandler) :
       handler(connectionHandler),
       shouldTerminate (false){};

using namespace std;

static void shortToBytes(short num, char* bytesArr) // from assi
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

void keyboardThreadTask::operator()() {

    while (!shouldTerminate  ) {//REGISTER YONI ABC 01-12-1990 LOGIN YONI ABC 1
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);

        std::vector<std::string> words;
        std::vector<char> toRet;

        boost::split(words, line, boost::is_any_of(" "));
        string currtWord = words.front();


        char *currOptcode = new char[2];
        char *endline = new char[1];
        endline[0] = ';';
        bool toSendEndline = true;

        if (!words.empty()) {
            if (currtWord == "REGISTER") { // todo
                shortToBytes((short) 1, currOptcode);
                handler.sendBytes(currOptcode, 2);
                handler.sendFrameAscii(words.at(1), '\0'); //userName
                handler.sendFrameAscii(words.at(2), '\0'); //password
                handler.sendFrameAscii(words.at(3), '\0'); //birthday


            } else if (currtWord.compare("LOGIN") == 0) {
                shortToBytes((short) 2, currOptcode);
                handler.sendBytes(currOptcode, 2);

                handler.sendFrameAscii(words.at(1), '\0'); //userName
                handler.sendFrameAscii(words.at(2), '\0'); //password
                char *captcha = new char[1];
                if (words.at(3).compare("1") == 0) {
                    captcha[0] = 1;
                    handler.sendBytes(captcha, 1);
                }
                delete captcha;
//             //       handler.sendBytes(currOptcode, 2);
//
//                    shortToBytes((short) 1, currOptcode); //todo make sure Captcha
//                    handler.sendBytes(currOptcode, 2);
//                    string endline=";";
//                    handler.sendLine(endline);

                //                  handler.sendBytes(currOptcode, 1);

            } else if ((currtWord.compare("LOGOUT") == 0)) {
                shortToBytes((short) 3, currOptcode);
                handler.sendBytes(currOptcode, 2);
                shouldTerminate = true;
            } else if ((currtWord.compare("FOLLOW") == 0)) {
                shortToBytes((short) 4, currOptcode);
                handler.sendBytes(currOptcode, 2);
                shortToBytes((short) 0, currOptcode); // todo make sure its ok to use currOptcode twice
                handler.sendFrameAscii(words[1], '\0'); //username
            } else if ((currtWord.compare("UNFOLLOW") == 0)) {
                shortToBytes((short) 4, currOptcode);
                handler.sendBytes(currOptcode, 2);
                shortToBytes((short) 1, currOptcode); // todo make sure its ok to use currOptcode twice
                handler.sendFrameAscii(words[1], '\0'); //username
            } else if ((currtWord.compare("POST") == 0)) {
                shortToBytes((short) 5, currOptcode);
                handler.sendBytes(currOptcode, 2);
//                    string content = words[1];
                string content = "";
                for (unsigned int i = 1; i < words.size(); i++) {
                    content = content + words[i] + " ";
                }
                handler.sendFrameAscii(content, '\0'); //content
            } else if ((currtWord.compare("PM") == 0)) {
                shortToBytes((short) 6, currOptcode);
                handler.sendBytes(currOptcode, 2);
                handler.sendFrameAscii(words.at(1), '\0'); //userName
                string content = "";
                for (unsigned int i = 1; i < words.size(); i++) {
                    content = content + words[i] + " ";
                }
                handler.sendFrameAscii(content, '\0'); //content
            } else if ((currtWord.compare("LOGSTAT") == 0)) {
                shortToBytes((short) 7, currOptcode);
                handler.sendBytes(currOptcode, 2);
            } else if ((currtWord.compare("STAT") == 0)) {
                shortToBytes((short) 8, currOptcode);
                handler.sendBytes(currOptcode, 2);

                string usersNames;
                vector<string>::iterator it;
                it = words.begin();
                it++;
                for (; it != words.end(); it++) { // add names to one string with '|'
                    usersNames += *it + "|";
                }
                handler.sendFrameAscii(usersNames, '\0');
            } else if ((currtWord.compare("BLOCK") == 0)) {
                shortToBytes((short) 8, currOptcode);
                handler.sendBytes(currOptcode, 2);
                string userName = words[1];
                handler.sendFrameAscii(userName, '\0');
            } else {
                toSendEndline = false;
                cout << "TRY A DIFFERENT COMMEND" << endl;

            }
        }
        if (toSendEndline) {
        handler.sendBytes(endline, 1);
    }
               delete currOptcode;
               delete endline;


        }
    std::cout << "thread KEYBOARD is closing..." << std::endl;
    }


