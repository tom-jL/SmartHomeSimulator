package view;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.house.SmartHouse;
import model.sprites.Player;
import model.sprites.Sprite;

import java.time.LocalTime;

public class View {

    private int fps = 10;
    private int frame;

    private Canvas canvas;
    private Canvas lighting;
    private TextArea console;

    private double width;
    private double height;

    private AreaChart<Number, Number> weatherChart;
    private AreaChart<Number, Number> resourceChart;

    XYChart.Series seriesTemp;
    XYChart.Series seriesHumidity;
    XYChart.Series seriesDaylight;
    XYChart.Series seriesPower;
    XYChart.Series seriesWater;


    private GraphicsContext canvasGC;
    private GraphicsContext lightGC;

    private Player player;
    private Sprite tempSprite;
    private Sprite selectedSprite;
    private Sprite interSprite;

    private SmartHouse model;
    private AnimationTimer timer;
    private Long lastNanoTime = System.nanoTime();
    private double delay;


    public View(double width, double height, int fps) {
        //timer = new AnimationTimer()(1000/fps,this);
        this.width = width;
        this.height = height;
        canvas = new Canvas(width, height * 0.8);
        lighting = new Canvas(canvas.getWidth(), canvas.getHeight());
        canvasGC = canvas.getGraphicsContext2D();
        console = new TextArea();
        console.setEditable(false);
        lightGC = lighting.getGraphicsContext2D();
        lighting.setMouseTransparent(true);
        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMouseMovement(event);
            }
        });
        frame = 0;
        this.fps = fps;
        newModel();

        weatherChart = new AreaChart<Number, Number>(new NumberAxis(), new NumberAxis());
        resourceChart = new AreaChart<Number, Number>(new NumberAxis(), new NumberAxis());
        seriesTemp = new XYChart.Series();
        seriesTemp.setName("Temperature");
        seriesHumidity = new XYChart.Series();
        seriesHumidity.setName("Humidity");
        seriesDaylight = new XYChart.Series();
        seriesDaylight.setName("Daylight");
        seriesPower = new XYChart.Series();
        seriesPower.setName("Power Usage");
        seriesWater = new XYChart.Series();
        seriesWater.setName("Water Usage");
        weatherChart.getData().addAll(seriesTemp, seriesDaylight, seriesHumidity);
        resourceChart.getData().addAll(seriesPower, seriesWater);
        weatherChart.setCreateSymbols(false);
        resourceChart.setCreateSymbols(false);
        weatherChart.setLegendSide(Side.LEFT);
        resourceChart.setLegendSide(Side.LEFT);
        weatherChart.setLegendVisible(true);
        resourceChart.setLegendVisible(true);

        simulate();
    }

    public void refreshCharts() {
        seriesTemp.getData().add(new XYChart.Data((double) getTime().getHour() + ((double) getTime().getMinute() / 60), model.getTemperature()));
        seriesDaylight.getData().add(new XYChart.Data((double) getTime().getHour() + ((double) getTime().getMinute() / 60), model.getLight()));
        seriesHumidity.getData().add(new XYChart.Data((double) getTime().getHour() + ((double) getTime().getMinute() / 60), model.getHumidity()));
        seriesPower.getData().add(new XYChart.Data((double) getTime().getHour() + ((double) getTime().getMinute() / 60), model.getPowerMeter().getDailyUsage()));
        seriesWater.getData().add(new XYChart.Data((double) getTime().getHour() + ((double) getTime().getMinute() / 60), model.getWaterMeter().getDailyUsage()));
    }

    public void setModel(SmartHouse model) {
        this.model = model;
    }

    public void newModel() {
        model = new SmartHouse();
        Sprite door = new Sprite("Door", canvas.getWidth() / 2, 0, -1, "img/closed_door.png",
                1, 1, 1, false, false, Sprite.rooms.Livingroom, null);
        Sprite floor = new Sprite("Floor", door.getX(), door.getY() + door.getHeight(), -1, "img/floor.png",
                1, 1, 1, true, false, Sprite.rooms.Livingroom, null);
        player = new Player(floor.getX(), floor.getY() - floor.getHeight());
        model.getSprites().add(door);
        model.getSprites().add(floor);
        model.setTime(LocalTime.of(4, 59));
    }

    public void setLayoutY(double posy) {
        canvas.setLayoutY(posy);
        lighting.setLayoutY(posy);
        console.setLayoutY(posy + canvas.getHeight());
        console.setMaxWidth(canvas.getWidth() / 2);
        console.setMaxHeight(height * 0.4);
        weatherChart.setLayoutY(console.getLayoutY());
        weatherChart.setLayoutX(console.getMaxWidth());
        weatherChart.setMaxHeight(console.getMaxHeight() / 2);
        resourceChart.setLayoutY(weatherChart.getLayoutY() + weatherChart.getMaxHeight());
        resourceChart.setLayoutX(weatherChart.getLayoutX());
        resourceChart.setMaxHeight(weatherChart.getMaxHeight());

    }

    public void setLayoutX(double posx) {
        canvas.setLayoutX(posx);
        lighting.setLayoutX(posx);
        setLayoutY(canvas.getLayoutY());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Canvas getLighting() {
        return lighting;
    }

    public TextArea getConsole() {
        return console;
    }

    public void setTime(LocalTime time) {
        model.setTime(time);
    }

    public LocalTime getTime() {
        return model.getTime();
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }


    public void simulate() {

        delay = 0;
        timer = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime.longValue()) / 1000000000.0;
                lastNanoTime = currentNanoTime;
                delay += elapsedTime;
                if (delay >= (1 / (double) fps)) {
                    player.update();
                    frame = frame >= fps ? 0 : ++frame;

                    // animation and collision detection
                    canvasGC.setGlobalAlpha(1);
                    canvasGC.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    model.sort();
                    player.animate(frame, fps);
                    boolean inbounds = false;
                    boolean collided = false;
                    for (Sprite sprite : model.getSprites()) {
                        if (sprite.getName().equals("clock")) {
                            sprite.animate(frame, fps);
                        }
                        if (!sprite.isPassable() && player.getBodyBoundary().intersects(sprite.getBoundary())) {
                            collided = true;
                        }
                        if (player.getFeetBoundary().intersects(sprite.getBoundary())) {
                            inbounds = true;
                        }
                        if (!sprite.getName().equals("clock")) {
                            if (player.getProximityBoundary().intersects(sprite.getBoundary())) {
                                sprite.animate(frame, fps);
                                if (sprite.getSmartObj() != null) {
                                    sprite.getSmartObj().turnOn();
                                    sprite.setTripped(true);
                                }
                            } else {
                                sprite.resetAnimation();
                                sprite.setAnimated(true);
                                if (sprite.getSmartObj() != null && sprite.isTripped()) {
                                    sprite.getSmartObj().turnOff();
                                }
                                sprite.setTripped(false);

                            }
                        }
                        if (sprite.getSmartObj() != null) {
                            if (sprite.getSmartObj().isOn()) {
                                sprite.animate(frame, fps);
                            }
                        }

                        sprite.render(canvasGC);

                    }
                    if (!inbounds || collided) {
                        player.setPosition(player.getLastX(), player.getLastY());
                    } else {
                        player.setLastX(player.getX());
                        player.setLastY(player.getY());
                    }

                    if (tempSprite != null) {
                        canvasGC.setGlobalAlpha(0.5);
                        tempSprite.render(canvasGC);
                        canvasGC.setLineWidth(2);
                        canvasGC.strokeRect(tempSprite.getX(), tempSprite.getY(),
                                tempSprite.getWidth(), tempSprite.getHeight());
                    }

                    canvasGC.setGlobalAlpha(1);
                    player.render(canvasGC);


                    if (frame == fps) { //every second

                        model.simulate();
                        console.setText(model.getStatus());
                        model.setTime(model.getTime().plusMinutes(1));
                        if (model.getTime().getMinute() % 10 == 0) {
                            refreshCharts();
                        }

                        lightGC.clearRect(0, 0, lighting.getWidth(), lighting.getHeight());
                        lightGC.setGlobalAlpha(((model.getLight() - 100) / 100) * -1);
                        lightGC.setFill(Color.BLACK);
                        lightGC.fillRect(0, 0, lighting.getWidth(), lighting.getHeight());
                        for (Sprite sprite : model.getSprites()) {
                            if (sprite.getSmartObj() != null) {
                                if (sprite.getSmartObj().getName().equals("Light") && sprite.getSmartObj().isOn()) {
                                    lightGC.clearRect(sprite.getX() - 60 * 2, sprite.getY(), 60 * 4, 60 * 3);
                                }
                            }
                        }
                    }
                    delay = 0;
                }

            }
        };
    }

    public void stopTime() {
        timer.stop();
    }

    public void startTime() {
        timer.start();
    }

    public AreaChart<Number, Number> getWeatherChart() {
        return weatherChart;
    }

    public AreaChart<Number, Number> getResourceChart() {
        return resourceChart;
    }

    public Sprite getSelectedSprite() {
        return selectedSprite;
    }

    public Sprite getTempSprite() {
        return tempSprite;
    }

    public SmartHouse getModel() {
        return model;
    }

    public void handleMouseMovement(MouseEvent event) {
        double tempX;
        double tempY;
        boolean intersected = false;
        interSprite = null;
        Rectangle2D mouseBoundary = new Rectangle2D(event.getX(), event.getY(), 5, 5);
        for (Sprite sprite : model.getSprites()) {
            if (mouseBoundary.intersects(sprite.getBoundary())) {

                intersected = true;
                interSprite = sprite;
                if (selectedSprite != null) {
                    try {
                        tempSprite = (Sprite) selectedSprite.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    tempSprite.setImage(selectedSprite.getImage());
                    tempX = sprite.getBoundary().getMinX();
                    tempY = sprite.getBoundary().getMinY();
                    if (selectedSprite.getZ() == 0) {
                        if (event.getY() < (sprite.getY() + (sprite.getImage().getHeight() * 0.30))) {
                            tempY = sprite.getBoundary().getMinY() - tempSprite.getImage().getHeight();
                            tempX = sprite.getBoundary().getMinX();
                        } else if (event.getY() > ((sprite.getY() + (sprite.getImage().getHeight() * 0.70)))) {
                            tempY = sprite.getBoundary().getMaxY();
                            tempX = sprite.getBoundary().getMinX();
                        } else if (event.getX() > ((sprite.getX() + (sprite.getImage().getWidth() / 2)))) {
                            tempY = sprite.getBoundary().getMinY();
                            tempX = sprite.getBoundary().getMaxX();
                        } else if (event.getX() < ((sprite.getX() + (sprite.getImage().getWidth() / 2)))) {
                            tempY = sprite.getBoundary().getMinY();
                            tempX = sprite.getBoundary().getMinX() - tempSprite.getImage().getWidth();
                        }
                    } else if (selectedSprite.getZ() == 2) {
                        tempY = sprite.getY();
                        tempX = sprite.getX();
                    } else {
                        tempY = (sprite.getBoundary().getMaxY() - tempSprite.getHeight() - 32);
                        tempX = sprite.getX();
                    }


                } else {
                    try {
                        tempSprite = (Sprite) sprite.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    tempSprite.setImage(sprite.getImage());
                    tempX = sprite.getBoundary().getMinX();
                    tempY = sprite.getBoundary().getMinY();
                }
                tempSprite.setPosition(tempX, tempY);
            }
        }
        if (!intersected) tempSprite = null;
    }

    public Sprite getInterSprite() {
        return interSprite;
    }

    public void setInterSprite(Sprite interSprite) {
        this.interSprite = interSprite;
    }

    public void setTempSprite(Sprite tempSprite) {
        this.tempSprite = tempSprite;
    }

    public Player getPlayer() {
        return player;
    }

    public void setSelectedSprite(Sprite selectedSprite) {
        this.selectedSprite = selectedSprite;
    }
}