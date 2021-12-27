package bgu.spl.net.srv.BGS.msg;

public class NotificationMsg {
    final short optCode;
    final PM_Public pmOrPublic;
    final String postingUser;
    final String content;


    enum PM_Public{
        PM,
        PUBLIC
    }

    public NotificationMsg(byte pmOrPublic, String postingUser, String content) {
        this.optCode = 9;
        // Todo need to validate byte?
        this.pmOrPublic = pmOrPublic == 0 ? PM_Public.PM : PM_Public.PUBLIC; //Todo compare byte with == ?;
        this.postingUser = postingUser;
        this.content = content;
    }

    public short getOptCode() {
        return optCode;
    }

    public PM_Public getPmOrPublic() {
        return pmOrPublic;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }
}
