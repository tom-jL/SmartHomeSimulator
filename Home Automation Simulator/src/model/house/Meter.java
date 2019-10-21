package model.house;

public class Meter{

    private double usage;
    private double cost;
    private double dailyUsage;

    public Meter() {
        usage = 0;
        cost = 0;
    }

    public Meter(double cost) {
        usage = 0;
        this.cost = cost;
    }

    public double getUsage() {
        return usage;
    }

    public double getCost() {
        return usage * cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getDailyUsage() {
        return dailyUsage;
    }

    public double getDailyCost() {
        return dailyUsage * cost;
    }

    public void addUsage(double amount) {
        usage += amount;
        dailyUsage += amount;
    }

    public void reset() {
        usage = 0;
    }
}

