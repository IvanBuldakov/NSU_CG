package ru.nsu.ccfit.buldakov.cg.is;

import ru.nsu.ccfit.buldakov.cg.is.View.MainFrame;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame view = new MainFrame();
            view.setVisible(true);
        });
    }

}
