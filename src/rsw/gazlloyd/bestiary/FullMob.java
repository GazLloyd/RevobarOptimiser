package rsw.gazlloyd.bestiary;

import java.util.Map;

/**
 * Created by Gareth Lloyd on 15/06/2016.
 */
public class FullMob extends Mob {
    int level, lifepoints, defence, attack, magic, ranged, slayerlevel, size;
    String name, weakness, xp, slayercat, description;
    boolean members, attackable, aggressive, poisonous;
    String[] area;
    Map<String, Integer> animations;

    /*private class Anims {
        int death, attack, range;
    }*/

    public FullMob() {
        isnull = false;
    }
}
