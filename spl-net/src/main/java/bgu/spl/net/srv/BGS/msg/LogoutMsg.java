package bgu.spl.net.srv.BGS.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BgsDB;

public class LogoutMsg implements Message{


    final short optCode;

    public LogoutMsg() {
        this.optCode = 3;
    }

    public short getOptCode() {
        return optCode;
    }
    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        boolean success= db.logout(connectionId);
        if(success){
            connections.send(connectionId,new ACKMsg(this.getOptCode()));
        }
        else
            connections.send(connectionId,new ErrorMsg(this.getOptCode()));
    }
}

