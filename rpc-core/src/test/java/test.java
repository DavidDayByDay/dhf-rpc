import com.better.constants.MessageConstants;
import com.better.enums.MessageStatus;
import com.better.enums.SerializerType;
import com.better.exceptions.SerializeException;
import com.better.factories.SerializerFactory;
import com.better.protocol.MessageHeader;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        byte[] bytes = "hello world".getBytes(StandardCharsets.UTF_8);
        MessageHeader messageHeader = MessageHeader.buildMessageHeader((byte) 1, (byte) 2, (byte) 1, bytes.length);
        MessageHeader messageHeader2 = MessageHeader.buildMessageHeader((byte) 1, (byte) 2, (byte) 1, bytes.length);
        System.out.println(messageHeader);
        System.out.println(messageHeader2);


        SerializerType json = SerializerType.PROTOSTUFF;
        byte type = json.getType();
        System.out.println(type);

//        try {
//            MessageType.parseByType((byte) 100);
//        } catch (MessageException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        }

        MessageStatus messageStatus = MessageStatus.SUCCESS;
        System.out.println(MessageStatus.isSuccess(messageStatus.getType()));
//        System.out.println(messageStatus.getClass().getModifiers());


        try {
            System.out.println(SerializerFactory.getSerializer((byte) 3));
        } catch (SerializeException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Arrays.toString(MessageConstants.MAGIC_NUMBER));

        int q = 1;
        boolean isDone = false;

        switch (q){
            case 1:
                System.out.println(1);
                isDone = true;

            default:
                if (isDone != true){
                    System.out.println(1000);
                }

        }
    }
}
