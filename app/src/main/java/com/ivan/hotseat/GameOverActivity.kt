package com.ivan.hotseat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivan.hotseat.databinding.ActivityGameOverBinding

class GameOverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val earned = intent.getIntExtra("earned", 0)
        val won = intent.getBooleanExtra("won", false)

        binding.tvResult.text = if (won) {
            "🏆 ПОБЕДА!\nВы выиграли ${formatMoney(earned)} ₽"
        } else {
            "Игра окончена\nВаш выигрыш: ${formatMoney(earned)} ₽"
        }

        binding.btnRestart.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
            finish()
        }

        binding.btnMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun formatMoney(value: Int): String {
        return "%,d".format(value).replace(',', ' ')
    }
}
