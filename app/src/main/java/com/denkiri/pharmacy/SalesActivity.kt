package com.denkiri.pharmacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.denkiri.pharmacy.adapters.ViewPagerAdapter
import com.denkiri.pharmacy.ui.reports.CashSaleReport
import com.denkiri.pharmacy.ui.reports.CreditSaleReport
import com.denkiri.pharmacy.ui.reports.MpesaSaleReport
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_sales.*

class SalesActivity :BaseActivity(){
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
        setContentView(R.layout.activity_sales)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setupViewPager(viewpager)
        tabs.tabGravity = TabLayout.GRAVITY_FILL
        tabs.setupWithViewPager(viewpager)
        setupTabIcons()
    }
    private fun setupViewPager(viewPager: ViewPager) {


        val fragment = CashSaleReport()

        val fragment1 = MpesaSaleReport()

        val fragment2 = CreditSaleReport()


        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(fragment, "Cash")
        adapter.addFragment(fragment1, "Mpesa")
        adapter.addFragment(fragment2, "Credit")


        viewPager.offscreenPageLimit = 1
        viewPager.adapter = adapter
    }

    private fun setupTabIcons() {

        tabs.getTabAt(0)?.text = "Cash"
        tabs.getTabAt(1)?.text = "Mpesa"
        tabs.getTabAt(2)?.text = "Credit"


    }
}