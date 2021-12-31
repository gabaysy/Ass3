package bgu.spl.net.srv.bidi;

import bgu.spl.net.srv.User;

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
