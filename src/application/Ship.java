package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Ship extends Character {
    
    public Ship(int x, int y) {
        super(new Polygon(-4, -4, 8, 0, -4, 4), x, y);
        
        this.getCharacter().setFill(Color.TRANSPARENT);
        this.getCharacter().setStroke(Color.WHITE);
    }
}
