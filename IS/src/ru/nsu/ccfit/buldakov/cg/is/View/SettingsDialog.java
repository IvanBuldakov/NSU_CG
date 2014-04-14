package ru.nsu.ccfit.buldakov.cg.is.View;

import ru.nsu.ccfit.buldakov.cg.is.Settings;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {

    private Settings settings = Settings.getInstance();

    private JTextField aValue = new JTextField(10);
    private JTextField bValue = new JTextField(10);
    private JTextField cValue = new JTextField(10);
    private JTextField dValue = new JTextField(10);
    private JTextField kValue = new JTextField(10);
    private JTextField mValue = new JTextField(10);
    private JLabel     aLabel = new JLabel("A", SwingConstants.CENTER);
    private JLabel     bLabel = new JLabel("B", SwingConstants.CENTER);
    private JLabel     cLabel = new JLabel("C", SwingConstants.CENTER);
    private JLabel     dLabel = new JLabel("D", SwingConstants.CENTER);
    private JLabel     kLabel = new JLabel("K", SwingConstants.CENTER);
    private JLabel     mLabel = new JLabel("M", SwingConstants.CENTER);

    private JButton okButton     = new JButton("OK");
    private JButton cancelButton = new JButton("Cancel");

    public SettingsDialog(JFrame mainFrame) {
        super(mainFrame);
        setModal(true);
        setLayout(new GridLayout(7, 2));
        init();
        fill();
        pack();
        setLocationRelativeTo(null);
    }

    private void init() {
        add(aLabel);
        add(aValue);
        add(bLabel);
        add(bValue);
        add(cLabel);
        add(cValue);
        add(dLabel);
        add(dValue);
        add(kLabel);
        add(kValue);
        add(mLabel);
        add(mValue);
        add(okButton);
        add(cancelButton);
        okButton.addActionListener(e -> OK());
        cancelButton.addActionListener(e -> cancel());
    }

    private void fill() {
        aValue.setText(String.valueOf(settings.getA()));
        bValue.setText(String.valueOf(settings.getB()));
        cValue.setText(String.valueOf(settings.getC()));
        dValue.setText(String.valueOf(settings.getD()));
        kValue.setText(String.valueOf(settings.getK()));
        mValue.setText(String.valueOf(settings.getM()));
    }

    private void OK() {
        int a = Integer.parseInt(aValue.getText());
        int b = Integer.parseInt(bValue.getText());
        int c = Integer.parseInt(cValue.getText());
        int d = Integer.parseInt(dValue.getText());
        int k = Integer.parseInt(kValue.getText());
        int m = Integer.parseInt(mValue.getText());

        checkInput(a, b, c, d, k, m);

        settings.setA(a);
        settings.setB(b);
        settings.setC(c);
        settings.setD(d);
        settings.setK(k);
        settings.setM(m);

        getParent().repaint();
        dispose();
    }

    private void checkInput(int a, int b, int c, int d, int k, int m) {
        if (a >= b) {
            JOptionPane.showMessageDialog(this, "B must be greater than A", "", JOptionPane.ERROR_MESSAGE);
        } else if (c >= d) {
            JOptionPane.showMessageDialog(this, "D must be greater than C", "", JOptionPane.ERROR_MESSAGE);
        } else if (k < 2) {
            JOptionPane.showMessageDialog(this, "K must be greater than 1", "", JOptionPane.ERROR_MESSAGE);
        } else if (m < 2) {
            JOptionPane.showMessageDialog(this, "K must be greater than 1", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancel() {
        getParent().repaint();
        dispose();
    }
}
