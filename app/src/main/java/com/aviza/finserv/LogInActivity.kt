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
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        val auth = FirebaseAuth.getInstance()
//        if the current user if already signed in then starting the HomeActivity::class.javas
        if (auth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        create_account_button.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            val logInEmail = input_email.text.toString()
            val logInPassword = input_password.text.toString()
//            if the input field are empty
            if (TextUtils.isEmpty(logInEmail) || TextUtils.isEmpty(logInPassword)) {
                Toast.makeText(
                    this,
                    "Credentials can't be empty!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(logInEmail, logInPassword).addOnCompleteListener {
                if (auth.currentUser != null) {
                    progressBar.visibility = View.GONE
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        create_account_button.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}