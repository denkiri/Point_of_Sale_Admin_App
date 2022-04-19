package com.denkiri.pharmacy.ui.supplier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denkiri.pharmacy.R

class SupplierActivity : AppCompatActivity() {
    var supplierId: Int? = 0
    var supplierName: String?=null
    var supplierAddress:String?=null
    var contactPerson:String?=null
    var supplierContact:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.supplier_activity)
        supplierId = intent.getIntExtra("supplierId", 0)
        supplierName=intent.getStringExtra("supplierName")
        supplierAddress=intent.getStringExtra("supplierAddress")
        contactPerson=intent.getStringExtra("contactPerson")
        supplierContact=intent.getStringExtra("supplierContact")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EditSupplierFragment.newInstance())
                .commitNow()
        }
    }
}