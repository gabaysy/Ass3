package bgu.spl.net.srv.BGS;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

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

    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public void processRegister(){};
    public void processLogin(){};
    public void processLogout(){};
    public void processFollow(){};
    public void processPost(){};
    public void processPM(){};
    public void processLogStat(){};
    public void processStat(){};
    public void processNotification(){};
    public void processAck(){};
    public void processError(){};
    public void processBlock(){};
}
