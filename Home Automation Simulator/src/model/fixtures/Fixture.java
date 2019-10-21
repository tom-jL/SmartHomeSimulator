package model.fixtures;

import model.house.Event;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Fixture {

    private String name;
    private double powerUsage;
    private double waterUsage;
    private double temperature;
    private boolean on;
    private LocalTime time;

    private HashMap<LocalTime, ArrayList<String>> schedule;
    private ArrayList<String> actions;

    public Fixture(String name, double powerUsage, double waterUsage) {
        schedule = new HashMap<>();
        actions = new ArrayList<>();
        actions.add("Turn on");
        actions.add("Turn off");


        this.name = name;
        this.powerUsage = powerUsage;
        this.waterUsage = waterUsage;
    }

    public String getStatus() {
        return "Name: " + getName() + "\n" +
                "On: " + isOn() + "\n" +
                "Power Usage: " + String.format("%.2f", currentPowerUsage()) + " Killowatt Hours" + "\n" +
                "Water Usage: " + String.format("%.2f", currentWaterUsage()) + " Litres per hour" + "\n";
    }

    public void printStatus() {
        System.out.println(getStatus());
    }

    public void run(String action) {
        if (action.equals("Get status")) this.printStatus();
        if (action.equals("Turn on")) this.turnOn();
        if (action.equals("Turn off")) this.turnOff();
    }

    public String getName() {
        return name;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void turnOn() {
        on = true;
    }

    public void turnOff() {
        on = false;
    }

    public boolean isOn() {
        return on;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPowerUsage() {
        return powerUsage;
    }

    public double getWaterUsage() {
        return waterUsage;
    }

    public double currentPowerUsage() {
        return isOn() ? powerUsage : 0;
    }

    public double currentWaterUsage() {
        return isOn() ? waterUsage : 0;
    }

    public void schedule(LocalTime time, String action) {
        if (getSchedule().containsKey(time)) {
            getSchedule().get(time).add(action);
        } else {
            getSchedule().put(time, new ArrayList<>());
            getSchedule().get(time).add(action);
        }
    }


    public HashMap<LocalTime, ArrayList<String>> getSchedule() {
        return schedule;
    }

    public void setSchedule(HashMap<LocalTime, ArrayList<String>> schedule) {
        this.schedule = schedule;
    }

    public void clearSchedule() {
        getSchedule().clear();
    }

    public void removeSchedule(LocalTime time, String action) {
        if (getSchedule().containsKey(time)) {
            getSchedule().get(time).remove(action);
        }
    }

    public ArrayList<String> getActions() {
        return actions;
    }


    public void simulate(LocalTime time, double temperature, double sunlight, HashMap<String, Event> event) {
        setTime(time);
        if (getSchedule().containsKey(time)) {
            for (String action : getSchedule().get(time)) {
                run(action);
            }
        }

    }

    @Override
    public String toString() {
        return getName();
    }
}


