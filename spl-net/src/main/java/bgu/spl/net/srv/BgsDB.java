package bgu.spl.net.srv;

import bgu.spl.net.srv.BGS.msg.LogStatInfo;
import bgu.spl.net.srv.BGS.msg.NotificationMsg;
import bgu.spl.net.srv.bidi.post;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
        User userToAdd= new User(name , code , date, connectionId);
        users.putIfAbsent(name,userToAdd);
        usersById.putIfAbsent(connectionId, userToAdd);
        return true;
}

    public boolean logIn(String name, String code,int connectionId){
        if (! users.containsKey(name) ||(! code.equals(users.get(name).getPassword()) ) ||users.get(name).isloggedin())  // not register/Password doest match/ already logging
            return false;
        users.get(name).login();
        if (users.get(name).getConnectionID()!=connectionId)
            changeConnectionIdInListAndUser(connectionId,name);
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


    public boolean post(int connectionIdCurrUser, String content){ // cant assume  @ only for users
        if (!usersById.containsKey(connectionIdCurrUser)|| (!usersById.get(connectionIdCurrUser).isloggedin())) //curruser not register or not logged in
            return false;
        this.posts.add(new post(usersById.get(connectionIdCurrUser),content, null )); //save PM

        //Todo really send the PM

        return true;
    }

    public boolean sendPM(int connectionIdCurrUser,String userToSendToHim, String content, String sendingDateAndTime){ // content given by the protocol in already filtered
        if (!usersById.containsKey(connectionIdCurrUser)|| (!usersById.get(connectionIdCurrUser).isloggedin()) || ! users.containsKey(userToSendToHim)) //curruser not register or not logged in or userToSendToHim not register
            return false;
        User currUser=usersById.get(connectionIdCurrUser);
        if (currUser.isFollowingAfter(userToSendToHim)) // not Following
            return false;
        this.posts.add(new post(currUser,content, users.get(userToSendToHim) )); //save PM
        //Todo really send the PM
        return true;
    }

    public HashMap<User, LogStatInfo> logStat(int connectionIdCurrUser){
        if (!usersById.containsKey(connectionIdCurrUser)|| (!usersById.get(connectionIdCurrUser).isloggedin())) //curruser not register or not logged in
            return null;
        HashMap<User, LogStatInfo> mapToReturn=new HashMap<>();

        for( Map.Entry name: users.entrySet()) //todo make sure its ok
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

    public String getUsernameByConnectionID(int connectionID){
        return this.usersById.get(connectionID).getUsername();
    }

    public LinkedList<User> usersToSendNotificationDueToFollow(int senderConnectionID){
        LinkedList followersList =new LinkedList<User>();
        for (User curUser : usersById.get(senderConnectionID).getfollowers() )
            followersList.add(curUser.getUsername());
        return followersList;
    }


    public LinkedList<User> IDsToSendNotificationDueToTag(String content){
        //todo implement this- returns a list of users that need to get notifications due to Tag
        return null;
    }
    //for notifications

    public void addUnseenNotification(String username, NotificationMsg msg){
        this.users.get(username).addUnseenNotification(msg);
    }

    public User getUserByUsername(String username){
        return this.users.get(username);
    }

}
