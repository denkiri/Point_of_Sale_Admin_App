package com.denkiri.pharmacy.ui.main
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.denkiri.pharmacy.ExpenseActivity
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.ViewPagerAdapter
import com.denkiri.pharmacy.ui.main.ui.main.DayExpenseReportFragment
import com.denkiri.pharmacy.ui.main.ui.main.DayIncomeReportFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_sales.*
class ExpenseIncomeActivity : AppCompatActivity() {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, ExpenseActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_income_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setupViewPager(viewpager)
        tabs.tabGravity = TabLayout.GRAVITY_FILL
        tabs.setupWithViewPager(viewpager)
        setupTabIcons()

    }
    private fun setupViewPager(viewPager: ViewPager) {


        val fragment = DayIncomeReportFragment()

        val fragment1 = DayExpenseReportFragment()



        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(fragment, "Income")
        adapter.addFragment(fragment1, "Expense")



        viewPager.offscreenPageLimit = 1
        viewPager.adapter = adapter
    }

    private fun setupTabIcons() {

        tabs.getTabAt(0)?.text = "Income"
        tabs.getTabAt(1)?.text = "Expense"



    }
}