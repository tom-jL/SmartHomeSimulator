package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import model.sprites.Sprite;

import java.time.LocalTime;

public class SpriteMenu extends Menu {

    private MenuItem infoItem;
    private Menu actionMenu;
    private Menu scheduleMenu;
    private MenuItem deleteItem;

    private Sprite sprite;


    public SpriteMenu(Sprite sprite) {
        super(sprite.getSmartObj().getName());

        this.sprite = sprite;
        infoItem = new MenuItem("Info");
        deleteItem = new MenuItem("Delete");
        actionMenu = new Menu("Actions");
        scheduleMenu = new Menu("Schedule");

        Menu addMenu = new Menu("Add");
        Menu removeMenu = new Menu("Remove");

        for (String action : sprite.getSmartObj().getActions()) {

            MenuItem runAction = new MenuItem(action);
            runAction.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    sprite.getSmartObj().run(action);
                }
            });
            actionMenu.getItems().add(runAction);

            Menu scheduleAction = new Menu(action);

            Spinner<Integer> hourSpinner = new Spinner(0, 23, 8);
            Spinner<Integer> minuteSpinner = new Spinner(0, 59, 0);
            hourSpinner.setPrefWidth(50);
            minuteSpinner.setPrefWidth(50);
            CustomMenuItem hourItem = new CustomMenuItem();
            hourItem.setContent(hourSpinner);
            hourItem.setHideOnClick(false);


            CustomMenuItem minuteItem = new CustomMenuItem();
            minuteItem.setContent(minuteSpinner);
            minuteItem.setHideOnClick(false);

            MenuItem confirmItem = new MenuItem("Okay");

            confirmItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    sprite.getSmartObj().schedule(LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue()), action);
                }
            });

            scheduleAction.getItems().addAll(hourItem, minuteItem, confirmItem);
            addMenu.getItems().add(scheduleAction);
        }

        scheduleMenu.getItems().add(addMenu);

        for (LocalTime time : sprite.getSmartObj().getSchedule().keySet()) {
            for (String action : sprite.getSmartObj().getSchedule().get(time)) {
                MenuItem removeSchedule = new MenuItem(time.toString() + " " + action);
                removeSchedule.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        sprite.getSmartObj().removeSchedule(time, action);
                    }
                });
                removeMenu.getItems().add(removeSchedule);
            }
        }
        scheduleMenu.getItems().add(removeMenu);

        getItems().addAll(infoItem, deleteItem, actionMenu, scheduleMenu);

    }

    public MenuItem getInfoItem() {
        return infoItem;
    }

    public MenuItem getDeleteItem() {
        return deleteItem;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
