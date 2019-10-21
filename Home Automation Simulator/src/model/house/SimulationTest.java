package model.house;

import java.time.LocalTime;

public class SimulationTest {
    public static void main(String[] args) {
        Simulation simulation = new Simulation(10, 10);
        assert (simulation.getUnit(LocalTime.of(12, 0)) == 15);
        assert (simulation.getUnit(LocalTime.of(0, 0)) == 5);
        simulation.setUnit(10);
        assert (simulation.getUnit(LocalTime.of(12, 0)) == 10);
        assert (simulation.getUnit(LocalTime.of(0, 0)) == 10);
    }
}
