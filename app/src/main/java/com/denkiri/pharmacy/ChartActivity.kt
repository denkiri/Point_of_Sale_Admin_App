package com.denkiri.pharmacy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.denkiri.pharmacy.ui.charts.*
import com.denkiri.pharmacy.ui.reports.CustomerTransaction
import kotlinx.android.synthetic.main.activity_sales.*

class ChartActivity : AppCompatActivity() {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                when (supportFragmentManager.findFragmentById(R.id.container)) {
                    is CategoryChartFragment -> {
                        val homeFragment = ChartsFragment.newInstance()
                        openFragment(homeFragment)

                    }
                    is LoseChartFragment -> {
                        val homeFragment = ChartsFragment.newInstance()
                        openFragment(homeFragment)

                    }
                    is MonthFragment -> {
                        val homeFragment = ChartsFragment.newInstance()
                        openFragment(homeFragment)

                    }
                    is PaymentModeFragment -> {
                        val homeFragment = ChartsFragment.newInstance()
                        openFragment(homeFragment)

                    }
                    is VatChartFragment -> {
                        val homeFragment = ChartsFragment.newInstance()
                        openFragment(homeFragment)

                    }
                    is YearlySalesFragment -> {
                        val homeFragment = ChartsFragment.newInstance()
                        openFragment(homeFragment)

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
        setContentView(R.layout.chart_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChartsFragment.newInstance())
                .commitNow()
        }
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    override fun onBackPressed() {



    }

}