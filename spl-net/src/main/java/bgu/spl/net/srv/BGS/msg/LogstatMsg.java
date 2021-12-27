package bgu.spl.net.srv.BGS.msg;

public class LogstatMsg implements Message{
    final short optCode;

    public LogstatMsg() {
        this.optCode = 7;
    }

    @Override
    public void process() {

    }
}
