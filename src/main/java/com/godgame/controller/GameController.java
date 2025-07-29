package com.godgame.controller;

import com.godgame.model.GameState;
import com.godgame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @PostMapping("/start")
    public ResponseEntity<GameState> startGame(HttpSession session) {
        String sessionId = session.getId();
        GameState gameState = gameService.createGame(sessionId);
        return ResponseEntity.ok(gameState);
    }
    
    @GetMapping("/state")
    public ResponseEntity<GameState> getGameState(HttpSession session) {
        String sessionId = session.getId();
        GameState gameState = gameService.getGame(sessionId);
        if (gameState == null) {
            gameState = gameService.createGame(sessionId);
        }
        return ResponseEntity.ok(gameState);
    }
    
    @PostMapping("/move")
    public ResponseEntity<Void> movePlayer(
            @RequestParam int deltaX, 
            @RequestParam int deltaY, 
            HttpSession session) {
        String sessionId = session.getId();
        gameService.movePlayer(sessionId, deltaX, deltaY);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/restart")
    public ResponseEntity<GameState> restartGame(HttpSession session) {
        String sessionId = session.getId();
        GameState gameState = gameService.restartGame(sessionId);
        return ResponseEntity.ok(gameState);
    }
}
