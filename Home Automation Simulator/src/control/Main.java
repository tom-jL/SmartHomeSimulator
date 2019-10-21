package control;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.fixtures.Fixture;
import model.sprites.Sprite;
import view.Palette;
import view.View;
import view.menus.MainMenubar;
import view.menus.SpriteMenu;

import java.time.LocalTime;
import java.util.ConcurrentModificationException;


public class Main extends Application {


    private MenuBar spriteMenubar;
    private Palette currentPalette;


    @Override
    public void start(Stage stage) {
        Group root = new Group();
        stage.setResizable(false);
        stage.setMinHeight(600);
        stage.setMinWidth(1024);
        stage.setTitle("Smart Home Simulator");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(1);
            }
        });
        stage.setScene(new Scene(root));
        root.setAutoSizeChildren(true);
        MainMenubar mainMenu = new MainMenubar();
        mainMenu.setMinHeight(28);
        View view = new View(stage.getMinWidth(), stage.getMinHeight(), 10);
        currentPalette = new Palette();
        root.getChildren().add(view.getCanvas());
        root.getChildren().add(view.getLighting());
        root.getChildren().add(view.getConsole());
        root.getChildren().add(view.getWeatherChart());
        root.getChildren().add(view.getResourceChart());
        root.getChildren().add(mainMenu);
        root.getChildren().add(currentPalette);

        view.setLayoutY(+mainMenu.getMinHeight());
        currentPalette.setLayoutY(+mainMenu.getMinHeight());

        mainMenu.getFileMenu().getItem("Save").setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.getModel().saveHouse("sprites.xml");
            }
        });

        mainMenu.getFileMenu().getItem("Load").setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.getModel().loadHouse("sprites.xml");
                view.setTime(LocalTime.of(4, 59));
            }
        });

        mainMenu.getFileMenu().getItem("New").setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.newModel();
            }
        });

        mainMenu.getFileMenu().getItem("Exit").setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        mainMenu.getSimulationMenu().getItem("Show/Hide info").setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (view.getConsole().isVisible()) {
                    view.getConsole().setVisible(false);
                } else {
                    view.getConsole().setVisible(true);
                }
            }
        });

        mainMenu.getSimulationMenu().getItem("Run").setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.getModel().setTime(LocalTime.of(4, 59));
                view.startTime();
            }
        });

        mainMenu.getSimulationMenu().getItem("Stop").setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.stopTime();
            }
        });

        mainMenu.getSimulationMenu().getItem("Start").setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.startTime();
            }
        });


        for (MenuItem item : mainMenu.getPaletteMenu().getItems()) {
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mainMenu.getPaletteMenu().setPalette(event.getSource());
                    root.getChildren().remove(currentPalette);
                    currentPalette = mainMenu.getPaletteMenu().getCurrentPalette();
                    for (Sprite sprite : currentPalette.getSprites()) {
                        sprite.getImageView().setOnMouseClicked(
                                new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        if (root.getChildren().contains(spriteMenubar)) {
                                            root.getChildren().remove(spriteMenubar);
                                        }
                                        view.getCanvas().setCursor(new ImageCursor(sprite.getImage()));
                                        try {
                                            view.setSelectedSprite((Sprite) sprite.clone());
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );
                    }
                    root.getChildren().add(currentPalette);
                    currentPalette.setLayoutY(+mainMenu.getMinHeight());
                }
            });
        }

        view.getCanvas().setOnMousePressed(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (root.getChildren().contains(spriteMenubar)) {
                            root.getChildren().remove(spriteMenubar);
                        }
                        if (event.isPrimaryButtonDown()) {
                            if (view.getSelectedSprite() != null && view.getTempSprite() != null) {
                                try {
                                    Sprite newSprite = (Sprite) view.getSelectedSprite().clone();
                                    newSprite.setImage(view.getSelectedSprite().getImage());
                                    newSprite.setPosition(view.getTempSprite().getX(), view.getTempSprite().getY());
                                    if (view.getSelectedSprite().getSmartObj() != null) {
                                        newSprite.setSmartObj(new Fixture(
                                                view.getSelectedSprite().getSmartObj().getName(),
                                                view.getSelectedSprite().getSmartObj().getPowerUsage(),
                                                view.getSelectedSprite().getSmartObj().getWaterUsage()));
                                    }
                                    view.getModel().add(newSprite);
                                } catch (CloneNotSupportedException | ConcurrentModificationException e) {
                                    System.out.println("error");
                                }
                            } else if (view.getInterSprite() != null) {
                                if (view.getInterSprite().getSmartObj() != null) {
                                    spriteMenubar = new MenuBar();
                                    SpriteMenu spriteMenu = new SpriteMenu(view.getInterSprite());
                                    spriteMenu.getDeleteItem().setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            try {
                                                view.getModel().getSprites().remove(spriteMenu.getSprite());
                                            } catch (ConcurrentModificationException e) {
                                                System.out.println();
                                            }
                                        }
                                    });
                                    spriteMenu.getInfoItem().setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            System.out.println(spriteMenu.getSprite().getSmartObj().getStatus());
                                        }
                                    });
                                    spriteMenubar.getMenus().add(spriteMenu);
                                    root.getChildren().add(spriteMenubar);
                                    spriteMenubar.setLayoutX(event.getX());
                                    spriteMenubar.setLayoutY(event.getY());
                                } else {
                                    spriteMenubar = new MenuBar();
                                    Sprite sprite = view.getInterSprite();
                                    Menu spriteMenu = new Menu(sprite.getName());
                                    MenuItem deleteItem = new MenuItem("Delete");
                                    deleteItem.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            try {
                                                view.getModel().getSprites().remove(sprite);
                                            } catch (ConcurrentModificationException e) {
                                                System.out.println();
                                            }
                                        }
                                    });
                                    spriteMenu.getItems().add(deleteItem);
                                    spriteMenubar.getMenus().add(spriteMenu);
                                    root.getChildren().add(spriteMenubar);
                                    spriteMenubar.setLayoutX(event.getX());
                                    spriteMenubar.setLayoutY(event.getY());
                                }
                            }
                        } else if (event.isSecondaryButtonDown()) {

                            view.setInterSprite(null);
                            view.setTempSprite(null);
                            view.setSelectedSprite(null);
                            view.getCanvas().setCursor(null);
                        }
                    }
                }
        );


        view.getCanvas().getScene().setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        //view.getPlayer().setVelocity(0,0);
                        if (event.getCode().equals(KeyCode.W)) {
                            view.getPlayer().setVelocity(0, -5);
                        }
                        if (event.getCode().equals(KeyCode.S)) {
                            view.getPlayer().setVelocity(0, 5);
                        }
                        if (event.getCode().equals(KeyCode.A)) {
                            view.getPlayer().setVelocity(-5, 0);
                        }
                        if (event.getCode().equals((KeyCode.D))) {
                            view.getPlayer().setVelocity(5, 0);
                        }
                    }
                }
        );

        view.getCanvas().getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                view.getPlayer().setVelocity(0, 0);
            }
        });

        view.getCanvas().requestFocus();


        stage.show();
        view.startTime();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
