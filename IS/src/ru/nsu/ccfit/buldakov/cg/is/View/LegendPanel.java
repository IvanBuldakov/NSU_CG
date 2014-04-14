package ru.nsu.ccfit.buldakov.cg.is.View;

import ru.nsu.ccfit.buldakov.cg.is.Canvas;
import ru.nsu.ccfit.buldakov.cg.is.Settings;

import javax.swing.*;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class LegendPanel extends JPanel {

    private final Canvas legend;
    private Settings          settings = Settings.getInstance();
    private JLabel            title    = new JLabel("Legend", SwingConstants.RIGHT);
    private ArrayList<JLabel> labels   = new ArrayList<>();

    public LegendPanel(Canvas legend) {
        this.legend = legend;
        GroupLayout LegendLayout = new GroupLayout(this);
        LegendLayout.setVerticalGroup(
                LegendLayout.createSequentialGroup()
                        .addComponent(title)
        );

        LegendLayout.setHorizontalGroup(
                LegendLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(title, 80, 80, 80)
        );

        setLayout(LegendLayout);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        labels.forEach(this::remove);
        if (legend.getHeight() != getHeight()) {
            legend.resize(30, 4 * getHeight() / 5);
        }
        legend.paint(g, getWidth() - 40, getHeight() / 10);
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_UP);
        ArrayList<Double> values = settings.getValues();
        if (values.size() == 0) {
            return;
        }
        for (int i = 1; i <= settings.getN(); ++i) {
            JLabel label = new JLabel(String.valueOf(df.format(values.get(i))), SwingConstants.RIGHT);
            label.setBounds(getWidth() - 80, 9 * getHeight() / 10 - i * (4 * getHeight() / 5 / (settings.getN() + 1)) - 10, 30, 20);
            labels.add(label);
            add(label);
        }
    }

}
