package bgu.spl.net.srv.BGS.msg;

import java.util.LinkedList;
import java.util.List;

public class StatMsg implements Message{
    final short optCode;
    final String ListOfUsernames;
    final List<String> seperatedUsernames;

    public StatMsg(String listOfUsernames) {
        this.optCode = 8;
        ListOfUsernames = listOfUsernames;
        seperatedUsernames = new LinkedList<>();
        String[] parts = listOfUsernames.split("\\|"); //Todo check \\|
        for (String username: parts
             ) {
            seperatedUsernames.add(username);
        }


    }

    @Override
    public void process() {

    }
}
