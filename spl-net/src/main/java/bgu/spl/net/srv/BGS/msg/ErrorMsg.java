package bgu.spl.net.srv.BGS.msg;

public class ErrorMsg implements Message{
    final short optcode;

    public ErrorMsg(short messageOptcode) {
        this.optcode = 11;
        this.messageOptcode = messageOptcode;
    }

    final short messageOptcode;

    @Override
    public void process() {

    }
}
