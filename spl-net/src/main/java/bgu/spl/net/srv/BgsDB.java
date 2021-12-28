package bgu.spl.net.srv;

import bgu.spl.net.srv.BGS.msg.LogStatInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BgsDB {
    private ConcurrentHashMap<String, User> users;
    private int nextId;
    private ConcurrentHashMap<Integer, User> usersById;

    public BgsDB (){
        this.users=new ConcurrentHashMap();
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
        if (!usersById.containsKey(connectionIdCurrUser))
            return false;
        User user =usersById.get(connectionIdCurrUser);
        if (!user.isloggedin())
            return false;
        user.logout();
        return true;
    }

    public boolean follow(int connectionIdCurrUser, String usernameToFollow){
        if (!usersById.containsKey(connectionIdCurrUser))
            return false;
        User user =usersById.get(connectionIdCurrUser);
        if (! users.containsKey(usernameToFollow)  || (!user.isloggedin())||(user.isBlocked(users.get(usernameToFollow) )) )// not register/not logging/blocked
            return false;
        return user.follow(users.get(usernameToFollow));//false if already following

    }

    public boolean unFollow(int connectionIdCurrUser, String usernameToUnFollow) {
        if (!usersById.containsKey(connectionIdCurrUser))
        return false;
        User user =usersById.get(connectionIdCurrUser);
        if (! users.containsKey(usernameToUnFollow)  || (!user.isloggedin()))  // not register/not logging
            return false;
        return user.unfollow(users.get(usernameToUnFollow));//false if not following
    }


    public boolean post(int connectionIdCurrUser, String content){
        return false; //Todo implement this
    }

    public boolean sendPM(String userToSendToHim, String content, String sendingDateAndTime){
        // content given by the protocol in already filtered
        return false; //Todo implement this
    }

    public HashMap<User, LogStatInfo> logStat(int connectionIdCurrUser){
        if (!usersById.containsKey(connectionIdCurrUser)|| (!usersById.get(connectionIdCurrUser).isloggedin()))
            return null;
        HashMap<User, LogStatInfo> mapToReturn=new HashMap<>();

        for( Map.Entry name: users.entrySet()) //todo
        {
            User currUser = (User) name.getValue();
            mapToReturn.put(currUser,new LogStatInfo(currUser.getAge(),currUser.getNumPost(),currUser.getNumOfFollowers(),currUser.getNumOfFolloweing()));
        }
        return mapToReturn;

    }

    public HashMap<User, LogStatInfo> stat(List<String> usernames,int connectionIdCurrUser){//if need to send error return null. otherwise, return map with all relevant users and their info in a logStatInfo Object
        if (!usersById.containsKey(connectionIdCurrUser)|| (!usersById.get(connectionIdCurrUser).isloggedin()))
            return null;
        HashMap<User, LogStatInfo> mapToReturn=new HashMap<>();

        for(String name : usernames){
            User currUser =users.get(name);
            mapToReturn.put(currUser,new LogStatInfo(currUser.getAge(),currUser.getNumPost(),currUser.getNumOfFollowers(),currUser.getNumOfFolloweing()));
        }
    return mapToReturn;
    }

    public boolean block(int connectionIdCurrUser, String usernameToblock){
        if (!usersById.containsKey(connectionIdCurrUser))
            return false;
        User user =usersById.get(connectionIdCurrUser);
        if (! users.containsKey(usernameToblock)  || (!user.isloggedin()))  // not register/not logging
            return false;
        User userToBlock=users.get(usernameToblock);

        user.unfollow(userToBlock);
        userToBlock.unfollow(user);
        user.addToBlocked(userToBlock);
        return true;
    }






//    private boolean containName(String name) {
//        for (User user: users) {
//            if (user.getUsername().equals(name))
//                return true;
//        }
//    return false;
//    }

}
