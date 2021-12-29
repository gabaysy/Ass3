package bgu.spl.net.srv.BGS.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BgsDB;
import bgu.spl.net.srv.User;

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

        //notification to users who follow me
        LinkedList<Integer> IDsToSendNotificationDueToFollow=db.IDsToSendNotificationDueToFollow(connectionId);
        for (int currID: IDsToSendNotificationDueToFollow) {
            NotificationMsg msgToSend= new NotificationMsg(
                    (byte) 1, //Public
                    db.getUsernameByConnectionID(connectionId), //posting user = this user
                    this.getContent()); //content
            if(db.isUserLoggedInByID(currID)) {
                connections.send(currID, msgToSend); //content
            }
            else {
                db.addUnseenNotification(currID,msgToSend);
            }
        }

        //notification to users I tagged
        LinkedList<Integer> IDsToSendNotificationDueToTag=db.IDsToSendNotificationDueToTag(this.content);
        for (int currID: IDsToSendNotificationDueToTag) {
            NotificationMsg msgToSend=new NotificationMsg(
                    (byte) 1, //Public
                    db.getUsernameByConnectionID(connectionId), //posting user = this user
                    this.getContent());
            if(db.isUserLoggedInByID(currID)) {
                connections.send(currID, msgToSend); //content
            }
            else {
                db.addUnseenNotification(currID,msgToSend);
            }
        }
    }
}

