package model.fixtures;

import model.appliances.Appliance;

import java.time.LocalTime;
import java.util.HashMap;

public class FixtureTest {

    public static void main(String[] args) {
        Fixture fixture = new Appliance("test", 0.5, 1);
        assert (fixture.getName().equals("test"));


        fixture.run("Turn on");
        assert (fixture.isOn());
        assert (fixture.currentPowerUsage() == 0.5);
        assert (fixture.currentWaterUsage() == 1);

        fixture.run("Turn off");
        assert (!fixture.isOn());
        assert (fixture.currentPowerUsage() == 0);
        assert (fixture.currentWaterUsage() == 0);

        fixture.setTime(LocalTime.of(5, 0));
        assert (fixture.getTime().equals(LocalTime.of(5, 0)));

        fixture.schedule(LocalTime.of(5, 0), "Turn on");
        assert (fixture.getSchedule().containsKey(LocalTime.of(5, 0)));
        try {
            assert (fixture.getSchedule().get(LocalTime.of(5, 0)).contains("Turn on"));
        } catch (Exception e) {
            System.out.println(false);
        }
        fixture.simulate(LocalTime.of(5, 0), 25, 50, new HashMap<>());
        assert (fixture.isOn());

        fixture.removeSchedule(LocalTime.of(5, 0), "Turn on");
        assert (!fixture.getSchedule().get(LocalTime.of(5, 0)).contains("Turn on"));

    }
}
