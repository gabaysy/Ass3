package bgu.spl.net.srv.BGS.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BgsDB;

public class ErrorMsg implements Message{
    final short optcode;

    public ErrorMsg(short messageOptcode) {
        this.optcode = 11;
        this.messageOptcode = messageOptcode;
    }

    final short messageOptcode;

    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        //not implemented- relevant to server-to-client only

    }
    public String toString(){
        //Todo Implement this ?
        return "";
    }
}
