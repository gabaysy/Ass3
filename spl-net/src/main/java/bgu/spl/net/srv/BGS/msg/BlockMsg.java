package bgu.spl.net.srv.BGS.msg;

public class BlockMsg {
    final short optcode;
    final String username;

    public BlockMsg(String username) {
        this.optcode = 12;
        this.username = username;
    }
    public short getOptcode() {
        return optcode;
    }

    public String getUsername() {
        return username;
    }


}
