package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsImp implements Connections {

    private ConcurrentHashMap <Integer, ConnectionHandler> connectionHandlerHashMap ;
    private AtomicInteger nextID;

    public ConnectionsImp(){
        this.connectionHandlerHashMap=new ConcurrentHashMap<>();
        nextID=new AtomicInteger(1);
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



    public synchronized int addNewHandler(ConnectionHandler CH){
        int toRet=nextID.intValue();
        this.connectionHandlerHashMap.put(toRet,CH);
        nextID.getAndIncrement();
        return toRet;
    }
}
