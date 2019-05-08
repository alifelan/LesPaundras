package com.example.taxiunico_lespaundras

import ApiUtility.ApiClient
import ViewModels.UserViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class NavbarActivity : AppCompatActivity() {

    val fragmentManager = supportFragmentManager
    var email = ""

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                loadFragment(FragmentHome())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_trips -> {
                loadFragment(FragmentTrips())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                loadFragment(FragmentAccount())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add_trip -> {
                loadFragment(FragmentAddTrip())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun loadFragment(selectedFragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, selectedFragment)
        transaction.addToBackStack(null)  // enables back button with navbar items
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        if(intent.hasExtra(LoginActivity.EMAIL)) {
            val model = ViewModelProviders.of(this).get(UserViewModel::class.java)
            email = intent.extras!!.getString(LoginActivity.EMAIL)!!
            ApiClient(this).getUser(email){ user, success, message ->
                if(success) {
                    model.user = user
                } else {
                    Toast.makeText(this@NavbarActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // load home fragment first
        if(savedInstanceState == null)
            fragmentManager.beginTransaction().add(R.id.fragment_container, FragmentHome()).commit()
    }

    companion object {
        val TRIP : String = "TRIP"
        val FIRST: String = "FIRST"
        val USER: String = "USER"
    }
}
