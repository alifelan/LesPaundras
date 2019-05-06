package com.example.taxiunico_lespaundras

import ApiUtility.ApiClient
import ApiUtility.User
import ViewModels.UserViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_account.*
import java.lang.Exception

class FragmentAccount : Fragment() {

    private lateinit var model: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        ApiClient(activity?.applicationContext!!).getUser(model.email) { user, success, message ->
            if (success) {
                text_name.setText(user?.name)
                text_email.setText(user?.email)
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }

        account_button_ok.setOnClickListener {
            ApiClient(activity?.applicationContext!!).updateUser(
                text_name.text.toString(),
                model.email,
                text_password.text.toString(),
                text_payment.text.toString()
            ) { _, _, message ->
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}