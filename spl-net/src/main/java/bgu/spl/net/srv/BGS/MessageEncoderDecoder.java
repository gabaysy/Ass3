package bgu.spl.net.srv.BGS;

public class MessageEncoderDecoder implements bgu.spl.net.api.MessageEncoderDecoder<String> {
    @Override
    public String decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(String message) {
        return new byte[0];
    }
}
