package model.house;

public class EventTest {

    public static void main(String[] args) {
        Event event = new Event(50, 50);
        assert (event.getThreshold() == 50);
        assert (event.getChance() == 50);
    }
}
