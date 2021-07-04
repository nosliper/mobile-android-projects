package work.avila.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import work.avila.tictactoe.databinding.GameActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var newGame: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        newGame = findViewById(R.id.newGameButton)
        newGame.setOnClickListener {
            val intent = Intent(MainActivity@this, GameActivity::class.java)
            startActivity(intent)
        }

    }
}