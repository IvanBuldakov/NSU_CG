package ru.nsu.ccfit.buldakov.cg.is.Functions;

public class MainFunction implements Function {

    @Override
    public double valueAt(double x, double y) {
        return Math.cos( x * x - y * y ) + 3 * Math.sin( Math.sqrt( x * x + y * y ) ) + 5;
    }

}
