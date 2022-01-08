package bgu.spl.net.srv;

import java.util.function.Supplier;

public class ServerTPC extends BaseServer {


    public ServerTPC(int port, Supplier protocolFactory, Supplier encdecFactory) {
        super(port, protocolFactory, encdecFactory);
    }

    @Override
    protected void execute(BlockingConnectionHandler handler) {
        Thread t= new Thread(handler);
        t.start();

    }
}
