package bgu.spl.net.srv;

import bgu.spl.net.impl.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.MessageEncoderDecoder;
import bgu.spl.net.srv.BGS.BgsDB;

public class ReactorMain {
    public static void main(String[] args) {
        BgsDB DB = new BgsDB(); //one shared object

        Server.reactor(
                Integer.parseInt(args[1]),  //num of threads
                Integer.parseInt(args[0]), //port
                () -> new BidiMessagingProtocolImpl(DB), //protocol factory
                MessageEncoderDecoder::new //message encoder decoder factory
        ).serve();
    }
}