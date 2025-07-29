package com.godgame.service;

import com.godgame.model.GameState;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class GameService {
    private final ConcurrentHashMap<String, GameState> games = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    
    public GameService() {
        // Start game update loop
        scheduler.scheduleAtFixedRate(this::updateAllGames, 0, 16, TimeUnit.MILLISECONDS);
        // Start comet spawning
        scheduler.scheduleAtFixedRate(this::spawnComets, 2000, 1000, TimeUnit.MILLISECONDS);
    }
    
    public GameState createGame(String sessionId) {
        GameState gameState = new GameState();
        games.put(sessionId, gameState);
        return gameState;
    }
    
    public GameState getGame(String sessionId) {
        return games.get(sessionId);
    }
    
    public void removeGame(String sessionId) {
        games.remove(sessionId);
    }
    
    public void movePlayer(String sessionId, int deltaX, int deltaY) {
        GameState game = games.get(sessionId);
        if (game != null && game.isGameActive()) {
            game.getPlayer().move(deltaX, deltaY);
        }
    }
    
    public GameState restartGame(String sessionId) {
        GameState newGame = new GameState();
        games.put(sessionId, newGame);
        return newGame;
    }
    
    private void updateAllGames() {
        games.values().forEach(GameState::update);
    }
    
    private void spawnComets() {
        games.values().forEach(GameState::spawnComet);
    }
}
