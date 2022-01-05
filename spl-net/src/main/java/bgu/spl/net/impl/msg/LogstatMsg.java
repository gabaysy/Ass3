package bgu.spl.net.impl.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.BGS.BgsDB;
import bgu.spl.net.srv.BGS.User;

import java.util.HashMap;

public class LogstatMsg implements Message {
    final short optCode;

    public LogstatMsg() {
        this.optCode = 7;
    }

    public short getOptCode() {
        return optCode;
    }

    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        HashMap<User, LogStatInfo> logStatsInfo= db.logStat(connectionId);// added parameter
        if(logStatsInfo!=null){ //success
            for (LogStatInfo curr: logStatsInfo.values()) {//Todo make sure .values gets all values
                connections.send(connectionId,new ACKMsg(this.getOptCode(),curr.StatToString()));
            }
        }
        else //not success
            connections.send(connectionId,new ErrorMsg(this.getOptCode()));
    }
}
