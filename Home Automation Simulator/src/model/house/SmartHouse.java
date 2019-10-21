package model.house;

import control.XMLParser;
import model.sprites.Sprite;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class SmartHouse {

    private String name;

    private ArrayList<Sprite> sprites;

    private Meter powerMeter;
    private Meter waterMeter;
    private Battery battery;
    private Battery waterTank;

    private Simulation temperature;
    private Simulation light;
    private Simulation humidity;

    private HashMap<String, Event> events;

    private LocalTime time;
    XMLParser xmlParser;

    public SmartHouse() {
        this("Smart House", 0.25, 0.002, 13.5, 5000);

    }

    public SmartHouse(String name, double powerCost, double waterCost, double batteryCapacity, double waterCapacity) {
        this.name = name;
        powerMeter = new Meter(powerCost);
        waterMeter = new Meter(waterCost);
        battery = new Battery(batteryCapacity);
        waterTank = new Battery(waterCapacity);

        events = new HashMap<>();
        events.put("fire", new Event(35, 30));
        events.put("intruder", new Event(0, 20));
        events.put("rain", new Event(100, 90));

        humidity = new Simulation(80, 20);
        light = new Simulation(50, 100);
        temperature = new Simulation(20, 10);


        sprites = new ArrayList<>();
        xmlParser = new XMLParser();


    }

    public XMLParser getXmlParser() {
        return xmlParser;
    }

    public void loadHouse(String file) {
        sprites = xmlParser.loadXML(file);
    }

    public void saveHouse(String file) {
        xmlParser.saveXML(sprites, file);
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Battery getBattery() {
        return battery;
    }

    public Battery getWaterTank() {
        return waterTank;
    }

    public Meter getPowerMeter() {
        return powerMeter;
    }

    public Meter getWaterMeter() {
        return waterMeter;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Event> getEvents() {
        return events;
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public String getStatus() {


        return "Time: " + DateTimeFormatter.ofPattern("hh:mm a").format(time) + "\n" +
                String.format("Temperature: %.2f C", getTemperature()) + "\n" +
                String.format("Humidity: %.2f", getHumidity()) + "%" + "\n" +
                String.format("Sunlight: %.2f", getLight()) + "%" + "\n" +
                String.format("Battery Charge: %.3f KWH", getBattery().getCharge()) + "\n" +
                String.format("Water Level: %.2f Litres", getWaterTank().getCharge()) + "\n" +
                String.format("Current Power Usage: %.3f KWH $%.2f", getPowerMeter().getUsage(), getPowerMeter().getCost()) + "\n" +
                String.format("Current Water Usage: %.2f Litres $%.2f", getWaterMeter().getUsage(), getWaterMeter().getCost()) + "\n" +
                String.format("Daily Power Usage: %.3f KWH $%.2f", getPowerMeter().getDailyUsage(), getPowerMeter().getDailyCost()) + "\n" +
                String.format("Daily Water Usage: %.2f Litres $%.2f", getWaterMeter().getDailyUsage(), getWaterMeter().getDailyCost()) + "\n";
    }

    public void printStatus() {
        System.out.println(getStatus());
    }

    public double getTemperature() {
        return temperature.getUnit(time);
    }

    public void setTemperature(int temperature) {
        this.temperature.setUnit(temperature);
    }

    public double getLight() {
        return light.getUnit(time);
    }

    public void setLight(int light) {
        this.light.setUnit(light);
    }

    public double getHumidity() {
        return humidity.getUnit(time);
    }

    public void setHumidity(int humidity) {
        this.humidity.setUnit(humidity);
    }

    public void sort() {
        getSprites().sort(new Comparator<Sprite>() {
            @Override
            public int compare(Sprite o1, Sprite o2) {
                if (o1.getZ() > o2.getZ()) {
                    return 1;
                }
                if (o1.getZ() < o2.getZ()) {
                    return -1;
                }
                if (o1.getZ() == o2.getZ()) {
                    return 0;
                } else {
                    return 0;
                }
            }
        });
    }

    public void simulate() {

        getPowerMeter().reset();
        getWaterMeter().reset();

        if (time.equals(LocalTime.of(4, 59))) {

            System.out.println("NEW DAY!");
            double average_temp = 20 + (int) (Math.random() * ((28 - 20) + 1)); // random temperature simulation, average temp of 20 to 28 degrees
            double temp_range = 10 + (int) (Math.random() * ((15 - 10) + 1)); // random temperature swing variance of 10 to 15 degrees
            double average_humidity = 60 + (int) (Math.random() * ((80 - 60)));
            double humidity_range = 30;

            humidity = new Simulation(average_humidity, humidity_range);
            light = new Simulation(50, 100);
            temperature = new Simulation(average_temp, temp_range);

        }


        for (Sprite sprite : getSprites()) {
            if (sprite.getSmartObj() != null) {
                sprite.getSmartObj().simulate(time, getTemperature(), getLight(), events);
                getBattery().simulate(0, sprite.getSmartObj().currentPowerUsage() / 60);
                if (getBattery().getCharge() == 0) {
                    getPowerMeter().addUsage(sprite.getSmartObj().currentPowerUsage() / 60);
                }
                getWaterTank().simulate(0, sprite.getSmartObj().currentWaterUsage() / 60);
                if (getWaterTank().getCharge() == 0) {
                    getWaterMeter().addUsage(sprite.getSmartObj().currentWaterUsage() / 60);
                }

                if (sprite.getSmartObj().getName().equals("solar panel")) {
                    getBattery().simulate(sprite.getSmartObj().getPowerUsage() / 60, 0);
                }


            }
        }

        if (time.getMinute() == 0) {
            // Every hour
            // chance to rain on hour (if humidity is above 100%)
            if (getHumidity() > events.get("rain").getThreshold()) {
                if ((int) (Math.random() * (101)) < events.get("rain").getChance()) {
                    events.get("rain").setActive(true);

                } else {
                    events.get("rain").setActive(false);
                }
            }
            // chance for intruder on hour (if after hours)
            if (time.isAfter(LocalTime.of(21, 00)) && time.isBefore(LocalTime.of(5, 00))) {
                if ((int) (Math.random() * (101)) < events.get("intruder").getChance()) {
                    events.get("intruder").setActive(true);
                } else {
                    events.get("intruder").setActive(false);
                }
            }
            // chance for house fire
            if (getTemperature() > events.get("fire").getThreshold()) {
                if ((int) (Math.random() * (101)) < events.get("fire").getChance()) {
                    events.get("fire").setActive(true);
                } else {
                    events.get("fire").setActive(false);
                }
            }

        }
    }

    public void add(Sprite newSprite) {
        sprites.add(newSprite);
    }

    public void remove(Sprite sprite) {
        sprites.remove(sprite);
    }


}

