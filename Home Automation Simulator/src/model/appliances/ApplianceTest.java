package model.appliances;

import java.time.LocalTime;
import java.util.HashMap;

public class ApplianceTest {

    public static void main(String[] args) {
        Appliance appliance = new Appliance("test", 2400, 1);
        assert (appliance.getName().equals("test"));


        appliance.run("Turn on");
        assert (appliance.isOn());
        assert (appliance.currentPowerUsage() == 0.1);
        assert (appliance.currentWaterUsage() == 1);

        appliance.run("Turn off");
        assert (!appliance.isOn());
        assert (appliance.currentPowerUsage() == 0);
        assert (appliance.currentWaterUsage() == 0);

        appliance.setTime(LocalTime.of(5, 0));
        assert (appliance.getTime().equals(LocalTime.of(5, 0)));

        appliance.schedule(LocalTime.of(5, 0), "Turn on");
        assert (appliance.getSchedule().containsKey(LocalTime.of(5, 0)));
        try {
            assert (appliance.getSchedule().get(LocalTime.of(5, 0)).contains("Turn on"));
        } catch (Exception e) {
            System.out.println(false);
        }
        appliance.simulate(LocalTime.of(5, 0), 25, 50, new HashMap<>());
        assert (appliance.isOn());

        appliance.removeSchedule(LocalTime.of(5, 0), "Turn on");
        assert (!appliance.getSchedule().get(LocalTime.of(5, 0)).contains("Turn on"));

    }
}
