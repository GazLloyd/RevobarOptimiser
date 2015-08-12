package Optimiser;

import java.util.ArrayList;

/**
 * Created by Gareth Lloyd on 12/08/2015.
 */ //
class Bar {
    public double val;
    public ArrayList<Integer> used;
    public boolean err;
    public Bar() {
        err = false;
        used = new ArrayList<Integer>();
    }
}
