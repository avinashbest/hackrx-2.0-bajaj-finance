/*
 *  *
 *  * Created by Avinash Kumar on 07/07/2021
 *  * https://github.com/avinashbest/
 *  * Copyright (c) 2021 . All rights reserved.
 *  *
 */

package com.aviza.finserv

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val auth = FirebaseAuth.getInstance()

        signup_button.setOnClickListener {
            val emailText = email_id.text.toString()
            val password = input_password.text.toString()
            val confirmPassword = confirm_password.text.toString()

//            if the input field are empty
            if (TextUtils.isEmpty(emailText) ||
                TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword)
            ) {
                Toast.makeText(
                    this,
                    "Credentials can't be empty!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
//            if password is less than 6 character
            if (password.length < 6) {
                Toast.makeText(
                    this,
                    "Password can't be less than 6 characters!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(
                    this,
                    "Password is not matching with confirm password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            signUpProgressBar.visibility = View.VISIBLE
//            creating user via firebase
            auth.createUserWithEmailAndPassword(emailText, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Account created successfully, Please Check Your Email",
                            Toast.LENGTH_LONG
                        ).show()
                        signUpProgressBar.visibility = View.GONE
                        /*auth.currentUser?.sendEmailVerification()*/
                        val intent = Intent(this, LogInActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            if (it.exception != null) {
                                "Something went wrong!"
                            } else {
                                it.exception?.message
                            },
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}