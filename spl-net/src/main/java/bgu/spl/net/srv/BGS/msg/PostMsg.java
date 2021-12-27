package bgu.spl.net.srv.BGS.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BgsDB;

public class PostMsg implements Message{
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
        boolean success= db.post(connectionId,this.getContent());
        if(success){
            connections.send(connectionId,new ACKMsg(this.getOptCode()));
        }
        else
            connections.send(connectionId,new ErrorMsg(this.getOptCode()));
    }
}


