package com.relapps.trelloclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.relapps.trelloclone.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null)
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }



        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        ivSignInBackArrow.setOnClickListener {
            onBackPressed()
        }

        btnSignIn.setOnClickListener {
            signInRegisteredUser()
        }
    }

    private fun signInRegisteredUser()
    {

        val email = etSignInEmail.text.toString()
        val password = etSignInPassword.text.toString()

        if (validateForm(email, password))
        {
            showProgressDialog("Please wait")


            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign in", "signInWithEmail:success")
                            val user = auth.currentUser
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign in", "signInWithEmail:failure", task.exception)
                            notifyUser("Authentication failed.")

                            // ...
                        }

                        // ...
                    }
        }




    }



    private fun validateForm( email: String, password: String) : Boolean
    {
        return when
        {

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