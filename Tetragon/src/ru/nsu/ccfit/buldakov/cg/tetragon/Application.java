package ru.nsu.ccfit.buldakov.cg.tetragon;

import ru.nsu.ccfit.buldakov.cg.tetragon.model.Texture;
import ru.nsu.ccfit.buldakov.cg.tetragon.view.MainFrame;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Texture.load();
            MainFrame view = new MainFrame();
            view.setVisible(true);
        });
    }

}
