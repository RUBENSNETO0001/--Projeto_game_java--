package com.godgame.model;

public class Comet {
    private int x, y;
    private int speed;
    private boolean active;
    
    public Comet(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.active = true;
    }
    
    public void update(long deltaTime) {
        if (!active) return;
        
        x -= speed;
        if (x < -100) {
            active = false;
        }
    }
    
    // Getters and setters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; }
    public boolean isActive() { return active; }
    
    public void setActive(boolean active) { this.active = active; }
}
