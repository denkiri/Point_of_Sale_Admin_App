package com.denkiri.pharmacy.ui.supplier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.ui.supplier.ui.main.InvoiceFragment
import kotlinx.android.synthetic.main.receipt_activity.*

class InvoiceActivity : AppCompatActivity() {
    var invoice: String?=null
    var id:String?=null
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
        setContentView(R.layout.invoice_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        invoice=intent.getStringExtra("id")
        id=intent.getStringExtra("supplier")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, InvoiceFragment.newInstance())
                    .commitNow()
        }
    }
    override fun onBackPressed() {


    }
}