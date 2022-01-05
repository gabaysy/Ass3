package bgu.spl.net.impl.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.BGS.BgsDB;

public class LogoutMsg implements Message {


    final short optCode;

    public LogoutMsg() {
        this.optCode = 3;
    }

    public short getOptCode() {
        return optCode;
    }
    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        boolean success = db.logout(connectionId);
        //response- ACK or error msg
        Message messageToReturn =
                success ?
                new ACKMsg(this.getOptCode()) :
                new ErrorMsg(this.getOptCode());
        connections.send(connectionId, messageToReturn);
    }
}

