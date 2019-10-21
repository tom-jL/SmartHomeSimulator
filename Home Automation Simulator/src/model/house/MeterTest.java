package model.house;

public class MeterTest {
    public static void main(String[] args) {
        Meter meter = new Meter(10);
        meter.addUsage(10);
        assert (meter.getUsage() == 10);
        assert (meter.getCost() == 100);
        meter.reset();
        assert (meter.getUsage() == 0);
        assert (meter.getCost() == 0);
    }
}
