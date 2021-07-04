package work.avila.tictactoe

import android.view.View
import android.widget.Button
import android.widget.TextView

class Game (squares: Array<TextView>, newGameButton: Button) {
    private var currentPlayer: Int = 1
    var scoreX: Int = 0
    var scoreO: Int = 0
    var hasGameEnded: Boolean = false
    private var gameState: Array<Int>
    private var newGame: Button
    lateinit var squares: Array<TextView>
    init {
        squares.forEachIndexed { index, square -> square.setOnClickListener { markSquare(square, index) } }
        gameState = Array(9) { 0 }
        newGame = newGameButton
    }

    private fun markSquare(square: TextView, index: Int) {
        if (square.text.isEmpty() && !hasGameEnded) {
            square.text = getCurrentMark()
            processMove(index)
            currentPlayer = 3 - currentPlayer
        }
    }

    private fun processMove(index: Int) {
        gameState[index] = currentPlayer
        if (checkForWinner()) {
            scoreX += if (currentPlayer == 1) 1 else 0
            scoreO += if (currentPlayer == 2) 1 else 0
        }
        hasGameEnded = checkForDraw() || checkForWinner()
        if (hasGameEnded) {
            newGame.visibility = View.VISIBLE
            resetGameState()
        }
    }

    private fun resetGameState() {
        for (i in gameState.indices) {
            gameState[i] = 0
        }
    }

    private fun checkForWinner(): Boolean {
        val row1 = arrayOf(gameState[0], gameState[1], gameState[2])
        val row2 = arrayOf(gameState[3], gameState[4], gameState[5])
        val row3 = arrayOf(gameState[6], gameState[7], gameState[8])
        val col1 = arrayOf(gameState[0], gameState[3], gameState[6])
        val col2 = arrayOf(gameState[1], gameState[4], gameState[7])
        val col3 = arrayOf(gameState[2], gameState[5], gameState[8])
        val diagonal1 = arrayOf(gameState[0], gameState[4], gameState[8])
        val diagonal2 = arrayOf(gameState[2], gameState[4], gameState[6])
        return arrayOf(row1, row2, row3, col1, col2, col3, diagonal1, diagonal2)
            .any { it.all { player -> player == currentPlayer } }
    }

    private fun checkForDraw(): Boolean {
        return gameState.all { it != 0 } && !checkForWinner()
    }

    private fun getCurrentMark(): String {
        return if (currentPlayer == 1) "X" else "O"
    }
}