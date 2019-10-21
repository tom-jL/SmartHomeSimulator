package model.sprites;

import javafx.geometry.Rectangle2D;

public class Player extends Sprite {

    private int frame;

    private double lastX;
    private double lastY;

    public Player(double x, double y) {
        super("player",
                x, y, 1,
                "img/player.png",
                4, 4, 3.5f,
                true, false,
                rooms.Livingroom, null);
        setImage(getAnimations().get(1).get(0));
        lastX = getX();
        lastY = getY();
    }

    public Rectangle2D getFeetBoundary() {
        return new Rectangle2D(getBoundary().getMinX() + getWidth() / 2, getBoundary().getMaxY() - 5, 1, 1);
    }

    public Rectangle2D getBodyBoundary() {
        return new Rectangle2D(getX() + getWidth() / 4, getBoundary().getMaxY() - 5, getWidth() - getWidth() / 2, 2);
    }

    public Rectangle2D getProximityBoundary() {
        return new Rectangle2D(getX() - getWidth(), getY() - getHeight(), getWidth() * 3, getHeight() * 1.5);
    }

    @Override
    public void animate(int frames, int fps) {
        frame = frame >= 3 ? 0 : ++frame;
        if (getVelocityY() < 0) {
            setImage(getAnimations().get(0).get(frame));
        } else if (getVelocityY() > 0) {
            setImage(getAnimations().get(1).get(frame));
        } else if (getVelocityX() > 0) {
            setImage(getAnimations().get(2).get(frame));
        } else if (getVelocityX() < 0) {
            setImage(getAnimations().get(3).get(frame));
        }

    }


    public double getLastX() {
        return lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public void setLastX(double lastX) {
        this.lastX = lastX;
    }

    public void setLastY(double lastY) {
        this.lastY = lastY;
    }
}
