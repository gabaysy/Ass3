package bgu.spl.net.srv.BGS.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BgsDB;

public interface Message {
    public void process(BgsDB db, Connections connections, int connectionId);
}
