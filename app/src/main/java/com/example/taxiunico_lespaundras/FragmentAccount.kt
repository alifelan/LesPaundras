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

/**
 * Fragment used to update user information
 */
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
        text_name.setText(model.user?.name)
        text_email.setText(model.user?.email)

        account_button_ok.setOnClickListener {
            ApiClient(activity?.applicationContext!!).updateUser(
                text_name.text.toString(),
                model.user?.email!!,
                text_password.text.toString(),
                text_payment.text.toString()
            ) { _, _, message ->
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}