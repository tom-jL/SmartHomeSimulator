package model.house;

public class Event {
    private int threshold; // threshold for event to occur
    private double chance; // chance it will occur after reaching threshold
    private boolean active;  // if the event is active or not

    public Event() {
        threshold = 100;
        chance = 0.5;
        active = false;
    }

    public Event(int threshold, double chance) {
        this.threshold = threshold;
        this.chance = chance;

    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

