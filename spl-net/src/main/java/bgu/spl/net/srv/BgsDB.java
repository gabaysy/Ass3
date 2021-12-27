package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.LinkedList;
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
        if (containName(name) ||(! code.equals(users.get(name).getPassword()) ) ||users.get(name).isloggedin())  // not register/Password doest match/ already logging
            return false;
        users.get(name).login();
        return true;
    }








    private boolean containName(String name) {
        for (User user: users) {
            if (user.getUsername().equals(name))
                return true;
        }
    return false;
    }

}
