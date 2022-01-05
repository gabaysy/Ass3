package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.BGS.BgsDB;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol {


    private boolean shouldTerminate = false;
    private BgsDB db;
    private Connections connections;
    private int connectionId;

    public BidiMessagingProtocolImpl(BgsDB db){
        this.db=db;
    }


    @Override
    public void start(int _connectionId, Connections _connections) {
        this.connectionId=_connectionId;
        this.connections=_connections;
    }

    @Override
    public void process(Object message) {
        Message msgToProcess=(Message)message;
        msgToProcess.process(db, connections , connectionId);
        if(msgToProcess.getOptCode()==3) {
            shouldTerminate = true;
            connections.disconnect(connectionId);
        }
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
