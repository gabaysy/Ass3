package bgu.spl.net.srv.BGS.msg;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.BgsDB;

public class RegisterMsg implements Message{
    final short optCode;
    final String username;
    final String password;
    final String birthday;

    public RegisterMsg(String username, String password, String birthday) {
        this.optCode = 1;
        this.username = username;
        this.password = password;
        if(isBirthdayValid(birthday))
            this.birthday = birthday;
        else this.birthday="00/00/0000";
    }

    public short getOptCode() {
        return optCode;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    private boolean isBirthdayValid(String birthday){
        if (birthday.isEmpty())
            return false;

        String[] parts = birthday.split("-");
        if (parts==null||parts.length!=3)
            return false;
        for(int i=0;i<2;i++){
            if (i==0||i==1){
                if (parts[i].length()!=2)
                    return false;
            }
            if (i==2){
                if(parts[i].length()!=4)
                    return false;
            }
            if(!parts[i].matches("[0-9]+"))
                return false;
        }
        return true;
    }

    @Override
    public void process(BgsDB db, Connections connections, int connectionId) {
        boolean success= db.register(this.getUsername(),this.getPassword(), this.getBirthday(),connectionId );
        if(success){
            connections.send(connectionId,new ACKMsg(this.getOptCode()));
        }
        else{
            connections.send(connectionId,new ErrorMsg(this.getOptCode()));
        }

    }
}
