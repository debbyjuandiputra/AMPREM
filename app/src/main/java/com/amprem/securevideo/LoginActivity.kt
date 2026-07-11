package com.amprem.securevideo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.WindowManager
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var digitBoxes: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        // Cegah screenshot & screen recording di seluruh aplikasi
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Jika sesi masih berlaku (belum 15 menit), langsung masuk
        if (SessionManager.isSessionValid(this)) {
            goToMain()
            return
        }

        digitBoxes = listOf(
            findViewById(R.id.digit1),
            findViewById(R.id.digit2),
            findViewById(R.id.digit3),
            findViewById(R.id.digit4),
            findViewById(R.id.digit5),
            findViewById(R.id.digit6)
        )

        val errorText = findViewById<View>(R.id.errorText)
        val btnSubmit = findViewById<View>(R.id.btnSubmit)

        setupAutoAdvance()

        btnSubmit.setOnClickListener {
            val code = digitBoxes.joinToString("") { it.text.toString() }
            if (code.length != 6) {
                errorText.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (TotpUtil.verifyCode(code)) {
                errorText.visibility = View.INVISIBLE
                SessionManager.markLoggedIn(this)
                goToMain()
            } else {
                errorText.visibility = View.VISIBLE
                clearBoxes()
            }
        }
    }

    private fun setupAutoAdvance() {
        for (i in digitBoxes.indices) {
            val current = digitBoxes[i]

            current.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < digitBoxes.size - 1) {
                        digitBoxes[i + 1].requestFocus()
                    }
                }
            })

            current.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN
                    && current.text.isEmpty() && i > 0
                ) {
                    digitBoxes[i - 1].requestFocus()
                    digitBoxes[i - 1].text.clear()
                }
                false
            }
        }
        digitBoxes.first().requestFocus()
    }

    private fun clearBoxes() {
        digitBoxes.forEach { it.text.clear() }
        digitBoxes.first().requestFocus()
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
