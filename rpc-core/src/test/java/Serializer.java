import java.io.*;

public class Serializer {
//    @Test
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Constructing objects");
        Blip b = new Blip("A Stirng", 47);
        System.out.println(b);
        ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("file1.txt"));
        System.out.println("保存对象");
        o.writeObject(b);
        o.close();
        //获得对象
        System.out.println("获取对象");
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("file1.txt"));
        System.out.println("Recovering b");
        b = (Blip) in.readObject();
        System.out.println(b);
    }


     static class Blip implements Externalizable{
        private int i ;
        private String s;
        public Blip() {}
        public Blip(String x, int a) {
            System.out.println("Blip (String x, int a)");
            s = x;
            i = a;
        }
        public String toString() {
            return s+i;
        }
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            // TODO Auto-generated method stub
            System.out.println("Blip.writeExternal");
            out.writeObject(s);
            out.writeInt(i);
//
        }
        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            // TODO Auto-generated method stub
            System.out.println("Blip.readExternal");
            s = (String)in.readObject();
            i = in.readInt();
        }


    }


}
