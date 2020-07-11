package rsw.gazlloyd.OptimiserPrime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by gaz-l on 21/12/2017.
 */
public class TestStuff {
    public static void main2(String... args) {
        HashMap<ArrayList<Integer>, String> a = new HashMap<>();
        ArrayList<Integer> b=new ArrayList<>(),c=new ArrayList<>();

        b.add(1);
        b.add(2);
        b.trimToSize();
        a.put(b, "hello");

        c.add(1);
        c.add(2);

        System.out.println(a.get(c));
        c.trimToSize();
        System.out.println(a.get(c));
    }
    public static void main(String... args){
        Stack<Integer> a = new Stack<>(), b  = new Stack<>();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        a.add(5);
        b.push(1);
        b.push(2);
        b.push(3);
        b.push(4);
        b.push(5);
        System.out.println(a);
        System.out.println(b);
        a.pop();
        System.out.println(a);
        b.pop();
        System.out.println(b);

    }
}
