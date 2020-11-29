package com.relapps.trelloclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.NavUtils
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.relapps.trelloclone.R
import com.relapps.trelloclone.firebase.FirestoreClass
import com.relapps.trelloclone.models.User
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object
    {
        const val googleAccountUser: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (googleAccountUser.isNotEmpty())
        {
            updateDrawerWithGoogleAccount(googleAccountUser as GoogleSignInAccount)
        }

        FirestoreClass().signInUser(this)


        setupActionBar()

        navView.setNavigationItemSelectedListener(this)
    }

    private fun updateDrawerWithGoogleAccount(googleAccountUser: GoogleSignInAccount) {
        Glide.with(this).load(googleAccountUser.photoUrl).into(ivUserIcon)

        tvUsername.text = "dupajasiu"

    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_main_acivity)
        toolbar_main_acivity.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        toolbar_main_acivity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }



    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.itemMyProfile -> startActivity(Intent(this, ProfileActivity::class.java))

            R.id.itemLogout ->
            {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, IntroActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }

            R.id.itemAllProjects -> notifyUser("Nothing here yet")
            R.id.itemStartNewProject -> notifyUser("Nothing here yet")
        }
       return true
    }

    override fun updateNavigationUserDetails(user: User)
    {
        Glide
                .with(this)
                .load(user.image)
                .circleCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(ivUserIcon)

        tvUsername.text = user.name

    }


}