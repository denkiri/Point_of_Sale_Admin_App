package com.denkiri.pharmacy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.denkiri.pharmacy.ui.accounts.AccountsFragment
import com.denkiri.pharmacy.ui.main.AboutFragment
import com.denkiri.pharmacy.ui.main.PrivacyFragment
import com.denkiri.pharmacy.ui.main.TermsFragment
import com.denkiri.pharmacy.ui.reports.CustomerTransaction
import kotlinx.android.synthetic.main.activity_sales.*

class AboutActivity : AppCompatActivity() {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                when (supportFragmentManager.findFragmentById(R.id.container)) {
                    is TermsFragment -> {
                        finish()
                        val intent = Intent(this, AboutActivity::class.java)
                        startActivity(intent)
                    }
                    is PrivacyFragment -> {
                        finish()
                        val intent = Intent(this, AboutActivity::class.java)
                        startActivity(intent)
                    }
                    else -> {
                     finish()
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, AboutFragment.newInstance())
                    .commitNow()
        }
    }
    override fun onBackPressed() {
    }


}