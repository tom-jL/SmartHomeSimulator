package model.house;

public class BatteryTest {
    public static void main(String[] args) {
        Battery battery = new Battery(10);
        assert (battery.getCharge() == 0);
        assert (battery.getCapacity() == 10);
        battery.simulate(11, 0);
        assert (battery.getCharge() == 10);
        battery.simulate(0, 12);
        assert (battery.getCharge() == 0);
        System.out.println("Pass");
    }
}
