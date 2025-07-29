package com.godgame.model;

public class Soul {
    private int x, y;
    private boolean collected;
    
    public Soul(int x, int y) {
        this.x = x;
        this.y = y;
        this.collected = false;
    }
    
    // Getters and setters
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isCollected() { return collected; }
    
    public void setCollected(boolean collected) { this.collected = collected; }
}
