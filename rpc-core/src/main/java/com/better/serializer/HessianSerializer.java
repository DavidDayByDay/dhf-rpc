package com.better.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class HessianSerializer implements Serializer{

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        try{
            out.startMessage();
            out.writeObject(obj);
            out.completeMessage();
            out.flush();
//            bos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            log.error("something went wrong in hessian serializing for {}", obj);
            throw new RuntimeException(e);
        }finally {
            if(bos!=null) bos.close();
            if(out!=null) out.close();
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Hessian2Input in = new Hessian2Input(bis);
        try {
            in.startMessage();
            T o = (T) in.readObject();
            in.completeMessage();
            return o;
        } catch (IOException e) {
            log.error("something went wrong in hessian deserializing for {}", data);
            throw new RuntimeException(e);
        }
        finally {
            bis.close();
            in.close();
        }
    }
}
