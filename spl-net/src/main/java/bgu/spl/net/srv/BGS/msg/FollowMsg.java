package bgu.spl.net.srv.BGS.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BgsDB;

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

    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        boolean success= this.getFollow_unfollow()==Follow_Unfollow.FOLLOW ?
                db.follow(connectionId, this.getUsername())
                : db.unFollow(connectionId, this.getUsername());
        if(success){
            connections.send(connectionId,new ACKMsg(this.getOptCode(), this.getUsername()));
        }
        else
            connections.send(connectionId,new ErrorMsg(this.getOptCode()));
    }
}

