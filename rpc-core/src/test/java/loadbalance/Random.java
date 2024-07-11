package loadbalance;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Random {
    @Test
    public void testRand(){
        Map<Integer,Integer> map = new HashMap<>();
        java.util.Random random = new java.util.Random();

        for (int i = 0;  i < 1000000; i++){
            int k = random.nextInt(10);
            map.compute(k,(v,u) -> u == null ? 1 : u+1);
        }

        map.forEach((k,v)->{
            System.out.println(k + ": " + v +"\n");
        });
    }
}
