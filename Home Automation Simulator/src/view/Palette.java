package view;

import javafx.scene.layout.GridPane;
import model.sprites.Sprite;

import java.util.ArrayList;

public class Palette extends GridPane {

    protected ArrayList<Sprite> sprites;

    public Palette() {
        super();
        setHgap(5);
        setVgap(5);
        setStyle("-fx-border-width: 5px; -fx-padding: 5; -fx-border-radius: 4; -fx-border-color: grey; -fx-alignment: center;");
        sprites = new ArrayList<>();
    }

    public void Populate() {
        int rowIndex = 0;
        int columnIndex = 0;
        for (Sprite sprite : sprites) {
            //ImageView image = new ImageView(sprite.getImage());
            //cursors.add(sprite.getImageView());
            sprite.getImageView().setFitHeight(50);
            sprite.getImageView().setFitWidth(30);
            add(sprite.getImageView(), columnIndex, rowIndex);
            rowIndex += 1;
            if (rowIndex == 5) {
                columnIndex += 1;
                rowIndex = 0;
            }
        }
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }
}
