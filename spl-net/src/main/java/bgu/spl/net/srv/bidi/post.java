package bgu.spl.net.srv.bidi;

import bgu.spl.net.srv.User;

public class post {

    private String content;
    private boolean isPublic;
    private User postingUser;

    public post(User _postingUser , String _content,boolean _isPublic ){
        this.content=_content;
        this.isPublic=_isPublic;
        this.postingUser=_postingUser;
    }

}
