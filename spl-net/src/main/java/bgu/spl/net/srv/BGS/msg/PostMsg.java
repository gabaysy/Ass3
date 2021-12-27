package bgu.spl.net.srv.BGS.msg;

public class PostMsg implements Message{
    final short optCode;
    final String content;

    public PostMsg(String content) {
        this.optCode = 5;
        this.content = content;
    }

    public short getOptCode() {
        return optCode;
    }

    public String getContent() {
        return content;
    }

}

