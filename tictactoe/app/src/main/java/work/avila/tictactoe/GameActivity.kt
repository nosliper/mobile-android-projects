package work.avila.tictactoe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import work.avila.tictactoe.databinding.GameActivityBinding

const val GAME_STATE = "game_state"
const val CURRENT_PLAYER = "current_player"
const val HAS_GAME_ENDED = "has_game_ended"
const val SCORE_X = "score_x"
const val SCORE_O = "score_o"

class GameActivity : AppCompatActivity() {

    private lateinit var binding: GameActivityBinding
    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GameActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newGameButton = binding.finishGame
        val squares = arrayOf(binding.square1,
            binding.square2,
            binding.square3,
            binding.square4,
            binding.square5,
            binding.square6,
            binding.square7,
            binding.square8,
            binding.square9)

        game = Game(squares, newGameButton)

        if (savedInstanceState !== null) {
            game.currentPlayer = savedInstanceState.getInt(CURRENT_PLAYER)
            game.hasGameEnded = savedInstanceState.getBoolean(HAS_GAME_ENDED)
            game.scoreX = savedInstanceState.getInt(SCORE_X)
            game.scoreO = savedInstanceState.getInt(SCORE_O)
            game.gameState = savedInstanceState.getIntArray(GAME_STATE)?.toTypedArray() ?: game.gameState
            restoreMarksFromGameState(squares, game.gameState)
            newGameButton.visibility = if (game.hasGameEnded) View.VISIBLE else View.GONE
        }

        updateScore(game)

        newGameButton.setOnClickListener {
            updateScore(game)
            newGameButton.visibility = View.GONE
            game.resetGameState()
            clearSquares(squares)
            game.hasGameEnded = false
        }
    }

    private fun clearSquares(squares: Array<TextView>) {
        for (square in squares) {
            square.text = ""
        }
    }

    private fun updateScore(game: Game) {
        binding.scoreX.text = getString(R.string.scorex, game.scoreX)
        binding.scoreO.text = getString(R.string.scoreo, game.scoreO)
    }

    private fun restoreMarksFromGameState(squares: Array<TextView>, gameState: Array<Int>) {
        gameState.forEachIndexed() { i, _ ->
            if (gameState[i] == 1) {
                squares[i].text = "X"
            } else if (gameState[i] == 2) {
                squares[i].text = "O"
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("GameActivity", "Ran OnSaveInstanceState")
        outState.putInt(CURRENT_PLAYER, game.currentPlayer)
        outState.putBoolean(HAS_GAME_ENDED, game.hasGameEnded)
        outState.putInt(SCORE_X, game.scoreX)
        outState.putInt(SCORE_O, game.scoreO)
        outState.putIntArray(GAME_STATE, game.gameState.toIntArray())
    }
}