package bgu.spl.net.impl.msg;

public class LogStatInfo {
    private int userAge;
    private int postsPosted;
    private int numOfFollowers;
    private int numOfFollowing;

    public LogStatInfo(int userAge, int postsPosted, int numOfFollowers, int numOfFollowing) {
        this.userAge = userAge;
        this.postsPosted = postsPosted;
        this.numOfFollowers = numOfFollowers;
        this.numOfFollowing = numOfFollowing;
    }

    public int getUserAge() {
        return userAge;
    }

    public int getPostsPosted() {
        return postsPosted;
    }

    public int getNumOfFollowers() {
        return numOfFollowers;
    }

    public int getNumOfFollowing() {
        return numOfFollowing;
    }

    public String StatToString(){
        return " "+this.getUserAge()+" "+this.getPostsPosted()+" "+this.getNumOfFollowers()+" "+this.getNumOfFollowing();
    }
}
