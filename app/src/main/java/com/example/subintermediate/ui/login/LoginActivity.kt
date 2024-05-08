package com.example.subintermediate.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.subintermediate.R
import com.example.subintermediate.data.pref.UserModel
import com.example.subintermediate.databinding.ActivityLoginBinding
import com.example.subintermediate.main.MainActivity
import com.example.subintermediate.ui.ViewModelFactory
import com.example.subintermediate.ui.customview.MyButton
import com.example.subintermediate.ui.customview.MyEditText
import com.example.subintermediate.ui.customview.MyPassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var myEditText: MyEditText
    private lateinit var password: MyPassword
    private lateinit var button: MyButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myEditText = findViewById(R.id.emailEditText)
        password = findViewById(R.id.passwordEditText)
        button = findViewById(R.id.loginButton)

        myEditText.setButtonEnabler { setMyButtonEnable() }
        password.setButtonEnabler { setMyButtonEnable() }

        setMyButtonEnable()

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            binding.progressBar.visibility = View.VISIBLE
            viewModel.login(email, password)
        }

        viewModel.loginResponse.observe(this) { response ->
            binding.progressBar.visibility = View.GONE

            Log.d("Login", "Response error: ${response.error}")

            if (response.error == false) {
                val user = UserModel(response.loginResult?.name ?: "", response.loginResult?.token ?: "")
                viewModel.saveSession(user)


                Log.d("Login", "Saved token: ${user.token}")

                lifecycleScope.launch {
                    delay(1000)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }

            } else {
                Toast.makeText(this, "Login gagal: ${response.message}", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setMyButtonEnable() {
        val email = myEditText.text
        val password = password.text
        button.isEnabled = email != null && email.toString().isNotEmpty() && password != null && password.toString().isNotEmpty()
        button.invalidate()
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}

