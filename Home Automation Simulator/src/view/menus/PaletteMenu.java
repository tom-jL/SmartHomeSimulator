package view.menus;

import control.XMLParser;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import model.sprites.Sprite;
import view.Palette;

import java.util.ArrayList;

public class PaletteMenu extends Menu {

    private Palette currentPalette;

    private RadioMenuItem kitchenItem;
    private RadioMenuItem livingItem;
    private RadioMenuItem bathItem;
    private RadioMenuItem bedItem;
    private RadioMenuItem garageItem;
    private RadioMenuItem patioItem;

    private Palette kitchenPalette;
    private Palette livingPalette;
    private Palette bathPalette;
    private Palette bedPalette;
    private Palette garagePalette;
    private Palette patioPalette;

    private XMLParser xmlParser;

    public PaletteMenu() {
        super("Room Palette");

        xmlParser = new XMLParser();
        ArrayList<Sprite> paletteSprites = xmlParser.loadXML("palettes.xml");

        kitchenPalette = new Palette();
        livingPalette = new Palette();
        bathPalette = new Palette();
        bedPalette = new Palette();
        garagePalette = new Palette();
        patioPalette = new Palette();
        for (Sprite sprite : paletteSprites) {
            switch (sprite.getRoom()) {
                case Kitchen:
                    kitchenPalette.getSprites().add(sprite);
                    break;
                case Livingroom:
                    livingPalette.getSprites().add(sprite);
                    break;
                case Bathroom:
                    bathPalette.getSprites().add(sprite);
                    break;
                case Bedroom:
                    bedPalette.getSprites().add(sprite);
                    break;
                case Garage:
                    garagePalette.getSprites().add(sprite);
                    break;
                case Patio:
                    patioPalette.getSprites().add(sprite);
                    break;
            }


        }
        kitchenPalette.Populate();
        livingPalette.Populate();
        bathPalette.Populate();
        bedPalette.Populate();
        garagePalette.Populate();
        patioPalette.Populate();

        currentPalette = kitchenPalette;

        //RadioMenuItem houseItem = new RadioMenuItem("House Palette");
        kitchenItem = new RadioMenuItem("Kitchen Palette");
        livingItem = new RadioMenuItem("Living Room Palette");
        bathItem = new RadioMenuItem("Bathroom Palette");
        bedItem = new RadioMenuItem("Bedroom Palette");
        garageItem = new RadioMenuItem("Garage Palette");
        patioItem = new RadioMenuItem("Patio Palette");

        ToggleGroup paletteGroup = new ToggleGroup();
        paletteGroup.getToggles().addAll(
                //houseItem,
                kitchenItem,
                livingItem,
                bathItem,
                bedItem,
                garageItem,
                patioItem
        );
        getItems().addAll(
                //houseItem,
                kitchenItem,
                livingItem,
                bathItem,
                bedItem,
                garageItem,
                patioItem
        );

    }

    public Palette getCurrentPalette() {
        return currentPalette;
    }

    public void setPalette(Object source) {
        if (source.equals(kitchenItem)) {
            currentPalette = kitchenPalette;
        }
        if (source.equals(livingItem)) {
            currentPalette = livingPalette;
        }
        if (source.equals(bathItem)) {
            currentPalette = bathPalette;
        }
        if (source.equals(bedItem)) {
            currentPalette = bedPalette;
        }
        if (source.equals(garageItem)) {
            currentPalette = garagePalette;
        }
        if (source.equals(patioItem)) {
            currentPalette = patioPalette;
        }
    }
}
