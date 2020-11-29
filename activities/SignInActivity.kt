package com.relapps.trelloclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.relapps.trelloclone.R
import com.relapps.trelloclone.activities.MainActivity.Companion.googleAccountUser
import com.relapps.trelloclone.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {

    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        ivSignInBackArrow.setOnClickListener {
            onBackPressed()
        }

        btnSignIn.setOnClickListener {
            signInRegisteredUser()
        }

        sign_in_with_google_button.setOnClickListener {
            signInWithGoogle()
        }
    }


    private fun signInWithGoogle() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            notifyUser("Login Successful")
            startActivity(Intent(this, MainActivity::class.java).putExtra(googleAccountUser, account))
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google register", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun signInRegisteredUser()
    {

        val email = etSignInEmail.text.toString()
        val password = etSignInPassword.text.toString()

        if (validateForm(email, password))
        {
            showProgressDialog("Please wait")


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign in", "signInWithEmail:success")
                            val user = FirebaseAuth.getInstance().currentUser
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

    fun signInSuccess(loggedInUser: User) {
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
}