import com.better.test.Fooo;

import java.lang.reflect.Field;

public class test {
    public static void main(String[] args) throws NoSuchMethodException {
//        Class<Foo> fooClass = Foo.class;
//        Method declaredMethod = fooClass.getDeclaredMethod("foo", int.class);
//        Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
//        for (int i = 0; i < parameterTypes.length; i++) {
//            System.out.println(parameterTypes[i]);
//        }

        Class<me> meClass = me.class;
        System.out.println(meClass.isInterface());
        Field[] declaredFields = meClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Class<?> declaringClass = declaredField.getType();
            if (declaringClass.isInterface()){
                System.out.println(declaredField.getName());
            }
        }


    }


    static class me{
        public Fooo fooo;
    }


}
