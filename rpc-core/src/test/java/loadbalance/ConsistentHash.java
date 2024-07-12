package loadbalance;

import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash {
    public static void main(String[] args) {
        TreeMap<Integer,String> map = new TreeMap<Integer,String>();
        map.put(1,"1");
        map.put(2,"2");
        map.put(3,"3");
        SortedMap<Integer, String> integerStringSortedMap = map.tailMap(4);
        for(Integer key : integerStringSortedMap.keySet()) {
            System.out.println(key+":"+integerStringSortedMap.get(key));
        }
        System.out.println(integerStringSortedMap.firstKey());
    }
}
