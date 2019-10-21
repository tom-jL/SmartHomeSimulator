package model.sprites;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.fixtures.Fixture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite implements Cloneable {
    private String name;
    private String imagePath;
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private int positionZ;
    private double width;
    private double height;
    private ImageView imageView;
    private ArrayList<ArrayList<Image>> animations;
    protected boolean passable;
    protected boolean mountable;
    private Fixture smartObj;
    private rooms room;

    private int tilesX;
    private int tilesY;
    private double scale;
    private int tileX = 0;
    private int tileY = 0;
    private boolean animated;
    private boolean tripped;


    public enum rooms {
        Kitchen,
        Livingroom,
        Bathroom,
        Bedroom,
        Garage,
        Patio
    }


    public Sprite(String name, double x, double y, int z, String imagePath, int tilesX, int tilesy, double scale,
                  boolean passable, boolean mountable, rooms room, Fixture smartObj) {
        this.name = name;
        this.imagePath = imagePath;
        animations = new ArrayList<ArrayList<Image>>();
        setImage(imagePath);
        this.passable = passable;
        this.mountable = mountable;
        positionX = x;
        positionY = y;
        velocityX = 0;
        velocityY = 0;
        positionZ = z;
        this.smartObj = smartObj;
        this.room = room;
        setAnimations(tilesy, tilesX, scale);
        setImage(getAnimations().get(0).get(0));
        animated = true;
        tripped = false;
    }


    public Object clone() throws CloneNotSupportedException {

        return super.clone();
    }


    public String getName() {
        return name;
    }

    public void setImage(Image i) {
        image = i;
        imageView = new ImageView(image);
        width = i.getWidth();
        height = i.getHeight();
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void setImage(String filename) {
        //System.out.println(getClass().getResource(filename).toString());
        imagePath = filename;
        //System.out.println(filename);
        Image i = new Image(getClass().getResource(filename).toString());
        setImage(i);
    }

    public String getImagePath() {
        return imagePath;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Image getImage() {
        return image;
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void setPositionZ(int positionZ) {
        this.positionZ = positionZ;
    }

    public void bringForward() {
        this.positionZ++;
    }

    public void sendBack() {
        this.positionZ--;
    }

    public double getX() {
        return positionX;
    }

    public double getY() {
        return positionY;
    }

    public int getZ() {
        return positionZ;
    }

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y) {
        velocityX += x;
        velocityY += y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }


    public int getTilesX() {
        return tilesX;
    }

    public int getTilesY() {
        return tilesY;
    }

    public double getScale() {
        return scale;
    }


    public boolean isPassable() {
        return passable;
    }

    public boolean isMountable() {
        return mountable;
    }

    public void setSmartObj(Fixture smartObj) {
        this.smartObj = smartObj;
    }

    public Fixture getSmartObj() {
        return smartObj;
    }

    public rooms getRoom() {
        return room;
    }

    public void setRoom(rooms room) {
        this.room = room;
    }

    public void update() {
        positionX += velocityX;
        positionY += velocityY;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY, width, height);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Sprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }


    public String toString() {
        return this.getClass() + " Position: [" + positionX + "," + positionY + "]"
                + " Velocity: [" + velocityX + "," + velocityY + "]" + "Boundary: [" + width + "," + height + "]";
    }

    public void animate(int frames, int fps) {

        if (frames % (fps / (tilesX)) == 0 && animated) {
            tileX = tileX >= tilesX - 1 ? 0 : ++tileX;
            tileY = tileY >= tilesY - 1 ? 0 : ++tileY;
        }
        setImage(getAnimations().get(tileY).get(tileX));
        if (tileX == tilesX - 1 && !getName().equals("clock")) {
            animated = false;
        }
    }

    public void resetAnimation() {
        setImage(getAnimations().get(0).get(0));
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setTripped(boolean tripped) {
        this.tripped = tripped;
    }

    public boolean isTripped() {
        return tripped;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public void setAnimations(int tilesy, int tilesx, double scale) {
        this.tilesX = tilesx;
        this.tilesY = tilesy;
        this.scale = scale;
        int x = 0;
        int y = 0;
        BufferedImage img = SwingFXUtils.fromFXImage(getImage(), null);
        int scaleX = (int) (img.getWidth() * scale);
        int scaleY = (int) (img.getHeight() * scale);
        java.awt.Image image = img.getScaledInstance(scaleX, scaleY, java.awt.Image.SCALE_SMOOTH);
        BufferedImage buffered = new BufferedImage(scaleX, scaleY, BufferedImage.TYPE_INT_ARGB);
        buffered.getGraphics().drawImage(image, 0, 0, null);
        buffered.getGraphics().dispose();
        for (int i = 0; i < tilesy; i++) {
            ArrayList<Image> tempArray = new ArrayList<>();
            for (int j = 0; j < tilesx; j++) {
                tempArray.add(SwingFXUtils.toFXImage(buffered.getSubimage(x, y, buffered.getWidth() / tilesx, buffered.getHeight() / tilesy), null));
                x += buffered.getWidth() / tilesx;
            }
            y += buffered.getHeight() / tilesy;
            x = 0;
            animations.add(tempArray);
        }
    }

    public ArrayList<ArrayList<Image>> getAnimations() {
        return animations;
    }
}

