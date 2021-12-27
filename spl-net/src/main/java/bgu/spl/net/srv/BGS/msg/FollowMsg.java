package bgu.spl.net.srv.BGS.msg;

public class FollowMsg implements Message{
    final short optCode;
    final String username;
    final Follow_Unfollow follow_unfollow;


    enum Follow_Unfollow{
        FOLLOW,
        UNFOLLOW
    }

    public FollowMsg(byte follow_unfollow, String username) {
        this.optCode=4;
        // Todo validate follow_unfollow ??
        this.follow_unfollow = follow_unfollow == 0 ? Follow_Unfollow.FOLLOW : Follow_Unfollow.UNFOLLOW; //Todo compare byte with == ?
        this.username = username;
    }

    public short getOptCode() {
        return optCode;
    }

    public Follow_Unfollow getFollow_unfollow() {
        return follow_unfollow;
    }

    public String getUsername() {
        return username;
    }

}

