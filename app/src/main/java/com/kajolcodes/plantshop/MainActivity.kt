package com.kajolcodes.plantshop

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var menu: ImageView
    private lateinit var logout: ImageView
    private var currentFragment = "plantshop"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        setupMenuClick()
        setupLogout()

        // Load default fragment only once
        if (savedInstanceState == null) {
            replaceFragment(PlantOverview())
        }
    }

    private fun initUI() {
        logout = findViewById(R.id.logout)
        menu = findViewById(R.id.menu)
    }

    private fun setupMenuClick() {
        menu.setOnClickListener {
            if (currentFragment == "plantshop") {
                menu.setImageResource(R.drawable.homee)
                currentFragment = "cart"
                replaceFragment(Cart())
            } else {
                menu.setImageResource(R.drawable.cartttt)
                currentFragment = "plantshop"
                replaceFragment(PlantOverview())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null) // Enables back navigation
            .commit()
    }

    private fun setupLogout() {
        logout.setOnClickListener { logout() }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
