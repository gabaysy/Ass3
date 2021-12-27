package bgu.spl.net.srv.BGS.msg;

public class LogoutMsg implements Message{
    final short optCode;


    public LogoutMsg() {
        this.optCode = 3;
    }

    @Override
    public void process() {

    }
}

