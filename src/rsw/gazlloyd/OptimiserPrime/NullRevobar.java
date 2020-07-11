package rsw.gazlloyd.OptimiserPrime;

/**
 * Created by gaz-l on 17/12/2017.
 */
public class NullRevobar extends Revobar {

    public NullRevobar() {
        revoval = 0;
        revocalced = true;
    }

    @Override
    public double calcRevo() {
        return 0;
    }
}
