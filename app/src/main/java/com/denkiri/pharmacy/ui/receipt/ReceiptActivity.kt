package com.denkiri.pharmacy.ui.receipt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.ui.receipt.ui.mreceipt.MReceiptFragment
import kotlinx.android.synthetic.main.receipt_activity.*

class ReceiptActivity : AppCompatActivity() {
    var invoice: String?=null
    var paymentMode:String?=null
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
        setContentView(R.layout.receipt_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        invoice=intent.getStringExtra("id")
        paymentMode=intent.getStringExtra("paymentMode")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ReceiptFragment.newInstance())
                    .commitNow()
        }
    }
    override fun onBackPressed() {


    }
}