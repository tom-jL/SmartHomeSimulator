package view.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class FileMenu extends Menu {


    public FileMenu(String name) {
        super(name);
    }

    public MenuItem getItem(String string) {
        for (MenuItem item : getItems()) {
            if (item.getText().equals(string)) {
                return item;
            }
        }
        return null;
    }

}
