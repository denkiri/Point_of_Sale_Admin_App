package com.denkiri.pharmacy
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.ui.auth.AuthActivity

class Splashscreen : AppCompatActivity() {
    private val time:Long=3000 // 3 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        Handler(Looper.getMainLooper()).postDelayed({

init()
        }, time)
    }
  fun  init() {
        if(PreferenceManager(this).getLoginStatus()==0) {
            startActivity(Intent(this@Splashscreen, AuthActivity::class.java))
            finish()
        }
        else{
            startActivity(Intent(this@Splashscreen,MainActivity::class.java))
            finish()
        }
    }
}