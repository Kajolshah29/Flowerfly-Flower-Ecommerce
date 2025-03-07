package com.kajolcodes.plantshop

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.snackbar.Snackbar
import com.kajolcodes.plantshop.R

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var mEmail: EditText
    private lateinit var mForgotPass: CardView
    private lateinit var mCancel: CardView
    private lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        initUI()
        forgotPasswordFunction()
    }

    private fun initUI() {
        mEmail = findViewById(R.id.forgotpasswordemail)
        mForgotPass = findViewById(R.id.forgotpasswordfunction)
        fAuth = FirebaseAuth.getInstance()
        mCancel = findViewById(R.id.forgotpasswordcancel)
    }

    private fun forgotPasswordFunction() {
        mForgotPass.setOnClickListener { view ->
            val email = mEmail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                mEmail.error = "Email is required"
                return@setOnClickListener
            }

            fAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Snackbar.make(view, "Sent email!", Snackbar.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Snackbar.make(view, e.message ?: "Error occurred", Snackbar.LENGTH_SHORT).show()
                }
        }

        mCancel.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
    }
}