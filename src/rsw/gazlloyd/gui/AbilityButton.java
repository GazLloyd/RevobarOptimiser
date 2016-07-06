package rsw.gazlloyd.gui;

import rsw.gazlloyd.Optimiser.Optimiser2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Gareth Lloyd on 13/09/2015.
 */

public class AbilityButton extends JButton {
    ImageIcon img;
    String abil;
    boolean active;

    public AbilityButton(String a) {
        abil = a;
        img = Optimiser2.abilities.get(a).img;
        active = false;
        this.setIcon(img);
        this.addActionListener(new AbilityClick(this));
    }


    class AbilityClick implements ActionListener {
        JButton button;

        public AbilityClick(JButton b) {
            button = b;
            button.setBorder((active ? OptFrame.highlight : OptFrame.nolight));

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            active = !active;
            button.setBorder((active ? OptFrame.highlight : OptFrame.nolight));
            OptFrame.self.checkwarning();
        }
    }
}
