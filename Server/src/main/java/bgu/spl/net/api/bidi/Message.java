package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.BGS.BgsDB;

public interface Message {
    public void process(BgsDB db, Connections connections, int connectionId);
    public short getOptCode();
}
