package bgu.spl.net.srv;

import bgu.spl.net.srv.BGS.msg.LogStatInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BgsDB {
    private Map<String, User> users;
    private int nextId;

    public BgsDB (){
        this.users=new HashMap();
        this.nextId=1;
    }



public boolean register (String name, String code, String date){
//        if(containName(name))
    if(users.containsKey(name))
            return false;
        users.putIfAbsent(name,new User(name , code , date));
        return true;
}

    public boolean logIn(String name, String code){
        if (! users.containsKey(name) ||(! code.equals(users.get(name).getPassword()) ) ||users.get(name).isloggedin())  // not register/Password doest match/ already logging
            return false;
        users.get(name).login();
        return true;
    }


    public boolean logout(String name){
        if (! users.containsKey(name)  ||! users.get(name).isloggedin())  // not register/not  logging
            return false;
        users.get(name).logout();
        return true;
    }

    public boolean logout(int connectionIdCurrUser){
         return false; //Todo implement this
    }

    public boolean follow(int connectionIdCurrUser, String usernameToFollow){
        return false; //Todo implement this

    }

    public boolean unFollow(int connectionIdCurrUser, String usernameToUnFollow) {
        return false; //Todo implement this
    }
    public boolean post(int connectionIdCurrUser, String content){
        return false; //Todo implement this
    }

    public boolean sendPM(String userToSendToHim, String content, String sendingDateAndTime){
        // content given by the protocol in already filtered
        return false; //Todo implement this
    }

    public HashMap<User, LogStatInfo> logStat(){
        //Todo implement this
        return null; //if need to send error return null. otherwise, return map with all relevant users and their info in a logStatInfo Object
    }
    public HashMap<User, LogStatInfo> stat(List<String> usernames){
        //Todo implement this
        return null; //if need to send error return null. otherwise, return map with all relevant users and their info in a logStatInfo Object
    }








//    private boolean containName(String name) {
//        for (User user: users) {
//            if (user.getUsername().equals(name))
//                return true;
//        }
//    return false;
//    }

}
