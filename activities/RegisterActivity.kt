package com.relapps.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.relapps.trelloclone.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        ivRegisterBackArrow.setOnClickListener {
            onBackPressed()
        }

        btnRegisterConfirm.setOnClickListener {
            registerUser()
        }
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
                hideProgressDialog()
                if (task.isSuccessful) {
                    val firebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email
                    notifyUser("Email $registeredEmail registered succesfully")
                    FirebaseAuth.getInstance().signOut()
                    finish()
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