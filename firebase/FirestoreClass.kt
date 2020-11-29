package com.relapps.trelloclone.firebase

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.relapps.trelloclone.activities.MainActivity
import com.relapps.trelloclone.activities.ProfileActivity
import com.relapps.trelloclone.activities.RegisterActivity
import com.relapps.trelloclone.activities.SignInActivity
import com.relapps.trelloclone.models.User
import com.relapps.trelloclone.utils.Constants

class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User)
    {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }


    fun signInUser(activity: Activity)
    {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId()).get().addOnSuccessListener {
                val loggedInUser = it.toObject(User::class.java)

                    when (activity)
                    {
                        is SignInActivity ->
                        {
                            activity.signInSuccess(loggedInUser!!)
                        }

                        is MainActivity ->
                        {
                            activity.updateNavigationUserDetails(loggedInUser!!)
                        }

                        is ProfileActivity ->
                        {
                            activity.updateNavigationUserDetails(loggedInUser!!)
                        }
                    }


            }
    }

    fun getCurrentUserId(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""

        if (currentUser != null)
        {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}