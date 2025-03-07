package com.kajolcodes.plantshop

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kajolcodes.plantshop.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mRegister: CardView
    private lateinit var gologin: TextView
    private lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initUI()
        registerFunction()
    }

    private fun initUI() {
        mEmail = findViewById(R.id.registeremail)
        mPassword = findViewById(R.id.registerpassword)
        mRegister = findViewById(R.id.registerevent)
        fAuth = FirebaseAuth.getInstance()
        gologin = findViewById(R.id.gologin)
    }

    private fun registerFunction() {
        if (fAuth.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        gologin.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        mRegister.setOnClickListener {
            val email = mEmail.text.toString().trim()
            val password = mPassword.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                mEmail.error = "Email is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.error = "Password is required"
                return@setOnClickListener
            }
            if (password.length < 7) {
                mPassword.error = "Password must be longer than 6 characters"
                return@setOnClickListener
            }

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Snackbar.make(mRegister, "Success!", Snackbar.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                } else {
                    Snackbar.make(mRegister, task.exception?.message ?: "Error", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}

