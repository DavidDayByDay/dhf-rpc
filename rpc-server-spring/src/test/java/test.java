import com.better.test.Foo;

import java.lang.reflect.Method;

public class test {
    public static void main(String[] args) throws NoSuchMethodException {
        Class<Foo> fooClass = Foo.class;
        Method declaredMethod = fooClass.getDeclaredMethod("foo", int.class);
        Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            System.out.println(parameterTypes[i]);
        }
    }
}
