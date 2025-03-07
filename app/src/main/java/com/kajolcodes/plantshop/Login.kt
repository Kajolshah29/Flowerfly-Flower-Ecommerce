package com.kajolcodes.plantshop

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kajolcodes.plantshop.R

class LoginActivity : AppCompatActivity() {
    private lateinit var gotoregister: TextView
    private lateinit var forgotpassword: TextView
    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mLogin: CardView
    private lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initUI()
        loginFunction()
    }

    private fun initUI() {
        gotoregister = findViewById(R.id.gotosignup)
        mEmail = findViewById(R.id.emaillogin)
        mPassword = findViewById(R.id.passwordlogin)
        mLogin = findViewById(R.id.loginfunction)
        forgotpassword = findViewById(R.id.forgotpassword)
        fAuth = FirebaseAuth.getInstance()
    }

    private fun loginFunction() {
        forgotpassword.setOnClickListener {
            startActivity(Intent(applicationContext, ForgotPasswordActivity::class.java))
        }

        gotoregister.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
            finish()
        }

        mLogin.setOnClickListener { view ->
            val email = mEmail.text.toString().trim()
            val password = mPassword.text.toString().trim()

            when {
                TextUtils.isEmpty(email) -> {
                    mEmail.error = "Email is required"
                }
                TextUtils.isEmpty(password) -> {
                    mPassword.error = "Password is required"
                }
                password.length < 7 -> {
                    mPassword.error = "Password must be longer than 6 characters"
                }
                else -> {
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(view, "Login Successful!", Snackbar.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                        } else {
                            Snackbar.make(view, task.exception?.message ?: "Login Failed", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

