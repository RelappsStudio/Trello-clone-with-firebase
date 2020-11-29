package com.relapps.trelloclone.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.relapps.trelloclone.R
import com.relapps.trelloclone.firebase.FirestoreClass
import com.relapps.trelloclone.models.User
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        ivRegisterBackArrow.setOnClickListener {
            onBackPressed()
        }

        btnRegisterConfirm.setOnClickListener {
            registerUser()
        }


    }



    fun userRegisteredSuccess()
    {
        hideProgressDialog()
        notifyUser("Succesfully registered")
        FirebaseAuth.getInstance().signOut()
        finish()
    }


    private fun registerUser()
    {
        val name: String = etRegisterName.text.toString().trim{ it <= ' '}
        val email: String = etRegisterEmail.text.toString().trim { it <= ' ' }
        val password: String = etRegisterPassword.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password))
        {
            showProgressDialog("Please wait")

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val firebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email
                    val user = User(firebaseUser.uid, name, registeredEmail!!)
                    FirestoreClass().registerUser(this, user)
                } else {
                    notifyUser("${task.exception!!.message}")
                }
            }
        }
    }

    private fun validateForm(name: String, email: String, password: String) : Boolean
    {
       return when
        {
            TextUtils.isEmpty(name) ->
            {
                showErrorSnackBar("Please enter username")
                false
            }

           TextUtils.isEmpty(email)->
           {
               showErrorSnackBar("Please enter email address")
               false
           }

           TextUtils.isEmpty(password) ->
           {
               showErrorSnackBar("Please enter username")
               false
           }
           else -> true
       }
    }
}