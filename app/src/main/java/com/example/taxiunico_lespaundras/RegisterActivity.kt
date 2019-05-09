package com.example.taxiunico_lespaundras

import ApiUtility.ApiClient
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.email
import kotlinx.android.synthetic.main.activity_register.email_register_button
import kotlinx.android.synthetic.main.activity_register.login_form
import kotlinx.android.synthetic.main.activity_register.login_progress
import kotlinx.android.synthetic.main.activity_register.password
import java.util.*

/**
 * Activity in charge of registering an user with no previous account
 */
class RegisterActivity : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sharedPref = this.getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE)

        email_register_button.setOnClickListener {
            showProgress(true)
            if (parametersAreValid()) {
                ApiClient(applicationContext).createUser(
                    name.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    card.text.toString()
                ) { _, success, message ->
                    showProgress(false)
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                    if (success) {
                        val date = Date(System.currentTimeMillis())
                        with(sharedPref.edit()) {
                            putString(getString(R.string.email_key), email.text.toString())
                            putString(getString(R.string.password_key), password.text.toString())
                            putLong(getString(R.string.date_key), date.time)
                            apply()
                        }
                        startNavbarActivity(email.text.toString())
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.form_error), Toast.LENGTH_SHORT).show()
                showProgress(false)
            }
        }
    }

    private fun startNavbarActivity(email: String) {
        val intent = Intent(this, NavbarActivity::class.java)
        intent.putExtra(LoginActivity.EMAIL, email)
        startActivity(intent)
    }

    private fun parametersAreValid(): Boolean {
        return !(name.text?.isEmpty() ?: false || email.text.isEmpty() || password.text.isEmpty() || card.text?.isEmpty() ?: false) && email.text.contains(
            "@"
        ) && card.text?.length != 16
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }
}
