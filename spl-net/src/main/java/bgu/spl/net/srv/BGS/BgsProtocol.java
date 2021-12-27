package bgu.spl.net.srv.BGS;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BGS.msg.Message;
import bgu.spl.net.srv.BgsDB;

public class BgsProtocol implements BidiMessagingProtocol {


    private boolean shouldTerminate = false;
    private BgsDB db;

    public BgsProtocol(BgsDB db){
        this.db=db;
    }


    @Override
    public void start(int connectionId, Connections connections) {

    }

    @Override
    public void process(Object message) {
        Message msgToProcess=(Message)message;
        msgToProcess.process();
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}
