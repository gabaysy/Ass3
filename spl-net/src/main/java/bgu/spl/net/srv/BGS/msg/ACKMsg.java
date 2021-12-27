package bgu.spl.net.srv.BGS.msg;

import java.util.Optional;

public class ACKMsg {
    final short optcode;
    final short messageOptcode;
    private Optional<String> additionalDatal; //Todo

    public ACKMsg(short messageOptcode, Optional<String> additionalDatal) {
        this.optcode = 10;
        this.messageOptcode = messageOptcode;
        this.additionalDatal = additionalDatal;
    }
}
