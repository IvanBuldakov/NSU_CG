package ru.nsu.ccfit.buldakov.cg.knot;

import ru.nsu.ccfit.buldakov.cg.knot.view.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame view = new MainFrame();
            view.setVisible(true);
        });
    }
}
