package bgu.spl.net.srv.BGS.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BgsDB;

import java.util.Optional;

public class ACKMsg implements Message{
    final short optcode;
    final short messageOptcode;
    private Optional<String> additionalData;


    public ACKMsg(short messageOptcode) {
        this.optcode = 10;
        this.messageOptcode = messageOptcode;
    }

    public ACKMsg(short messageOptcode ,String additionalData) {
        this.optcode = 10;
        this.messageOptcode = messageOptcode;
        this.additionalData = Optional.of(additionalData);
    }

    public short getOptcode() {
        return optcode;
    }

    public short getMessageOptcode() {
        return messageOptcode;
    }

    public String getAdditionalData() {
        return additionalData.get();
    }

    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        //not implemented- relevant to server-to-client only
    }

    public String toString(){
        String ans= this.getOptcode() +" "+ this.getMessageOptcode();
        if(this.additionalData.isPresent())
            ans+=" "+additionalData.get();
        return ans;
    }
}
