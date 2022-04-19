package com.denkiri.pharmacy

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.room.Transaction
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.ui.reports.CustomerTransaction
import com.denkiri.pharmacy.ui.reports.ExpiredProducts
import com.denkiri.pharmacy.ui.reports.Transactions
import kotlinx.android.synthetic.main.activity_sales.*

class CustomerActivity : BaseActivity() {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CustomerTransaction.newInstance())
                .commitNow()
        }
    }
    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container)
        if (f is CustomerTransaction) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val homeFragment = CustomerTransaction.newInstance()
            openFragment(homeFragment)
        }
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}