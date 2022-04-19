package com.denkiri.pharmacy

import android.accounts.Account
import android.app.AlertDialog
import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem

import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ReportFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.denkiri.pharmacy.ui.accounts.AccountsFragment
import com.denkiri.pharmacy.ui.categories.CategoriesFragment
import com.denkiri.pharmacy.ui.customers.CustomerFragment.Companion.newInstance
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.ui.ledger.LedgerFragment
import com.denkiri.pharmacy.ui.notification.NotificationFragment
import com.denkiri.pharmacy.ui.profile.ProfileFragment
import com.denkiri.pharmacy.ui.profile.ProfileFragment.Companion.newInstance
import com.denkiri.pharmacy.ui.reports.ReportsFragment
import com.denkiri.pharmacy.ui.users.UsersFragment
import com.denkiri.pharmacy.utils.ConnectivityReceiver
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity:BaseActivity()   {
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home-> {
                val homeFragment = HomeFragment.newInstance()
                openFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_reports -> {
                val reportsFragment = ReportsFragment.newInstance()
                openFragment(reportsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ledger -> {
                val ledgerFragment = LedgerFragment.newInstance()
                openFragment(ledgerFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_accounts -> {
                val accountsFragment = AccountsFragment.newInstance()
                openFragment(accountsFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().replace(R.id.container,HomeFragment.newInstance()).commitNow()
       // val toolbar: Toolbar = findViewById(R.id.toolbar)
       // setSupportActionBar(toolbar)
     //   val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
       // val navView: NavigationView = findViewById(R.id.nav_view)
     //   val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
     /*appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_product, R.id.nav_category,R.id.nav_customer,R.id.nav_order,R.id.nav_orderForm,R.id.nav_supplier,R.id.nav_ledger,R.id.nav_users,R.id.nav_profile), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/

    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    } */

  /*  override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.action_logout) {
            finish()
            startActivity(Intent(this@MainActivity, Splashscreen::class.java))
            return true
        }



        return super.onOptionsItemSelected(item)

    }*/

   /* override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }*/

    internal fun popOutFragments() {
        val fragmentManager = supportFragmentManager
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack()
        }
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container)
        if (f is HomeFragment) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit")
            builder.setMessage("Are You Sure?")

            builder.setPositiveButton("Yes", { dialog, which ->
                dialog.dismiss()
                super.onBackPressed()
            })

            builder.setNegativeButton("No", { dialog, which -> dialog.dismiss() })
            val alert = builder.create()
            alert.show()            //additional code
        } else {
            val homeFragment = HomeFragment.newInstance()
            openFragment(homeFragment)
        }
    }


}