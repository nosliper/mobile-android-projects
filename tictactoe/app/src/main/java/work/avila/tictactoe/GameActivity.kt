package work.avila.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import work.avila.tictactoe.databinding.GameActivityBinding

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
        updateScore(game)

        newGameButton.setOnClickListener {
            updateScore(game)
            newGameButton.visibility = View.GONE
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
}