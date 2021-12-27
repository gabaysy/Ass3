package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BgsDB {
    private Map<Integer, User> users;
    private int nextId;

    public BgsDB (){
        this.users=new HashMap();
    }



public boolean register (String name, String code, String date){
        if(containName(name))
            return false;
        users.add(new User(name , code , date));
        return true;
}

    public boolean logIn(String name, String code){
        if (containName(name) ||(! code.equals() ))
    }








    private boolean containName(String name) {
        for (User user: users) {
            if (user.getUsername().equals(name))
                return true;
        }
    return false;
    }

}
