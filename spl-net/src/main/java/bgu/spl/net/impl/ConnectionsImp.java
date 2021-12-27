package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.HashMap;

public class ConnectionsImp implements Connections {

    private HashMap <Integer, ConnectionHandler> connectionHandlerHashMap ;

    public ConnectionsImp(){
        this.connectionHandlerHashMap=new HashMap<>();
    }

    @Override
    public boolean send(int connectionId, Object msg) {
        ConnectionHandler CH=connectionHandlerHashMap.get(connectionId);
        if(CH==null)
            return false;
        CH.send(msg);
        return true;
    }

    @Override
    public void broadcast(Object msg) {
        for (ConnectionHandler CH: connectionHandlerHashMap.values()){ //Todo check
            CH.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        if(connectionHandlerHashMap.get(connectionId)!=null)
            connectionHandlerHashMap.remove(connectionId);
    }
}
