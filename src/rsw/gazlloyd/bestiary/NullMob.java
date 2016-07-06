package rsw.gazlloyd.bestiary;

/**
 * Created by Gareth Lloyd on 15/06/2016.
 */
public class NullMob extends Mob {
    String contents;
    public NullMob(int i) {
        id = i;
        isnull = true;
        contents = "null";
    }
}
