package com.relapps.trelloclone.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.relapps.trelloclone.R.*
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        //here I would change font in the title screen but I don't have the license to put whole ttf file inside the project
        //val typeFace = Typeface.createFromAsset(assets,"beware.ttf")
        //        tvSplashAppName.typeface = typeFace


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }, 1500)
    }
}