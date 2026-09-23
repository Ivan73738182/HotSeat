package com.ivan.hotseat

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ivan.hotseat.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var currentIndex = 0
    private var earned = 0
    private lateinit var answerButtons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerButtons = listOf(binding.btnA, binding.btnB, binding.btnC, binding.btnD)

        answerButtons.forEachIndexed { index, button ->
            button.setOnClickListener { onAnswerSelected(index) }
        }

        binding.btnTakeMoney.setOnClickListener { finishGame(earned) }
        binding.btn5050.setOnClickListener { use5050() }

        showQuestion()
    }

    private fun showQuestion() {
        val q = QuestionRepository.questions[currentIndex]

        binding.tvQuestion.text = q.text
        binding.tvProgress.text = "Вопрос ${currentIndex + 1} / 15"
        binding.tvPrize.text = "${formatMoney(QuestionRepository.prizeLadder[currentIndex])} ₽"

        val letters = listOf("A", "B", "C", "D")
        answerButtons.forEachIndexed { i, btn ->
            btn.text = "${letters[i]}: ${q.answers[i]}"
            btn.isEnabled = true
            btn.setBackgroundColor(Color.parseColor("#1A237E"))
        }
    }

    private fun onAnswerSelected(index: Int) {
        val q = QuestionRepository.questions[currentIndex]
        val selected = answerButtons[index]

        if (index == q.correctIndex) {
            selected.setBackgroundColor(Color.parseColor("#2E7D32"))
            earned = QuestionRepository.prizeLadder[currentIndex]
            answerButtons.forEach { it.isEnabled = false }

            binding.root.postDelayed({
                if (currentIndex == QuestionRepository.questions.size - 1) {
                    finishGame(QuestionRepository.prizeLadder.last())
                } else {
                    currentIndex++
                    showQuestion()
                }
            }, 1000)
        } else {
            selected.setBackgroundColor(Color.parseColor("#C62828"))
            answerButtons[q.correctIndex].setBackgroundColor(Color.parseColor("#2E7D32"))
            answerButtons.forEach { it.isEnabled = false }

            val safeEarned = getSafeEarned()
            binding.root.postDelayed({ finishGame(safeEarned) }, 1500)
        }
    }

    private fun getSafeEarned(): Int {
        var safe = 0
        for (i in 0 until currentIndex) {
            if (i in QuestionRepository.safeLevels) {
                safe = QuestionRepository.prizeLadder[i]
            }
        }
        return safe
    }

    private fun use5050() {
        val q = QuestionRepository.questions[currentIndex]
        var removed = 0
        answerButtons.forEachIndexed { i, btn ->
            if (i != q.correctIndex && removed < 2) {
                btn.isEnabled = false
                btn.text = "..."
                removed++
            }
        }
        binding.btn5050.isEnabled = false
    }

    private fun finishGame(amount: Int) {
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("earned", amount)
        intent.putExtra("won", amount == QuestionRepository.prizeLadder.last())
        startActivity(intent)
        finish()
    }

    private fun formatMoney(value: Int): String {
        return "%,d".format(value).replace(',', ' ')
    }
}
