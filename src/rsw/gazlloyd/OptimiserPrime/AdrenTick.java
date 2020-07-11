package rsw.gazlloyd.OptimiserPrime;

/**
 * Created by gaz-l on 31/12/2017.
 */
public class AdrenTick {
    public double adrenaline;
    public int tick;
    public String ability;
    public AdrenTick(int t, double adren, String abil) {
        tick = t;
        adrenaline = adren;
        ability = abil;
    }

    @Override
    public String toString() {
        return tick+"\t"+adrenaline+"\t"+ability;
    }
}
