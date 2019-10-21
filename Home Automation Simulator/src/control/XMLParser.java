package control;

import model.fixtures.Fixture;
import model.sprites.Sprite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jc232009 on 8/10/18.
 */
public class XMLParser {

    ArrayList<Sprite> sprites;

    public void saveXML(ArrayList<Sprite> sprites, String file) {
        try {
            Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = xml.createElement(file);
            xml.appendChild(root);
            this.sprites = sprites;

            for (Sprite.rooms room : Sprite.rooms.values()) {
                root.appendChild(createRoom(xml, room));
            }

            xml.getDocumentElement().normalize();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(xml);
            StreamResult result = new StreamResult(new File(file));
            transformer.transform(source, result);

            //StreamResult consoleResult = new StreamResult(System.out);
            //transformer.transform(source, consoleResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Node createRoom(Document xml, Sprite.rooms id) {
        Element room = xml.createElement("room");
        room.setAttribute("name", id.toString());
        for (Sprite sprite : sprites) {
            if (sprite.getRoom().equals(id)) {
                room.appendChild(createSprite(xml, sprite));
            }
        }
        return room;
    }

    private Node createSprite(Document xml, Sprite sprite) {

        Element spriteElement = xml.createElement("sprite");

        spriteElement.setAttribute("name", sprite.getName());

        spriteElement.appendChild(createField(xml, "x", String.valueOf(sprite.getX())));
        spriteElement.appendChild(createField(xml, "y", String.valueOf(sprite.getY())));
        spriteElement.appendChild(createField(xml, "z", String.valueOf(sprite.getZ())));
        spriteElement.appendChild(createField(xml, "tilesX", String.valueOf(sprite.getTilesX())));
        spriteElement.appendChild(createField(xml, "tilesY", String.valueOf(sprite.getTilesY())));
        spriteElement.appendChild(createField(xml, "scale", String.valueOf(sprite.getScale())));
        spriteElement.appendChild(createField(xml, "path", sprite.getImagePath()));
        spriteElement.appendChild(createField(xml, "passable", String.valueOf(sprite.isPassable())));
        spriteElement.appendChild(createField(xml, "mountable", String.valueOf(sprite.isMountable())));


        if (sprite.getSmartObj() != null) {
            Fixture smartObj = sprite.getSmartObj();
            spriteElement.appendChild(createFixture(xml, smartObj));
        }

        return spriteElement;
    }

    private Node createFixture(Document xml, Fixture fixture) {
        Element fixtureElement = xml.createElement("smartobject");
        fixtureElement.setAttribute("name", fixture.getName());

        fixtureElement.appendChild(createField(xml, "powerusage", String.valueOf(fixture.getPowerUsage())));
        fixtureElement.appendChild(createField(xml, "waterusage", String.valueOf(fixture.getWaterUsage())));

        Element schedule = xml.createElement("schedule");
        for (LocalTime time : fixture.getSchedule().keySet()) {
            Element timeElement = xml.createElement("time");
            timeElement.setAttribute("value", time.toString());
            for (String action : fixture.getSchedule().get(time)) {
                timeElement.appendChild(createField(xml, "action", action));
            }
            schedule.appendChild(timeElement);
        }
        fixtureElement.appendChild(schedule);

        return fixtureElement;

    }


    private Node createField(Document xml, String name, String value) {

        Element node = xml.createElement(name);
        node.appendChild(xml.createTextNode(value));
        return node;
    }

    public ArrayList<Sprite> loadXML(String file) {

        try {
            Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

            xml.getDocumentElement().normalize();

            sprites = new ArrayList<>();

            NodeList roomList = xml.getElementsByTagName("room");
            for (int r = 0; r < roomList.getLength(); r++) {
                if (roomList.item(r).getNodeType() == Node.ELEMENT_NODE) {
                    Element room = (Element) roomList.item(r);
                    NodeList spriteList = room.getElementsByTagName("sprite");
                    for (int s = 0; s < spriteList.getLength(); s++) {
                        if (spriteList.item(s).getNodeType() == Node.ELEMENT_NODE) {
                            Element sprite = (Element) spriteList.item(s);

                            String spriteName = sprite.getAttribute("name");
                            double spriteX = Double.parseDouble(sprite.getElementsByTagName("x").item(0)
                                    .getTextContent());
                            double spriteY = Double.parseDouble(sprite.getElementsByTagName("y").item(0)
                                    .getTextContent());
                            int spriteZ = Integer.parseInt(sprite.getElementsByTagName("z").item(0).getTextContent());
                            int tilesX = Integer.parseInt(sprite.getElementsByTagName("tilesX").item(0)
                                    .getTextContent());
                            int tilesY = Integer.parseInt(sprite.getElementsByTagName("tilesY").item(0)
                                    .getTextContent());
                            double scale = Double.parseDouble(sprite.getElementsByTagName("scale").item(0)
                                    .getTextContent());
                            String imagePath = sprite.getElementsByTagName("path").item(0).getTextContent();
                            boolean passable = Boolean.parseBoolean(sprite.getElementsByTagName("passable")
                                    .item(0).getTextContent());
                            boolean mountable = Boolean.parseBoolean(sprite.getElementsByTagName("mountable")
                                    .item(0).getTextContent());
                            Sprite.rooms roomName = Sprite.rooms.valueOf(room.getAttribute("name"));

                            Fixture smartObj = null;
                            if (sprite.getElementsByTagName("smartobject").getLength() >= 1) {
                                if (sprite.getElementsByTagName("smartobject").item(0).getNodeType()
                                        == Node.ELEMENT_NODE) {
                                    Element smart = (Element) sprite.getElementsByTagName("smartobject").item(0);
                                    String smartName = smart.getAttribute("name");
                                    double powerusage = Double.parseDouble(smart.getElementsByTagName("powerusage")
                                            .item(0).getTextContent());
                                    double waterusage = Double.parseDouble(smart.getElementsByTagName("waterusage")
                                            .item(0).getTextContent());
                                    HashMap<LocalTime, ArrayList<String>> scheduled = new HashMap<>();

                                    if (smart.getElementsByTagName("schedule").item(0).getNodeType()
                                            == Node.ELEMENT_NODE) {
                                        Element schedule = (Element) smart.getElementsByTagName("schedule").item(0);
                                        NodeList timeList = schedule.getElementsByTagName("time");
                                        for (int t = 0; t < timeList.getLength(); t++) {
                                            if (timeList.item(t).getNodeType() == Node.ELEMENT_NODE) {
                                                Element time = (Element) timeList.item(t);
                                                LocalTime localTime = LocalTime.parse(time.getAttribute("value"));
                                                NodeList actionList = time.getElementsByTagName("action");
                                                ArrayList<String> actions = new ArrayList<>();
                                                for (int a = 0; a < actionList.getLength(); a++) {
                                                    actions.add(actionList.item(a).getTextContent());
                                                }
                                                scheduled.put(localTime, actions);
                                            }
                                        }
                                    }

                                    smartObj = new Fixture(smartName, powerusage, waterusage);
                                    smartObj.setSchedule(scheduled);
                                }
                            }

                            Sprite spriteObj = new Sprite(spriteName, spriteX, spriteY, spriteZ, imagePath,
                                    tilesX, tilesY, scale, passable, mountable, roomName, smartObj);

                            sprites.add(spriteObj);
                        }
                    }
                }

            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return sprites;
    }
}
