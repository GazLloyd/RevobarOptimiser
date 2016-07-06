package rsw.gazlloyd.gui;

import rsw.gazlloyd.Optimiser.Optimiser2;
import rsw.gazlloyd.Optimiser.util.WrapLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by Gareth Lloyd on 09/09/2015.
 */
public class OptFrame extends JFrame {

    public static OptFrame self;

    public static final int WARNING_VAL = 7;

    public static String[]  meleeabils =        {"Slice", "Havoc", "Backhand", "Smash", "Barge", "Sever", "Kick", "Punish", "Dismember", "Fury", "Cleave", "Decimate"},
                            magicabils =        {"Wrack", "Dragon Breath", "Sonic Wave", "Impact", "Concentrated Blast", "Combust", "Chain", "Corruption Blast"},
                            rangedabils =       {"Piercing Shot", "Snipe", "Dazing Shot", "Binding Shot", "Needle Strike", "Fragmentation Shot", "Ricochet", "Corruption Shot"},
                            defenceabils =      {"Anticipation", "Bash", "Provoke", "Freedom"},
                            constitutionabils = {"Sacrifice", "Tuska's Wrath"};

    public static Border highlight = new HighlightBorder(new Color(0,255,0), new Color(0,171,0)), nolight = BorderFactory.createEmptyBorder(1,1,1,1);

    public HashMap<String,AbilityButton> icons;

    JLabel warning;

    public OptFrame() {
        super();
        self = this;
        icons = new HashMap<>();
        AbilityButton l;


        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridwidth = 6;
        JPanel settingspanel = new JPanel();
        settingspanel.setLayout(new WrapLayout());
        settingspanel.add(new JLabel("Settings go here"));
        JButton optbutton = new JButton("Optimise");
        settingspanel.add(optbutton);
        warning = new JLabel("");
        warning.setFont(new Font(warning.getFont().getName(), Font.BOLD, warning.getFont().getSize()));
        warning.setForeground(Color.RED);
        settingspanel.add(warning);
        content.add(settingspanel, c);

        c.gridwidth = 2;
        c.gridy = 1;
        JPanel meleepanel = new JPanel();
        meleepanel.setLayout(new WrapLayout());
        meleepanel.setPreferredSize(new Dimension(160, 110));
        for (String a : meleeabils) {
            l = new AbilityButton(a);
            icons.put(a,l);
            meleepanel.add(l);
        }
        content.add(meleepanel, c);

        c.gridx = 2;
        JPanel magicpanel = new JPanel();
        magicpanel.setLayout(new WrapLayout());
        magicpanel.setPreferredSize(new Dimension(160, 110));
        for (String a : magicabils) {
            l = new AbilityButton(a);
            icons.put(a, l);
            magicpanel.add(l);
        }
        content.add(magicpanel, c);

        c.gridx = 4;
        JPanel rangedpanel = new JPanel();
        rangedpanel.setLayout(new WrapLayout());
        rangedpanel.setPreferredSize(new Dimension(160, 110));
        for (String a : rangedabils) {
            l = new AbilityButton(a);
            icons.put(a,l);
            rangedpanel.add(l);
        }
        content.add(rangedpanel, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        JPanel defencepanel = new JPanel();
        defencepanel.setLayout(new WrapLayout());
        defencepanel.setPreferredSize(new Dimension(160, 110));
        defencepanel.add(Box.createHorizontalGlue());
        for (String a : defenceabils) {
            l = new AbilityButton(a);
            icons.put(a,l);
            defencepanel.add(l);
        }
        content.add(defencepanel, c);

        c.gridx = 3;
        JPanel constitutionpanel = new JPanel();
        constitutionpanel.setLayout(new WrapLayout());
        constitutionpanel.setPreferredSize(new Dimension(160, 110));
        for (String a : constitutionabils) {
            l = new AbilityButton(a);
            icons.put(a,l);
            constitutionpanel.add(l);
        }
        content.add(constitutionpanel, c);

        c.gridwidth = 6;
        c.gridx = 0;
        c.gridy = 3;
        JPanel resultpanel = new JPanel();
        resultpanel.add(new JLabel("Result goes here"));
        content.add(resultpanel, c);

        checkwarning();
        this.add(content);
        this.setContentPane(content);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void checkwarning() {
        int i = 0;
        for (AbilityButton a : icons.values()) {
            if (a.active)
                i++;
        }
        if (i >= WARNING_VAL)
            warning.setText("Warning! You're optimising for a lot of abilities, this may take awhile!");
        else
            warning.setText("");
    }

    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //log.info("Obtained system look and feel");
        }

        catch(Exception e) {
            //log.severe("Failed to get system look and feel");
            e.printStackTrace();
        }
        Optimiser2.setup();
        OptFrame self = new OptFrame();
    }
}
