package com.godgame.model;

public class Player {
    private int x, y;
    private boolean alive;
    private int soulsCollected;
    private String name;
    
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.alive = true;
        this.soulsCollected = 0;
        this.name = "─═God═─";
    }
    
    public void move(int deltaX, int deltaY) {
        if (!alive) return;
        
        this.x = Math.max(0, Math.min(1150, this.x + deltaX));
        this.y = Math.max(0, Math.min(650, this.y + deltaY));
    }
    
    public void incrementSoulsCollected() {
        this.soulsCollected++;
    }
    
    // Getters and setters
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return alive; }
    public int getSoulsCollected() { return soulsCollected; }
    public String getName() { return name; }
    
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setAlive(boolean alive) { this.alive = alive; }
    public void setSoulsCollected(int soulsCollected) { this.soulsCollected = soulsCollected; }
}
