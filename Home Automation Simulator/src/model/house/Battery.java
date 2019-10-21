package model.house;



public class Battery{

    private double charge;
    private double capacity;

    public Battery() {
        charge = 0;
        capacity = 0;
    }

    public Battery(double capacity) {
        charge = 0;
        this.capacity = capacity;
    }

    public double getCharge() {
        return charge;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return String.format("%.2f", getCharge());
    }

    public void simulate(double charge, double drain) {
        this.charge += charge - drain;
        if (this.charge > capacity) {
            this.charge = capacity;
        }
        if (this.charge < 0) {
            this.charge = 0;
        }


        //this.charge = this.charge > capacity ? capacity : this.charge < 0 ? 0 : this.charge;
    }
}

