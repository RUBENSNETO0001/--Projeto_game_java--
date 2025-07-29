class GodGame {
  constructor() {
    this.canvas = document.getElementById("gameCanvas")
    this.ctx = this.canvas.getContext("2d")
    this.gameState = null
    this.images = {}
    this.keys = {}
    this.gameLoop = null

    this.init()
  }

  async init() {
    await this.loadImages()
    this.setupEventListeners()
    this.showHomeScreen()
  }

  async loadImages() {
    const imageUrls = {
      player: "res/persona/posseprincipal.png",
      playerEffect: "res/persona/posse_pegandoaAlma.png",
      soul: "res/almas/alma.png",
      comet: "res/cometas/cometa.png",
      background: "res/background/background.jpeg",
    }

    for (const [key, url] of Object.entries(imageUrls)) {
      try {
        this.images[key] = await this.loadImage(url)
      } catch (error) {
        console.warn(`Failed to load image ${url}, using fallback`)
        this.images[key] = this.createFallbackImage(key)
      }
    }
  }

  loadImage(src) {
    return new Promise((resolve, reject) => {
      const img = new Image()
      img.onload = () => resolve(img)
      img.onerror = reject
      img.src = src
    })
  }

  createFallbackImage(type) {
    const canvas = document.createElement("canvas")
    const ctx = canvas.getContext("2d")
    canvas.width = 50
    canvas.height = 50

    switch (type) {
      case "player":
        ctx.fillStyle = "#4682b4"
        break
      case "soul":
        ctx.fillStyle = "#ffd700"
        break
      case "comet":
        ctx.fillStyle = "#ff4500"
        break
      default:
        ctx.fillStyle = "#666"
    }

    ctx.fillRect(0, 0, 50, 50)
    return canvas
  }

  setupEventListeners() {
    // Home screen buttons
    document.getElementById("startBtn").addEventListener("click", () => this.startGame())
    document.getElementById("videoBtn").addEventListener("click", () => this.showVideo())
    document.getElementById("exitBtn").addEventListener("click", () => this.exitGame())

    // Game controls
    document.getElementById("saveBtn").addEventListener("click", () => this.saveGame())
    document.getElementById("loadBtn").addEventListener("click", () => this.loadGame())
    document.getElementById("homeBtn").addEventListener("click", () => this.showHomeScreen())

    // Video modal
    document.querySelector(".close").addEventListener("click", () => this.closeVideo())

    // Keyboard controls
    document.addEventListener("keydown", (e) => {
      this.keys[e.code] = true
      e.preventDefault()
    })

    document.addEventListener("keyup", (e) => {
      this.keys[e.code] = false
      e.preventDefault()
    })

    // Focus canvas for keyboard input
    this.canvas.addEventListener("click", () => this.canvas.focus())
    this.canvas.tabIndex = 1
  }

  showHomeScreen() {
    document.getElementById("homeScreen").classList.add("active")
    document.getElementById("gameScreen").classList.remove("active")
    if (this.gameLoop) {
      clearInterval(this.gameLoop)
      this.gameLoop = null
    }
  }

  showGameScreen() {
    document.getElementById("homeScreen").classList.remove("active")
    document.getElementById("gameScreen").classList.add("active")
    this.canvas.focus()
  }

  showVideo() {
    document.getElementById("videoModal").style.display = "block"
    document.getElementById("introVideo").play()
  }

  closeVideo() {
    document.getElementById("videoModal").style.display = "none"
    document.getElementById("introVideo").pause()
  }

  exitGame() {
    if (confirm("Tem certeza que deseja sair?")) {
      window.close()
    }
  }

  async startGame() {
    try {
      const response = await fetch("/api/game/start", { method: "POST" })
      this.gameState = await response.json()
      this.showGameScreen()
      this.startGameLoop()
    } catch (error) {
      console.error("Error starting game:", error)
      alert("Erro ao iniciar o jogo. Tente novamente.")
    }
  }

  async restartGame() {
    try {
      const response = await fetch("/api/game/restart", { method: "POST" })
      this.gameState = await response.json()
      this.startGameLoop()
    } catch (error) {
      console.error("Error restarting game:", error)
    }
  }

  startGameLoop() {
    if (this.gameLoop) clearInterval(this.gameLoop)

    this.gameLoop = setInterval(async () => {
      await this.updateGame()
      this.handleInput()
      this.render()
    }, 16) // ~60 FPS
  }

  async updateGame() {
    try {
      const response = await fetch("/api/game/state")
      this.gameState = await response.json()

      if (!this.gameState.gameActive) {
        clearInterval(this.gameLoop)
        this.gameLoop = null

        if (this.gameState.gameWon) {
          this.showVictoryMessage()
        } else {
          this.showGameOverMessage()
        }
      }

      this.updateUI()
    } catch (error) {
      console.error("Error updating game:", error)
    }
  }

  async handleInput() {
    if (!this.gameState || !this.gameState.gameActive) return

    let deltaX = 0
    let deltaY = 0
    const speed = 7

    if (this.keys["ArrowLeft"]) deltaX -= speed
    if (this.keys["ArrowRight"]) deltaX += speed
    if (this.keys["ArrowUp"]) deltaY -= speed
    if (this.keys["ArrowDown"]) deltaY += speed

    if (deltaX !== 0 || deltaY !== 0) {
      try {
        await fetch(`/api/game/move?deltaX=${deltaX}&deltaY=${deltaY}`, { method: "POST" })
      } catch (error) {
        console.error("Error moving player:", error)
      }
    }
  }

  render() {
    if (!this.gameState) return

    this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height)

    // Draw background
    if (this.images.background) {
      this.ctx.drawImage(this.images.background, 0, 0, this.canvas.width, this.canvas.height)
    }

    // Draw souls
    this.gameState.souls.forEach((soul) => {
      if (!soul.collected && this.images.soul) {
        this.ctx.drawImage(this.images.soul, soul.x, soul.y, 30, 30)
      }
    })

    // Draw comets
    this.gameState.comets.forEach((comet) => {
      if (comet.active && this.images.comet) {
        this.ctx.drawImage(this.images.comet, comet.x, comet.y, 60, 60)
      }
    })

    // Draw player
    if (this.gameState.player.alive && this.images.player) {
      this.ctx.drawImage(this.images.player, this.gameState.player.x, this.gameState.player.y, 50, 50)

      // Draw player name
      this.ctx.fillStyle = "white"
      this.ctx.font = "14px Arial"
      this.ctx.fillText(this.gameState.player.name, this.gameState.player.x, this.gameState.player.y - 5)
    }
  }

  updateUI() {
    if (!this.gameState) return

    const collectedSouls = this.gameState.souls.filter((soul) => soul.collected).length
    document.getElementById("soulsCount").textContent = `Almas: ${collectedSouls}/5`
    document.getElementById("playerName").textContent = this.gameState.player.name
  }

  showVictoryMessage() {
    const message = document.createElement("div")
    message.className = "game-message"
    message.innerHTML = `
            <h2>🎉 Parabéns! 🎉</h2>
            <p>Você coletou todas as almas!</p>
            <p>Você venceu o jogo!</p>
            <button onclick="game.restartGame()">Jogar Novamente</button>
            <button onclick="game.showHomeScreen()">Menu Principal</button>
        `
    document.body.appendChild(message)

    setTimeout(() => {
      if (message.parentNode) {
        message.parentNode.removeChild(message)
      }
    }, 5000)
  }

  showGameOverMessage() {
    const message = document.createElement("div")
    message.className = "game-message"
    message.innerHTML = `
            <h2>💀 Game Over! 💀</h2>
            <p>O cometa te matou!</p>
            <p>Deseja jogar novamente?</p>
            <button onclick="game.restartGame()">Jogar Novamente</button>
            <button onclick="game.showHomeScreen()">Menu Principal</button>
        `
    document.body.appendChild(message)

    setTimeout(() => {
      if (message.parentNode) {
        message.parentNode.removeChild(message)
      }
    }, 5000)
  }

  saveGame() {
    const gameData = JSON.stringify(this.gameState)
    localStorage.setItem("godGameSave", gameData)
    alert("Jogo salvo com sucesso!")
  }

  loadGame() {
    const savedData = localStorage.getItem("godGameSave")
    if (savedData) {
      this.gameState = JSON.parse(savedData)
      alert("Jogo carregado com sucesso!")
      this.startGameLoop()
    } else {
      alert("Nenhum jogo salvo encontrado!")
    }
  }
}

// Initialize game when page loads
let game
window.addEventListener("load", () => {
  game = new GodGame()
})

// Handle window close
window.addEventListener("beforeunload", (e) => {
  if (game && game.gameState && game.gameState.gameActive) {
    e.preventDefault()
    e.returnValue = ""
  }
})
