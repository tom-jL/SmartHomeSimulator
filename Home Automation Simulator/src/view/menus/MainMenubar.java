package view.menus;

import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MainMenubar extends MenuBar {

    private FileMenu fileMenu;
    private FileMenu simulationMenu;
    private FileMenu helpMenu;
    private PaletteMenu paletteMenu;

    public MainMenubar() {
        super();

        fileMenu = new FileMenu("File");
        fileMenu.getItems().add(new MenuItem("New"));
        fileMenu.getItems().add(new MenuItem("Save"));
        fileMenu.getItems().add(new MenuItem("Load"));
        fileMenu.getItems().add(new MenuItem("Exit"));


        simulationMenu = new FileMenu("Simulation");
        simulationMenu.getItems().add(new MenuItem("Show/Hide info"));
        simulationMenu.getItems().add(new MenuItem("Run"));
        simulationMenu.getItems().add(new MenuItem("Stop"));
        simulationMenu.getItems().add(new MenuItem("Start"));

        helpMenu = new FileMenu("Help");
        helpMenu.getItems().add(new MenuItem("About"));
        helpMenu.getItems().add(new MenuItem("User Guide"));

        paletteMenu = new PaletteMenu();

        getMenus().addAll(fileMenu, simulationMenu, paletteMenu, helpMenu);
    }

    public FileMenu getFileMenu() {
        return fileMenu;
    }

    public FileMenu getHelpMenu() {
        return helpMenu;
    }

    public FileMenu getSimulationMenu() {
        return simulationMenu;
    }

    public PaletteMenu getPaletteMenu() {
        return paletteMenu;
    }

}
