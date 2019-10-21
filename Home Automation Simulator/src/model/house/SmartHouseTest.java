package model.house;

public class SmartHouseTest {
    public static void main(String[] args) {
        SmartHouse house = new SmartHouse();
        assert (house.getName().equals("Smart House"));
        assert (house.getPowerMeter().getCost()==0.25);
        assert (house.getWaterMeter().getCost()==0.002);
        assert (house.getBattery().getCapacity()==13.5);
        assert (house.getWaterTank().getCapacity()==5000);
        assert (house.getEvents().get("fire").getChance()==30);
        assert (house.getEvents().get("intruder").getChance()==20);
        assert (house.getEvents().get("rain").getChance()==90);

        house.simulate();
        System.out.println(house.getStatus());
        house.simulate();
        System.out.println(house.getStatus());

    }
}
