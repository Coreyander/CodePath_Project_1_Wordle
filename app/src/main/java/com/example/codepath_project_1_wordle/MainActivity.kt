package com.example.codepath_project_1_wordle

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.codepath_project_1_wordle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var wordList = FourLetterWordList.getAllFourLetterWords()
        var wordToGuess = FourLetterWordList.getRandomFourLetterWord()


        //DEBUG BEGIN
            //Shows Word to Guess through a TextView
        binding.answerTextView.text = getString(R.string.answer, wordToGuess)
        //DEBUG END

        binding.guessEditText.setOnClickListener{
                binding.guessEditText.selectAll()
        }

        var guessCounter = 0
        binding.btn.setOnClickListener {
            if (!binding.guessEditText.text.isEmpty()) {
                binding.instructions2.visibility = View.GONE
                binding.instructions.visibility = View.GONE
            }
            val guess = binding.guessEditText.text.toString().uppercase()
            binding.answerTextView.text = getString(R.string.answer, wordToGuess)
            //Toast.makeText(applicationContext, "Button Clicked", Toast.LENGTH_SHORT).show()
            if (!inputLengthErrorMessage(guess, it)) {
                Toast.makeText(
                    it.context,
                    "You Guessed " + binding.guessEditText.text.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                Toast.makeText( it.context,
                    "Guess Check Result: " + checkGuess(guess, wordToGuess),
                    Toast.LENGTH_SHORT
                ).show()
                guessCounter++
                when (guessCounter) {
                    1 -> {
                        binding.guessTitleTextView1.visibility = View.VISIBLE
                        binding.guessDisplayTextView1.visibility = View.VISIBLE
                        binding.guessDisplayTextView1.text = getString(R.string.guess_result, checkGuess(guess, wordToGuess), guess)
                    }
                    2 -> {
                        binding.guessTitleTextView2.visibility = View.VISIBLE
                        binding.guessDisplayTextView2.visibility = View.VISIBLE
                        binding.guessDisplayTextView2.text = getString(R.string.guess_result, checkGuess(guess, wordToGuess), guess)
                    }
                    3 -> {
                        binding.guessTitleTextView3.visibility = View.VISIBLE
                        binding.guessDisplayTextView3.visibility = View.VISIBLE
                        binding.guessDisplayTextView3.text = getString(R.string.guess_result, checkGuess(guess, wordToGuess), guess)
                        disableButton(binding.btn)
                        binding.resetBtn.visibility = View.VISIBLE
                        binding.answerTextView.visibility = View.VISIBLE
                    }
                }
                if(winCondition(wordToGuess, guess)) {
                    binding.winTextView.text = getString(R.string.win_text, wordToGuess)
                    binding.winTextView.visibility = View.VISIBLE
                    binding.answerTextView.visibility = View.GONE
                    disableButton(binding.btn)
                    binding.resetBtn.visibility = View.VISIBLE
                }
            }
            else {
                binding.guessEditText.text.clear()
            }
        }

        binding.resetBtn.setOnClickListener {
            wordToGuess = updateWordToGuess()
            enableButton(binding.btn)
            binding.answerTextView.text = getString(R.string.answer, wordToGuess)
            binding.resetBtn.visibility = View.GONE
            binding.answerTextView.visibility = View.GONE
            binding.winTextView.visibility = View.GONE
            binding.guessDisplayTextView1.text = ""
            binding.guessDisplayTextView2.text = ""
            binding.guessDisplayTextView3.text = ""
            binding.guessDisplayTextView1.visibility = View.GONE
            binding.guessDisplayTextView2.visibility = View.GONE
            binding.guessDisplayTextView3.visibility = View.GONE
            binding.guessTitleTextView1.visibility = View.GONE
            binding.guessTitleTextView2.visibility = View.GONE
            binding.guessTitleTextView3.visibility = View.GONE
            guessCounter = 0
        }
    }
}

/**
 * Parameters / Fields:
 *   wordToGuess : String - the target word the user is trying to guess
 *   guess : String - what the user entered as their guess
 *
 * Returns a String of 'O', '+', and 'X', where:
 *   'O' represents the right letter in the right place
 *   '+' represents the right letter in the wrong place
 *   'X' represents a letter not in the target word
 */


private fun checkGuess(guess: String, wordToGuess: String) : String {
    var result = ""
    for (i in 0..3) {
        if (guess[i] == wordToGuess[i]) {
            result += "O"
        }
        else if (guess[i] in wordToGuess) {
            result += "+"
        }
        else {
            result += "X"
        }
    }
    return result
}

private fun inputLengthErrorMessage(guess: String, view: View): Boolean {

    if (guess.length < 4) {
        Toast.makeText(view.context, "Guess was too short. Must be 4 letters in length" , Toast.LENGTH_SHORT).show()
        return true
    }
    else if (guess.length > 4) {
        Toast.makeText(view.context, "Guess was too long. Must be 4 letters in length", Toast.LENGTH_SHORT).show()
        return true
    }
    return false
}

private fun updateWordToGuess(): String {
    return FourLetterWordList.getRandomFourLetterWord()
}

private fun winCondition(wordToGuess: String, guess: String) : Boolean {
    return wordToGuess == guess
}

private fun disableButton(button: Button) {
    button.isEnabled = false
    button.setTextColor(Color.WHITE)
    button.setBackgroundColor(Color.GRAY)
}

private fun enableButton(button: Button) {
    button.isEnabled = true
    button.setTextColor(Color.BLACK)
    button.setBackgroundColor(Color.CYAN)
}
