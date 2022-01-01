package bgu.spl.net.srv.BGS.msg;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BGS.FilteredWords;
import bgu.spl.net.srv.BgsDB;
import bgu.spl.net.srv.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class PMMsg implements Message{
    final short optCode;
    final String username;
    final String content;
    final String sendingDateTime;


    public PMMsg(String username, String content) {
        this.optCode = 6;
        this.username = username;
        this.content=filterContent(content);
        this.sendingDateTime=new SimpleDateFormat("dd:MM:yyyy HH:mm").format(new Date());
    }

    public short getOptCode() {
        return optCode;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }


    public String getSendingDateTime() {
        return sendingDateTime;
    }

    private String filterContent(String content){
        String filterContent=content;
        Set<String> wordsToFilter=FilteredWords.wordsToFilter();
        for (String forbiddenWord: wordsToFilter) {
            if(content.contains(forbiddenWord)){
                filterContent=filterContent.replaceAll(forbiddenWord,"<filtered>");
            }
        }
        return filterContent;
    }

    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        boolean success = db.sendPM(connectionId, this.getUsername(), this.getContent(), this.getSendingDateTime()); //added parameter
        //response- ACK or Error
        Message messageToReturn =
                success ?
                        new ACKMsg(this.getOptCode()) :
                        new ErrorMsg(this.getOptCode());
        connections.send(connectionId, messageToReturn);

        //notification
        User userToSendNotification=db.getUserByUsername(username);
        NotificationMsg msgToSend=new NotificationMsg(
                (byte) 0, //PM
                db.getUsernameByConnectionID(connectionId), //posting user = this user
                this.getContent()); //content
        if(userToSendNotification.isloggedin()){
            connections.send(userToSendNotification.getConnectionID(),msgToSend);
        }
        else {
            db.addUnseenNotification(userToSendNotification.getUsername(),msgToSend);
        }
    }
}

