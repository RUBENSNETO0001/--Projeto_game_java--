package com.godgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
    private Player player;
    private List<Soul> souls;
    private List<Comet> comets;
    private boolean gameActive;
    private boolean gameWon;
    private long lastUpdate;
    
    public GameState() {
        this.player = new Player(100, 100);
        this.souls = new CopyOnWriteArrayList<>();
        this.comets = new CopyOnWriteArrayList<>();
        this.gameActive = true;
        this.gameWon = false;
        this.lastUpdate = System.currentTimeMillis();
        initializeSouls();
    }
    
    private void initializeSouls() {
        // Create 5 souls at random positions
        for (int i = 0; i < 5; i++) {
            int x = (int) (Math.random() * 1000) + 100;
            int y = (int) (Math.random() * 500) + 100;
            souls.add(new Soul(x, y));
        }
    }
    
    public void update() {
        if (!gameActive) return;
        
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastUpdate;
        lastUpdate = currentTime;
        
        // Update comets
        comets.removeIf(comet -> {
            comet.update(deltaTime);
            return !comet.isActive();
        });
        
        // Check collisions
        checkCollisions();
        
        // Check win condition
        if (souls.stream().allMatch(Soul::isCollected)) {
            gameWon = true;
            gameActive = false;
        }
    }
    
    private void checkCollisions() {
        // Check soul collection
        for (Soul soul : souls) {
            if (!soul.isCollected() && isColliding(player, soul)) {
                soul.setCollected(true);
                player.incrementSoulsCollected();
            }
        }
        
        // Check comet collision
        for (Comet comet : comets) {
            if (comet.isActive() && isColliding(player, comet)) {
                player.setAlive(false);
                gameActive = false;
                break;
            }
        }
    }
    
    private boolean isColliding(Player player, Soul soul) {
        return Math.abs(player.getX() - soul.getX()) < 40 && 
               Math.abs(player.getY() - soul.getY()) < 40;
    }
    
    private boolean isColliding(Player player, Comet comet) {
        return Math.abs(player.getX() - comet.getX()) < 50 && 
               Math.abs(player.getY() - comet.getY()) < 50;
    }
    
    public void spawnComet() {
        if (gameActive) {
            int y = (int) (Math.random() * 600) + 50;
            int speed = (int) (Math.random() * 10) + 3;
            comets.add(new Comet(1200, y, speed));
        }
    }
    
    // Getters and setters
    public Player getPlayer() { return player; }
    public List<Soul> getSouls() { return souls; }
    public List<Comet> getComets() { return comets; }
    public boolean isGameActive() { return gameActive; }
    public boolean isGameWon() { return gameWon; }
    
    public void setGameActive(boolean gameActive) { this.gameActive = gameActive; }
    public void setGameWon(boolean gameWon) { this.gameWon = gameWon; }
}
