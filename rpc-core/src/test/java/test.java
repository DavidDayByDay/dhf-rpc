import com.better.protocol.MessageHeader;

import java.nio.charset.StandardCharsets;

public class test {
    public static void main(String[] args) {
        byte[] bytes = "hello world".getBytes(StandardCharsets.UTF_8);
        MessageHeader messageHeader = MessageHeader.buildMessageHeader((byte) 1, (byte) 2, (byte) 1, bytes.length);
        MessageHeader messageHeader2 = MessageHeader.buildMessageHeader((byte) 1, (byte) 2, (byte) 1, bytes.length);
        System.out.println(messageHeader);
        System.out.println(messageHeader2);
    }
}
