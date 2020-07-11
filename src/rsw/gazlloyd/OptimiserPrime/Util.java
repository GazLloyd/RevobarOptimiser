package rsw.gazlloyd.OptimiserPrime;

import java.util.ArrayList;

/**
 * Created by gaz-l on 21/12/2017.
 */
public class Util {

    // convert an int[] to an ArrayList<Integer>, since Arrays.asList doesn't handle boxing
    public static ArrayList<Integer> intArrToList(int[] arr) {
        ArrayList<Integer> ret = new ArrayList<>(arr.length);
        for (int a : arr) {
            ret.add(a);
        }
        return ret;
    }
}
