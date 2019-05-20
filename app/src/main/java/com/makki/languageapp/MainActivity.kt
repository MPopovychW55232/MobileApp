package com.makki.languageapp

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.makki.languageapp.fragments.auth.WelcomeFragment
import com.makki.languageapp.lib.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, WelcomeFragment.newInstance())
            .commit()

//        val background = container?.background
//        if (background is AnimationDrawable) {
//            background.setEnterFadeDuration(600)
//            background.setExitFadeDuration(3000)
//            background.start()
//        }
    }
}
