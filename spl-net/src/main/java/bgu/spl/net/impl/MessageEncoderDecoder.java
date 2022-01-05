package bgu.spl.net.impl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.impl.msg.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class MessageEncoderDecoder implements bgu.spl.net.api.bidi.MessageEncoderDecoder<Message> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (nextByte == ';') { //Todo check comparison
            return decodeEntireString(bytes);
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    public Message decodeEntireString(byte[] bytes) {
        int optCode = bytesToShort(new byte[]{bytes[0], bytes[1]});
        switch (optCode) {
            case 1:
                return decReg(bytes);
            case 2:
                return decLogin(bytes);
            case 3:
                return decLogout(bytes);
            case 4:
                return decFollow(bytes);
            case 5:
                return decPost(bytes);
            case 6:
                return decPM(bytes);
            case 7:
                return decLogstat(bytes);
            case 8:
                return decStat(bytes);
            case 12:
                return decBlock(bytes);
        }
        return null;
    }

    private Message decReg(byte[] bytes) {
        int currIndex = 2;
        int usernameLen = 0;
        int userIndex=0;
        byte [] username = new byte[bytes.length];

        //username
        while(bytes[currIndex]!='\0') {
            username[userIndex]=bytes[currIndex];
            usernameLen++;
            userIndex++;
            currIndex++;
        }
        String _username= new String(username, 0, usernameLen, StandardCharsets.UTF_8);

        //password
        currIndex++;
        int passIndex=0;
        int passLen = 0;
        byte [] password = new byte[bytes.length-usernameLen];
        while(bytes[currIndex]!='\0') {
            password[passIndex]=bytes[currIndex];
            passIndex++;
            passLen++;
            currIndex++;
        }
        String _password= new String(username, 0, passLen, StandardCharsets.UTF_8);

        //birthday
        currIndex++;
        int birthIndex=0;
        int birthLen = 0;
        byte [] birthday = new byte[bytes.length-usernameLen-passLen];
        while(bytes[currIndex]!='\0') {
            birthday[birthIndex]=bytes[currIndex];
            birthIndex++;
            birthLen++;
            currIndex++;
        }
        String _birthday= new String(username, 0, birthLen, StandardCharsets.UTF_8);

        return new RegisterMsg(_username,_password,_birthday);
    }


    private Message decLogin(byte[] bytes) {
        int currIndex = 2;
        int usernameLen = 0;
        int userIndex=0;
        byte [] username = new byte[bytes.length];

        //username
        while(bytes[currIndex]!='\0') {
            username[userIndex]=bytes[currIndex];
            usernameLen++;
            userIndex++;
            currIndex++;
        }
        String _username= new String(username, 0, usernameLen, StandardCharsets.UTF_8);

        //password
        currIndex++;
        int passIndex=0;
        int passLen = 0;
        byte [] password = new byte[bytes.length-usernameLen];
        while(bytes[currIndex]!='\0') {
            password[passIndex]=bytes[currIndex];
            passIndex++;
            passLen++;
            currIndex++;
        }
        String _password= new String(username, 0, passLen, StandardCharsets.UTF_8);

        //captcha
        byte captcha=bytes[bytes.length-1];

        if(captcha!=bytes[currIndex++]) { //debug
            System.out.println("Problem");
        }

        return new LoginMsg(_username,_password,captcha);
    }

    private Message decLogout(byte[] bytes) {
        return new LogoutMsg();
    }

    private Message decFollow(byte[] bytes) {
        //follow or unfollow
        byte foll_unFoll=bytes[2];
        //username
        int currIndex=3;
        int userIndex=0;
        int userLen=0;
        byte [] username = new byte[bytes.length-3];
        while(currIndex<bytes.length) {
            username[userIndex]=bytes[currIndex];
            userIndex++;
            currIndex++;
            userLen++;
        }
        String _username= new String(username, 0, userLen, StandardCharsets.UTF_8);

        return new FollowMsg(foll_unFoll, _username);
    }

    private Message decPost(byte[] bytes) {
        //content
        int currIndex=2;
        int contentIndex=0;
        int contentLen=0;
        byte [] content = new byte[bytes.length-2];
        while(bytes[currIndex]!='\0') {
            content[contentIndex]=bytes[currIndex];
            contentIndex++;
            currIndex++;
            contentLen++;
        }

        String _content= new String(content, 0, contentLen, StandardCharsets.UTF_8);

        return new PostMsg(_content);
    }

    private Message decPM(byte[] bytes) {
        int currIndex = 2;
        int usernameLen = 0;
        int userIndex=0;
        byte [] username = new byte[bytes.length];

        //username
        while(bytes[currIndex]!='\0') {
            username[userIndex]=bytes[currIndex];
            usernameLen++;
            userIndex++;
            currIndex++;
        }
        String _username= new String(username, 0, usernameLen, StandardCharsets.UTF_8);

        //content
        currIndex++;
        int contentIndex=0;
        int contentLen = 0;
        byte [] content = new byte[bytes.length-usernameLen];
        while(bytes[currIndex]!='\0') {
            content[contentIndex]=bytes[currIndex];
            contentIndex++;
            contentLen++;
            currIndex++;
        }
        String _content= new String(username, 0, contentLen, StandardCharsets.UTF_8);

        //sending time
        currIndex++;
        int sendingTimeIndex=0;
        int sendingTimeLen = 0;
        byte [] sendingTime = new byte[bytes.length-usernameLen-contentLen];
        while(bytes[currIndex]!='\0') {
            sendingTime[sendingTimeIndex]=bytes[currIndex];
            sendingTimeIndex++;
            sendingTimeLen++;
            currIndex++;
        }
        String _sendingTime= new String(username, 0, sendingTimeLen, StandardCharsets.UTF_8);

        return new PMMsg(_username,_content,_sendingTime);
    }


    private Message decLogstat(byte[] bytes) {
        return new LogstatMsg();
    }


    private Message decStat(byte[] bytes) {
        //usernames list
        int currIndex=2;
        int usersIndex=0;
        int usersLen=0;
        byte [] usernames = new byte[bytes.length-2];
        while(bytes[currIndex]!='\0') {
            usernames[usersIndex]=bytes[currIndex];
            usersIndex++;
            currIndex++;
            usersLen++;
        }

        String _usernames= new String(usernames, 0, usersLen, StandardCharsets.UTF_8);

        return new StatMsg(_usernames);
    }

    private Message decBlock(byte[] bytes) {
        //username
        int currIndex=2;
        int userIndex=0;
        int userLen=0;
        byte [] username = new byte[bytes.length-2];
        while(bytes[currIndex]!='\0') {
            username[userIndex]=bytes[currIndex];
            userIndex++;
            currIndex++;
            userLen++;
        }

        String _username= new String(username, 0, userLen, StandardCharsets.UTF_8);
        return new BlockMsg(_username);
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }


    @Override
    public byte[] encode(Message message) {
        if(message==null)
            return null;

        switch (message.getOptCode()){
            case (9):
                // Notification
                NotificationMsg notif = (NotificationMsg) message;

                byte[] optCode_9 = shortToBytes(notif.getOptCode());
                byte[] pm_or_post_9 = shortToBytes(notif.getPM_PublicInShort());
                byte[] postingUser_9 = notif.getPostingUser().getBytes(StandardCharsets.UTF_8);
                byte[] zero_9 = shortToBytes((byte) 0);
                byte[] content_9 = notif.getContent().getBytes(StandardCharsets.UTF_8);
                byte[] second_zero_9 = shortToBytes((byte) 0);

                int len = optCode_9.length+
                        pm_or_post_9.length+
                        postingUser_9.length+
                        zero_9.length+
                        content_9.length+
                        second_zero_9.length;
                ByteBuffer buff_9 = ByteBuffer.wrap(new byte[len]);

                buff_9.put(optCode_9);
                buff_9.put(pm_or_post_9);
                buff_9.put(postingUser_9);
                buff_9.put(zero_9);
                buff_9.put(content_9);
                buff_9.put(second_zero_9);

                byte[] notifEncoded = buff_9.array();
                return notifEncoded;

            //ACK
            case (10):
                ACKMsg ack=(ACKMsg) message;
                byte[] optCode_10 = shortToBytes(ack.getOptCode());
                byte[] msgOptCode_10 = shortToBytes(ack.getMessageOptcode());

                byte[] data_10 = null;
                int dataLength = 0;
                if(ack.hasData()){
                    data_10= ack.getAdditionalData().getBytes(StandardCharsets.UTF_8);
                    dataLength=data_10.length;
                }
                ByteBuffer buff_10 = ByteBuffer.wrap(new byte[5+dataLength]);
                buff_10.put(optCode_10);
                buff_10.put(msgOptCode_10);
                buff_10.put(";".getBytes(StandardCharsets.UTF_8));
                if(ack.hasData()){
                    buff_10.put(data_10);
                }
                byte[] ackEncoded = buff_10.array();
                return ackEncoded;

            // Error
            case (11):
                ErrorMsg err=(ErrorMsg) message;
                byte[] optCode_11=shortToBytes(err.getOptCode());
                byte[] msgOptCode_11=shortToBytes(err.getMessageOptcode());
                ByteBuffer buff_11 = ByteBuffer.wrap(new byte[4]);
                buff_11.put(optCode_11);
                buff_11.put(msgOptCode_11);
                byte[] errEncoded = buff_11.array();
                return errEncoded;

        }
        return new byte[0];
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
}
