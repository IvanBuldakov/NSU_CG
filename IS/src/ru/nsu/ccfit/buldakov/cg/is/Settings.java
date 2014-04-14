package ru.nsu.ccfit.buldakov.cg.is;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Settings {

    private static volatile Settings instance;

    private int               a;
    private int               b;
    private int               c;
    private int               d;
    private int               k;
    private int               m;
    private int               n;
    private ArrayList<Double> values;

    private Color   is;
    private Color[] colors;

    private boolean grid;
    private boolean isolines;
    private boolean interpolation;
    private boolean dithering;

    private Settings() {
        try {
            BufferedReader settingsReader = new BufferedReader(new FileReader("res/settings.is"));
            String line;
            String[] tokens;
            line = settingsReader.readLine();
            tokens = line.split(" ");
            a = Integer.parseInt(tokens[0]);
            b = Integer.parseInt(tokens[1]);
            c = Integer.parseInt(tokens[2]);
            d = Integer.parseInt(tokens[3]);
            line = settingsReader.readLine();
            tokens = line.split(" ");
            k = Integer.parseInt(tokens[0]);
            m = Integer.parseInt(tokens[1]);
            line = settingsReader.readLine();
            tokens = line.split(" ");
            n = Integer.parseInt(tokens[0]);
            colors = new Color[n + 2];
            for (int i = 0; i <= n; ++i) {
                line = settingsReader.readLine();
                tokens = line.split(" ");
                colors[i] = new Color(Integer.parseInt(tokens[0]),
                                      Integer.parseInt(tokens[1]),
                                      Integer.parseInt(tokens[2]));
            }
            colors[n + 1] = colors[n];
            line = settingsReader.readLine();
            tokens = line.split(" ");
            is = new Color(Integer.parseInt(tokens[0]),
                           Integer.parseInt(tokens[1]),
                           Integer.parseInt(tokens[2]));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static Settings getInstance() {
        if (null == instance) {
            synchronized (Settings.class) {
                if (null == instance) {
                    instance = new Settings();
                }
            }
        }
        return instance;
    }

    public int getN() {
        return n;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public double getWidth() {
        return b - a;
    }

    public double getHeight() {
        return d - c;
    }

    public Color[] getColors() {
        return colors;
    }

    public Color getISColor() {
        return is;
    }

    public void displayGrid() {
        grid = true;
    }

    public void hideGrid() {
        grid = false;
    }

    public boolean needToDisplayGrid() {
        return grid;
    }

    public void enableInterpolation() {
        interpolation = true;
    }

    public void disableInterpolation() {
        interpolation = false;
    }

    public boolean isInterpolationEnabled() {
        return interpolation;
    }

    public void enableDithering() {
        dithering = true;
    }

    public void disableDithering() {
        dithering = false;
    }

    public boolean isDitheringEnabled() {
        return dithering;
    }

    public void displayIsolines() {
        isolines = true;
    }

    public void hideIsolines() {
        isolines = false;
    }

    public boolean needToDisplayIsolines() {
        return isolines;
    }

    public void setValues(ArrayList<Double> values) {
        this.values = values;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

}
