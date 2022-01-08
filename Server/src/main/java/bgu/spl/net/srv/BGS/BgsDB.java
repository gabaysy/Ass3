package bgu.spl.net.srv.BGS;

import bgu.spl.net.impl.msg.LogStatInfo;
import bgu.spl.net.impl.msg.NotificationMsg;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BgsDB {
    private ConcurrentHashMap<String, User> users;
    private ConcurrentHashMap<Integer, User> usersById;
    private ConcurrentLinkedQueue<post> posts;

    public BgsDB (){
        this.users=new ConcurrentHashMap();
        this.usersById=new ConcurrentHashMap();
        this.posts=new ConcurrentLinkedQueue<post>();
    }



public boolean register (String name, String code, String date,int connectionId){
    if(users.containsKey(name))
            return false;
        User userToAdd= new User(name , code , date, -1);
        users.putIfAbsent(name,userToAdd);
//        usersById.putIfAbsent(connectionId, userToAdd);
//        System.out.println("user "+userToAdd.getUsername()+ " is register"); //debug
        return true;
}

    public boolean logIn(String name, String code,int connectionId){
        if (! users.containsKey(name) ||(! code.equals(users.get(name).getPassword()) ) ||users.get(name).isloggedin())  // not register/Password doest match/ already logging
            return false;
        users.get(name).login();
        if (users.get(name).getConnectionID()!=connectionId)
            changeConnectionIdInListAndUser(connectionId,name);
//        System.out.println("user "+name+ " is logIn"); //debug
        return true;
    }

    private void changeConnectionIdInListAndUser(int newConnectionId ,String name) {
       User user= users.get(name);
       usersById.remove(user.getConnectionID());
        usersById.put(newConnectionId,user);
        user.setNewConnectionId(newConnectionId);
    }

    public NotificationMsg nextUnseenNotification(int connectionIdCurrUser){
                return this.usersById.get(connectionIdCurrUser).getNextUnseenNotification();
    }


//    public boolean logout(String name){
//        if (! users.containsKey(name)  ||! users.get(name).isloggedin())  // not register/not  logging
//            return false;
//        users.get(name).logout();
//        return true;
//    }

    public boolean logout(int connectionIdCurrUser){
        if (!usersById.containsKey(connectionIdCurrUser))
            return false;
        User user =usersById.get(connectionIdCurrUser);
        if (!user.isloggedin())
            return false;
        user.logout();
        usersById.remove(connectionIdCurrUser);
        user.setNewConnectionId(-1);
        return true;
    }

    public boolean follow(int connectionIdCurrUser, String usernameToFollow){
        if (!usersById.containsKey(connectionIdCurrUser))
            return false;
        User me =usersById.get(connectionIdCurrUser);
        User whoIwantToFollow= users.get(usernameToFollow);
        if (! users.containsKey(usernameToFollow)  || (! me.isloggedin())||( me.isBlocked(whoIwantToFollow)) )// not register/not logging/users I block
            return false;
        if(whoIwantToFollow.isBlocked(me))
            return false;//user who blocked me
        return me.follow(users.get(usernameToFollow));//false if already following

    }

    public boolean unFollow(int connectionIdCurrUser, String usernameToUnFollow) {
        if (!usersById.containsKey(connectionIdCurrUser))
        return false;
        User user =usersById.get(connectionIdCurrUser);
        if (! users.containsKey(usernameToUnFollow)  || (!user.isloggedin()))  // not register/not logging
            return false;
        return user.unfollow(users.get(usernameToUnFollow));//false if not following
    }


    public boolean post(int connectionIdCurrUser, String content){ // cant assume  @ only for users
        if (!usersById.containsKey(connectionIdCurrUser)|| (!usersById.get(connectionIdCurrUser).isloggedin())) //curruser not register or not logged in
            return false;
        this.posts.add(new post(usersById.get(connectionIdCurrUser),content, null )); //save PM
        usersById.get(connectionIdCurrUser).icrNumPost();
        //Todo really send the PM

        return true;
    }

    public boolean sendPM(int connectionIdCurrUser,String userToSendToHim, String content, String sendingDateAndTime){ // content given by the protocol in already filtered
        if (!usersById.containsKey(connectionIdCurrUser)|| (!usersById.get(connectionIdCurrUser).isloggedin()) || ! users.containsKey(userToSendToHim)) //curruser not register or not logged in or userToSendToHim not register
            return false;
        User currUser=usersById.get(connectionIdCurrUser);
        if (!currUser.isFollowingAfter(users.get(userToSendToHim)) )// not Following, Im not check if blocking bc block-> not follow
            return false;
        this.posts.add(new post(currUser,content, users.get(userToSendToHim) )); //save PM
        return true;
    }

    public HashMap<User, LogStatInfo> logStat(int connectionIdCurrUser){
        if (!usersById.containsKey(connectionIdCurrUser)|| (!usersById.get(connectionIdCurrUser).isloggedin())) //curruser not register or not logged in
            return null;
        HashMap<User, LogStatInfo> mapToReturn=new HashMap<>();
        User myUser=usersById.get(connectionIdCurrUser);
        for( Map.Entry name: users.entrySet()) //todo make sure its ok
        {
            User currUser = (User) name.getValue();
            if (currUser.isloggedin() && !myUser.isBlocked(currUser)) //only logged users and not blocked
            mapToReturn.put(currUser,new LogStatInfo(currUser.getAge(),currUser.getNumPost(),currUser.getNumOfFollowers(),currUser.getNumOfFolloweing()));
        }
        return mapToReturn;

    }

    public HashMap<User, LogStatInfo> stat(List<String> usernames,int connectionIdCurrUser){//if need to send error return null. otherwise, return map with all relevant users and their info in a logStatInfo Object
        if (!usersById.containsKey(connectionIdCurrUser)|| (!usersById.get(connectionIdCurrUser).isloggedin()))
            return null;
        HashMap<User, LogStatInfo> mapToReturn=new HashMap<>();
        User myUser=usersById.get(connectionIdCurrUser);
        for(String name : usernames) {
            User currUser=users.get(name);
            if (!users.containsKey(name) || myUser.isBlocked(currUser) || currUser.isBlocked(myUser)) {
                return null;
            } else {
             //   User currUser = users.get(name);
                mapToReturn.put(currUser, new LogStatInfo(currUser.getAge(), currUser.getNumPost(), currUser.getNumOfFollowers(), currUser.getNumOfFolloweing()));
            }
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

    public String getUsernameByConnectionID(int connectionID){
        return this.usersById.get(connectionID).getUsername();
    }

    public HashSet<User> usersToSendNotificationDueToFollow(int senderConnectionID){
        HashSet followersList =new HashSet<User>();
        User myUser= usersById.get(senderConnectionID);
        for (User curUser : usersById.get(senderConnectionID).getfollowers() ){
           // followersList.add(curUser.getUsername());
            //check blocking:
            if(!curUser.isBlocked(myUser) && !myUser.isBlocked(curUser)) {
                followersList.add(curUser);
            }
        }

        return followersList;
    }


    public HashSet<User> UsersToSendNotificationDueToTag(int senderConnectionID, String content){
        HashSet<User> toRet= new HashSet<>();
        String currname;
        User myUser= usersById.get(senderConnectionID);
        for (User curruser : this.users.values())
        {
            currname="@"+curruser.getUsername();
            if (content.contains(currname)){
                if(!curruser.isBlocked(myUser) && !myUser.isBlocked(curruser)) {
                    toRet.add(curruser);
                }
            }

        }
        return toRet;
    }
    //for notifications

    public void addUnseenNotification(String username, NotificationMsg msg){
        this.users.get(username).addUnseenNotification(msg);
    }

    public User getUserByUsername(String username){
        return this.users.get(username);
    }

}
