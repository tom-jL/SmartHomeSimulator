package model.appliances;

import model.fixtures.Fixture;


public class Appliance extends Fixture {


    private boolean moveable = true;

    public Appliance(String name, double powerUsage, double waterUsage) {
        super(name, powerUsage, waterUsage);
    }
}


