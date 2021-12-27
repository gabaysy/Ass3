package bgu.spl.net.srv;

import java.util.LinkedList;
import java.util.List;

public class User {


    private String username;
    private String password;
    private String birthday;
    private List<User> followers;
    private List<User> followings;
    private int age;
    private int NumPost;
    private boolean loggedin;



    public User(String name, String code, String date ){
        this.username=name;
        this.password=code;
        this.birthday=date;
        this.followers=new LinkedList<>() ;
        this.followings=new LinkedList<>() ;
        this.loggedin=false;
    }

    public boolean login(){
        if (loggedin)
            return false;
        loggedin=true;
        return true;
    }

    public boolean logout(){
        if (!loggedin)
            return false;
        loggedin=false;
        return false;
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
        return age;
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


}
