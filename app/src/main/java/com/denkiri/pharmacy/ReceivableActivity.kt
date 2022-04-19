package com.denkiri.pharmacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivable
import com.denkiri.pharmacy.ui.reports.ExpiredProducts
import com.denkiri.pharmacy.ui.reports.ReceivableReport
import kotlinx.android.synthetic.main.activity_sales.*

class ReceivableActivity : BaseActivity() {
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
        setContentView(R.layout.activity_receivable)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ReceivableReport.newInstance())
                .commitNow()
        }
    }
}