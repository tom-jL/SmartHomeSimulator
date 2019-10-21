package model.house;

import java.time.LocalTime;

public class Simulation{

    private double average;
    private double range;

    public Simulation(double average, double range) {
        this.average = average;
        this.range = range;
    }

    public double getUnit(LocalTime time) {
        int hours = 12;
        int x = time.getHour();
        double radians = (Math.PI / hours) * (x + 12);
        return (range / 2) * Math.cos(radians) + average;
    }

    public void setUnit(int unit) {
        range = 0;
        average = unit;
    }
}

