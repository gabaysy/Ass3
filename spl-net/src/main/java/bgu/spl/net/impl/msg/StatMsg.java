package bgu.spl.net.impl.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.BGS.BgsDB;
import bgu.spl.net.srv.BGS.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class StatMsg implements Message {
    final short optCode;
    final String ListOfUsernames;
    final List<String> seperatedUsernames;

    public StatMsg(String listOfUsernames) {
        this.optCode = 8;
        ListOfUsernames = listOfUsernames;
        seperatedUsernames = new LinkedList<>();
        String[] parts = listOfUsernames.split("\\|"); //Todo check \\|
        for (String username: parts
             ) {
            seperatedUsernames.add(username);
        }
    }

    public short getOptCode() {
        return optCode;
    }

    public String getListOfUsernames() {
        return ListOfUsernames;
    }

    public List<String> getSeperatedUsernames() {
        return seperatedUsernames;
    }

    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        HashMap<User, LogStatInfo> statsInfo= db.stat(this.getSeperatedUsernames(),connectionId);//added parameter
        if(statsInfo!=null){ //success
            for (LogStatInfo curr: statsInfo.values()) {//Todo make sure .values gets all values
                connections.send(connectionId,new ACKMsg(this.getOptCode(),curr.StatToString()));
            }
        }
        else //not success
            connections.send(connectionId,new ErrorMsg(this.getOptCode()));
    }
}
