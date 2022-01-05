package bgu.spl.net.impl.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.BGS.BgsDB;

public class LoginMsg implements Message {
    final short optCode;
    final String username;
    final String password;
    final byte captcha;


    public LoginMsg(String username, String password, byte captcha) {
        this.optCode = 2;
        this.username = username;
        this.password = password;
        this.captcha = captcha;
    }

    public short getOptCode() {
        return optCode;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public byte getCaptcha() {
        return captcha;
    }

    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        if(this.getCaptcha()==0){
            connections.send(connectionId, new ErrorMsg(this.getOptCode()));
            return;
        }
        boolean success=db.logIn(this.getUsername(),this.getPassword(),connectionId);
        //response- ACK or error msg
        Message messageToReturn=
                success ?
                new ACKMsg(this.getOptCode()) :
                new ErrorMsg(this.getOptCode());
        connections.send(connectionId,messageToReturn);

        //send unseen notifications
        if(success){
            NotificationMsg msg=db.nextUnseenNotification(connectionId);
            while (msg!=null){
                connections.send(connectionId,msg);
                msg=db.nextUnseenNotification(connectionId);
            }
        }
    }
}
