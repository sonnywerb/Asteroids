/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author ericchan
 */
public class Projectile extends Character {
    
    private boolean isActive;
    
    public Projectile(int x, int y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
        this.isActive = true;
        
        this.getCharacter().setFill(Color.TRANSPARENT);
        this.getCharacter().setStroke(Color.WHITE);
    }
    
    public boolean isActive() {
        return this.isActive;
    }
    
    public void setActive(boolean bool) {
        this.isActive = bool;
    }
    
    @Override
    public void move() {
        this.getCharacter().setTranslateX(this.getCharacter().getTranslateX() + this.getMovement().getX());
        this.getCharacter().setTranslateY(this.getCharacter().getTranslateY() + this.getMovement().getY());
        
        if (this.getCharacter().getTranslateX() < 0) {
            this.setActive(false);
        }
        
        if (this.getCharacter().getTranslateX() > AsteroidsApplication.WIDTH) {
            this.setActive(false);
        }
        
        if (this.getCharacter().getTranslateY() < 0) {
            this.setActive(false);
        }
        
        if (this.getCharacter().getTranslateY() > AsteroidsApplication.HEIGHT) {
            this.setActive(false);
        }
    }

}
