package com.elena.my_wardrobe

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.elena.my_wardrobe.ui.LoginActivity
import com.elena.mywardrobe.R
import com.elena.my_wardrobe.ui.dashboard.AddFragment
import com.elena.my_wardrobe.ui.home.HomeFragment
import com.elena.my_wardrobe.ui.notifications.FavoritesFragment
import com.elena.mywardrobe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupClickListener()
        loadFragment(HomeFragment())

        binding.logout.setOnClickListener{

            val sharedPreferences = getSharedPreferences("LoginCredentials", MODE_PRIVATE)
            val myEdit = sharedPreferences.edit()

            myEdit.putString("email", "")
            myEdit.putString("password", "")
            myEdit.apply()

            val intent: Intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.startActivity(intent)

        }
    }


    private fun setupClickListener() {
        binding.navView.setOnItemSelectedListener {

            val fragment = when (it.itemId) {
                R.id.navigation_home -> {
                    HomeFragment()
                }
                R.id.navigation_add -> {
                    AddFragment()
                }
                else -> {
                    if (GlobalData.isAnonymous){
                        Toast.makeText(binding.root.context, "Anonymous users cant save data", Toast.LENGTH_SHORT)
                        HomeFragment()
                    }else {
                        FavoritesFragment()
                    }
                }
            }
            loadFragment(fragment)
            true
        }
    }

    public fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, fragment)
            .commit()
    }

}