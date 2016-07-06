package rsw.gazlloyd.gui;

import javax.swing.border.AbstractBorder;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created by Gareth Lloyd on 09/09/2015.
 */
public class HighlightBorder extends BevelBorder {

    public HighlightBorder(Color h, Color s) {
        super(BevelBorder.LOWERED, h, s);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(1,1,1,1);
    }

}
