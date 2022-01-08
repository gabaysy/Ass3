package bgu.spl.net.impl.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.BGS.BgsDB;
import bgu.spl.net.srv.BGS.User;

import java.util.HashSet;
import java.util.LinkedList;

public class PostMsg implements Message {
    final short optCode;
    final String content;

    public PostMsg(String content) {
        this.optCode = 5;
        this.content = content;
    }

    public short getOptCode() {
        return optCode;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        boolean success = db.post(connectionId, this.getContent());
        //response- ACK or error msg
        Message messageToReturn =
                success ?
                        new ACKMsg(this.getOptCode()) :
                        new ErrorMsg(this.getOptCode());
        connections.send(connectionId, messageToReturn);
        if(!success)
            return;
        //notification to users who follow me
        HashSet<User> usersToSendNotificationDueToFollow=db.usersToSendNotificationDueToFollow(connectionId);
        if(usersToSendNotificationDueToFollow!=null && !usersToSendNotificationDueToFollow.isEmpty()) {
            for (User currUser : usersToSendNotificationDueToFollow) {
                NotificationMsg msgToSend = new NotificationMsg(
                        (byte) 1, //Public
                        db.getUsernameByConnectionID(connectionId), //posting user = this user
                        this.getContent()); //content
                if (currUser.isloggedin()) {
                    connections.send(currUser.getConnectionID(), msgToSend); //content
                } else {
                    db.addUnseenNotification(currUser.getUsername(), msgToSend);
                }
            }
        }

        //notification to users I tagged
        HashSet<User> UsersToSendNotificationDueToTag=db.UsersToSendNotificationDueToTag(connectionId, this.content);
        boolean toSend;
        if(UsersToSendNotificationDueToTag!=null && !UsersToSendNotificationDueToTag.isEmpty()) {
            for (User currUser : UsersToSendNotificationDueToTag) {
                toSend=true;
                //check not to send notification again
                if (usersToSendNotificationDueToFollow != null && !usersToSendNotificationDueToFollow.isEmpty()) {
                    if (usersToSendNotificationDueToFollow.contains(currUser)) {
                        toSend = false;
                    }
                }
                if(toSend){
                    NotificationMsg msgToSend = new NotificationMsg(
                            (byte) 1, //Public
                            db.getUsernameByConnectionID(connectionId), //posting user = this user
                            this.getContent());
                    if (currUser.isloggedin()) {
                        connections.send(currUser.getConnectionID(), msgToSend); //content
                    } else {
                        db.addUnseenNotification(currUser.getUsername(), msgToSend);
                    }
                }
                }
            }
        }
    }


