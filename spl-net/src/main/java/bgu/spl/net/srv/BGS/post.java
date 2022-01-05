package bgu.spl.net.srv.BGS;

import bgu.spl.net.srv.BGS.User;

public class post {

    final private String content;
    final private User postingUser;
    final private User userReceiving;


    public post(User _postingUser , String _content,User _sentTo ){
        this.content=_content;
        this.userReceiving=_sentTo;
        this.postingUser=_postingUser;
    }

    private boolean isPM() {
        return(userReceiving!=null);
    }
    private boolean isPublic() {
        return(userReceiving==null);
    }

    public String getContent() {
        return content;
    }

    public User getUserReceiving() {
        return userReceiving;
    }

    public User getPostingUser() {
        return postingUser;
    }
}
