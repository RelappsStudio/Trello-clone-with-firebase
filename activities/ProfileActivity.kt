package com.relapps.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.relapps.trelloclone.R
import com.relapps.trelloclone.firebase.FirestoreClass
import com.relapps.trelloclone.models.User
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.app_bar_main.*

class ProfileActivity : BaseActivity() {

    var changesSaved = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        FirestoreClass().signInUser(this)

        ivProfileBackArrow.setOnClickListener {
            onBackPressed()
        }

        ivProfileEdit.setOnClickListener {
            switchLayouts()
        }

        btnSaveProfileChanges.setOnClickListener {
            saveProfileChanges()
        }

    }

    private fun saveProfileChanges() {
        changesSaved = true
        switchLayouts()
        notifyUser("Changes saved.")

        //val currentUpdatedUser: User = User(FirebaseAuth.getInstance().uid.toString(),
        //        tvProfileUsernameEdit.text.toString(),
        //        tvProfileEmailEdit.text.toString(),
        //        ivProfileIconEdit.,
        //        tvProfilePhoneNumberEdit.text as Long,
        //        "")
        //
        //        FirebaseAuth.getInstance().updateCurrentUser()



    }

    private fun switchLayouts() {

        if (cvProfileInfo.visibility == View.VISIBLE)
        {
            changesSaved = false
            cvProfileInfo.visibility = View.GONE
            cvProfileEdit.visibility = View.VISIBLE
            btnSaveProfileChanges.visibility = View.VISIBLE
            FirestoreClass().signInUser(this)
        }
        else
        {
            if (!changesSaved)
            {
                showWarningDialog()
            }
            else
            {
                cvProfileInfo.visibility = View.VISIBLE
                cvProfileEdit.visibility = View.GONE
                btnSaveProfileChanges.visibility = View.GONE
                FirestoreClass().signInUser(this)
            }

        }


    }

    private fun showWarningDialog() {
        val alert = AlertDialog.Builder(this)

        alert.setTitle("Are you sure?")
        alert.setMessage("You want to go back without saving changes?")
        alert.setIcon(android.R.drawable.ic_dialog_alert)

        alert.setPositiveButton("Yes"){
            dialogInterface, which ->

            cvProfileInfo.visibility = View.VISIBLE
            cvProfileEdit.visibility = View.GONE
            btnSaveProfileChanges.visibility = View.GONE
            FirestoreClass().signInUser(this)

            dialogInterface.dismiss()
        }

        alert.setNegativeButton("No"){ dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog = alert.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun updateNavigationUserDetails(user: User) {

        if (cvProfileInfo.visibility == View.VISIBLE)
        {
            Glide.with(this)
                .load(user.image)
                .circleCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(ivProfileIcon)
        }

        tvProfileUsername.text = user.name
        tvProfileEmail.text = user.email
        tvProfilePhoneNumber.text = user.mobile.toString()

    }


}