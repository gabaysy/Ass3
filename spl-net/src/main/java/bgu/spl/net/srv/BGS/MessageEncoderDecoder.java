package bgu.spl.net.srv.BGS;

import bgu.spl.net.srv.BGS.msg.ACKMsg;
import bgu.spl.net.srv.BGS.msg.ErrorMsg;
import bgu.spl.net.srv.BGS.msg.Message;
import bgu.spl.net.srv.BGS.msg.NotificationMsg;
import com.sun.tools.javac.util.ArrayUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

public class MessageEncoderDecoder implements bgu.spl.net.api.MessageEncoderDecoder<Message> {
    @Override
    public Message decodeNextByte(byte nextByte) {
        return null;
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
                ByteBuffer buff_10 = ByteBuffer.wrap(new byte[4+dataLength]);
                buff_10.put(optCode_10);
                buff_10.put(msgOptCode_10);
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
