package bgu.spl.net.srv.BGS.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BgsDB;

public class BlockMsg implements Message {
    final short optcode;
    final String username;

    public BlockMsg(String username) {
        this.optcode = 12;
        this.username = username;
    }

    public short getOptCode() {
        return optcode;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        boolean success = db.block(connectionId, this.getUsername());
        //response- ACK or error msg
        Message messageToReturn =
                success ?
                        new ACKMsg(this.getOptCode()) :
                        new ErrorMsg(this.getOptCode());
        connections.send(connectionId, messageToReturn);
    }
}
