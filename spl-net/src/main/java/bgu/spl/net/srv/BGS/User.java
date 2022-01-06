package bgu.spl.net.srv.BGS;

import bgu.spl.net.impl.msg.NotificationMsg;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class User {


    private String username;
    private String password;
    private String birthday;
    private List<User> followers;
    private List<User> followings;
    private List<User> blocked;//
    private Integer connectionID;

    private int age;
    private int NumPost;
    //   private boolean loggedin;
    private AtomicBoolean loggedin; //use Atomic?

    private ConcurrentLinkedDeque<NotificationMsg> unSeenNotifications;

    public User(String name, String code, String date, int connectionID ){
        this.username=name;
        this.password=code;
        this.birthday=date;
        this.followers=new LinkedList<>() ;
        this.followings=new LinkedList<>() ;
        this.blocked=new LinkedList<>();
        this.loggedin=new AtomicBoolean(false);
        this.connectionID=connectionID;
        this.unSeenNotifications=new ConcurrentLinkedDeque<NotificationMsg>();
        
        int birthYear= Integer.parseInt(this.birthday.substring(6,9)); //todo make sure index
        this.age=(2022-birthYear);
    }

    public boolean login(){
        return loggedin.compareAndSet(false,true);
//        if (loggedin)
//            return false;
//        loggedin=true;
//        return true;
    }

    public boolean logout(){
        return loggedin.compareAndSet(true, false);
//        if (!loggedin)
//            return false;
//        loggedin=false;
//        return false;
    }

    public int getConnectionID() {
        return connectionID;
    }
    public List<User> getfollowers() {
        return followers;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge() {

        return this.age;

    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNumPost() {
        return NumPost;
    }

    public void setNumPost(int numPost) {
        NumPost = numPost;
    }


    public boolean isloggedin() {
        return loggedin.get();
    }

    public boolean isFollowingAfter(String name) {
        return (followings.contains(name));

    }

    public boolean follow(User usernameToFollow) {
        if ( isFollowingAfter(usernameToFollow.getUsername()) )
            return false;
        synchronized (this.followings){
        this.followings.add(usernameToFollow);}
        synchronized ( usernameToFollow.followers){
        usernameToFollow.followers.add(this);}
        return true;
    }

    public boolean unfollow(User usernameToUnFollow) {
        if ( !isFollowingAfter(usernameToUnFollow.getUsername()) )
            return false;
        synchronized(this.followings){
        this.followings.remove(usernameToUnFollow);}
        synchronized(usernameToUnFollow.followers){
        usernameToUnFollow.followers.remove(this);}
        return true;

    }


    public void addToBlocked(User userToBlock) {
        this.blocked.add(userToBlock);
    }

    public boolean isBlocked(User userToCheck) {
        return (this.blocked.contains(userToCheck));
    }

    public  int getNumOfFollowers(){
        return this.followers.size();
    }

    public  int getNumOfFolloweing(){
        return this.followings.size();
    }

//    public ConcurrentLinkedDeque<NotificationMsg> getUnSeenNotifications() {
//        return unSeenNotifications;
//    }

    public void addUnseenNotification(NotificationMsg msg){
        this.unSeenNotifications.add(msg);
    }

    public NotificationMsg getNextUnseenNotification(){
        if(unSeenNotifications.isEmpty()){
            return null;
        }
        return this.unSeenNotifications.poll();
    }

    public void setNewConnectionId(int newConnectionId) {
        synchronized (this.connectionID){
        this.connectionID=newConnectionId;}
    }
}
