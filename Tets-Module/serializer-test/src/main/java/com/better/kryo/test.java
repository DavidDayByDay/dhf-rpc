package com.better.kryo;

import com.better.hessian.Car;
import com.better.hessian.Color;
import com.better.hessian.Model;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        Kryo kryo = new Kryo();
        Car car = new Car(Model.EDSEL, Color.BLACK);
        kryo.register(Car.class);
        kryo.register(Color.class);
        kryo.register(Model.class);
        FileOutputStream fos = new FileOutputStream("kryoTest");
        Output out = new Output(fos);
        kryo.writeObject(out,car);

        out.close();
        fos.flush();
        fos.close();


        FileInputStream fis = new FileInputStream("kryoTest");
        Input input = new Input(fis);
        Car car1 = kryo.readObject(input, Car.class);
        System.out.println(car1);
    }


}
