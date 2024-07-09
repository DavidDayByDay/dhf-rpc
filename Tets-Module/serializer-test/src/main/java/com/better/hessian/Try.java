package com.better.hessian;

import com.better.serializer.HessianSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Try {
    public static void main(String[] args) throws Exception {
        FileOutputStream fos = new FileOutputStream("C:\\SoftWare\\idear_project\\MyRPC\\Tets-Module\\serializer-test\\src\\main\\java\\com\\better\\hessian\\hessianFile1");
//        Deflation deflation = new Deflation();
//        Hessian2Output out = new Hessian2Output(fos);
        HessianSerializer hessianSerializer = new HessianSerializer();
        Car car1 = new Car(Model.MODEL_T, Color.BLACK);
        Car car2 = new Car(Model.EDSEL, Color.BLACK);
        byte[] data1 = hessianSerializer.serialize(car1);
        byte[] data2 = hessianSerializer.serialize(car2);

        fos.write(data1);
        fos.write(data2);
        fos.flush();
        fos.close();

        FileInputStream fis = new FileInputStream("hessianFile1");
        byte[] bytes = new byte[1024];
        int len;
        while ((len = fis.read(bytes)) != -1) {
            fis.read(bytes);
        }
        Car deserialize = hessianSerializer.deserialize(bytes, Car.class);
        System.out.println(deserialize);


//        FileInputStream fis = new FileInputStream("hessianFile1");
//        Hessian2Input in = new Hessian2Input(fis);
//        in.startMessage();
//        int i = in.readInt();
//        System.out.println(i);
//        Car o = (Car) in.readObject();
//        System.out.println(o.getClass());
//        System.out.println(o);

    }

}
