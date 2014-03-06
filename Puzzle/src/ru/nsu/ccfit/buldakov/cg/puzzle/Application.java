package ru.nsu.ccfit.buldakov.cg.puzzle;

import ru.nsu.ccfit.buldakov.cg.puzzle.view.MainFrame;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame view = new MainFrame();
                view.setVisible(true);
            }
        });
    }

}
