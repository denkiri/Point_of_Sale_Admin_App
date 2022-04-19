package com.denkiri.pharmacy.ui.supplier

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.denkiri.pharmacy.MainActivity
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.ui.orderform.CartFragment
import com.denkiri.pharmacy.ui.supplier.ui.main.PurchaseOrderFragment
import kotlinx.android.synthetic.main.receipt_activity.*

class PurchaseOrderActivity : AppCompatActivity() {
    var supplierId: String?=null
    var supplierName:String?=null
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val f = supportFragmentManager.findFragmentById(R.id.container)
                if (f is CartFragment) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container, PurchaseOrderFragment.newInstance())
                            .commitNow()
                }
                else{

                    finish()

                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.purchase_order_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supplierId=intent.getStringExtra("supplierId")
        supplierName=intent.getStringExtra("supplierName")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PurchaseOrderFragment.newInstance())
                    .commitNow()
        }
    }
    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container)
        if (f is CartFragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PurchaseOrderFragment.newInstance())
                    .commitNow()
        }
        else{
            finish()

        }


    }
}