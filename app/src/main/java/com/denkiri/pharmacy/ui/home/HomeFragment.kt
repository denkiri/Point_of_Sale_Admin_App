package com.denkiri.pharmacy.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.BaseActivity
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.Splashscreen
import com.denkiri.pharmacy.models.brand.Brand
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.dashboard.DashboardData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.ui.brand.BrandFragment
import com.denkiri.pharmacy.ui.categories.CategoriesFragment
import com.denkiri.pharmacy.ui.creditDue.CreditDueFragment
import com.denkiri.pharmacy.ui.customers.CustomerFragment
import com.denkiri.pharmacy.ui.expiry.ProductExpiryFragment
import com.denkiri.pharmacy.ui.products.ProductsFragment
import com.denkiri.pharmacy.ui.reorder.ReorderFragment
import com.denkiri.pharmacy.ui.supplier.SupplierFragment
import com.denkiri.pharmacy.utils.ConnectivityReceiver
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.NumberFormat
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private fun setStatus(data: Resource<DashboardData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
          //  activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
          //  activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
              //  empty_text.text = data.message
             //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    companion object {
        fun newInstance() = HomeFragment()
    }


    fun init() {

        if (NetworkUtils.isConnected(requireContext())) {
            homeViewModel.getDashboard()
            homeViewModel.getSalesReport()
        } //else {
          // val snack = view?.let { Snackbar.make(it,"Oops! Your Internet is off", Snackbar.LENGTH_INDEFINITE) }
          //  snack?.setAction("TURN ON") {
               //  executed when DISMISS is clicked
              //  startActivity(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS))
         // }
          //snack?.show()

     //  }
        }




    @SuppressLint("SetTextI18n")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)



        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        init()
        observers()
        setUpUi()
           homeViewModel.time.observe(viewLifecycleOwner, Observer {
            //  todaySalesDate.text = it
            //  todayCostDate.text = it
            //  todayProfitDate.text = it
            // reorderDate.text = it
        })
        homeViewModel.year.observe(viewLifecycleOwner, Observer {
            annualSalesHeader.text = "$it sales"
            //   yearCostText.text = "Total cost Year:$it"
            //  yearProfitText.text = "Total Profit Year: $it"

        })
        homeViewModel.month.observe(viewLifecycleOwner, Observer {
            monthlySalesHeader.text = "$it sales"
            //monthlyCostText.text = "Total cost: $it"
            //  monthlyProfitText.text = "Total Profit: $it"

        })
         signupback.setOnClickListener {
             val builder = AlertDialog.Builder(context)
             builder.setTitle("Exit")
             builder.setMessage("Are You Sure?")

             builder.setPositiveButton("Yes") { dialog, which ->
                 dialog.dismiss()
                 PreferenceManager(requireContext()).setLoginStatus(0)
                 val mDatabase = PharmacyDatabase.getDatabase(requireContext())
                 mDatabase?.clearAllTables()
                 activity?.finish()
                 startActivity(Intent(context, Splashscreen::class.java))
             }

             builder.setNegativeButton("No", { dialog, which -> dialog.dismiss() })
             val alert = builder.create()
             alert.show()

        }
        card_viewE.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, CreditDueFragment())
            .commitNow()  }
         card_viewD.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, ReorderFragment())
            .commitNow()  }
        card_viewF.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, ProductExpiryFragment())
            .commitNow()  }
        suppliersLink.setOnClickListener {   requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, SupplierFragment())
                .commitNow() }
        suppliers.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, SupplierFragment())
                .commitNow() }
        categoriesLink.setOnClickListener {   requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, CategoriesFragment())
                .commitNow() }
        categories.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, CategoriesFragment())
                .commitNow() }
        productsLink.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ProductsFragment())
                    .commitNow()
        }
        products.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductsFragment())
                .commitNow()  }

        customersLink.setOnClickListener {   requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, CustomerFragment())
                .commitNow() }
        customers.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, CustomerFragment())
                .commitNow() }
           brand.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, BrandFragment())
                .commitNow() }

        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
          //  observers()
         //   setUpUi()
            itemsswipetorefresh.isRefreshing = false
        }
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel("https://xindadisplay.com/wp-content/uploads/2018/08/modern-retail-pharmacy-interior-design-007-5.jpg", true))
        imageList.add(SlideModel("https://images-na.ssl-images-amazon.com/images/I/71nYANViDbL._AC_SL1500_.jpg", false))
        imageList.add(SlideModel("https://img1.exportersindia.com/product_images/bc-full/dir_111/3321300/pharmaceutical-tablets-1502129.jpg", false))
        imageList.add(SlideModel("https://images-na.ssl-images-amazon.com/images/I/617tQ-70gZL._AC_SL1440_.jpg",false))
       // image_slider.setImageList(imageList)
        val c = Calendar.getInstance()
        val timeOfDay = c[Calendar.HOUR_OF_DAY]

        if (timeOfDay >= 0 && timeOfDay < 12) {
              greeting!!.text=getString(R.string.morning_text)
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
               greeting!!.text=getString(R.string.afternoon_text)
        } else if (timeOfDay >= 16 && timeOfDay < 24) {

            greeting!!.text = getString(R.string.evening_text)
        }



    }
    fun observers(){
        homeViewModel.observeDashboard().observe(viewLifecycleOwner, { data ->
            run {
                setStatus(data)
                if (data.status == Status.SUCCESS && data.data != null) {
                    reorder.text = homeViewModel.getReOrder().toString()
                    creditDue.text = homeViewModel.getCreditDue().toString()
                    expiry.text = homeViewModel.getExpiry().toString()
                }
            }
        })
        homeViewModel.getTotalSales().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                overallSales.text =format.format(it.totalsales).toString()

            } catch (e: Exception) {
            }
        })
        homeViewModel.getTotalCost().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
              //  cost.text =format.format(it.totalcost).toString()

            } catch (e: Exception) {
            }
        })
        homeViewModel.getTotalProfit().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                if(it.totalprofit!=0.0){
                    salesProfit.text =format.format(it.totalprofit).toString()}

            } catch (e: Exception) {
            }
        })
        homeViewModel.getTotalSalesBalance().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                salesBalance.text =format.format(it.balance).toString()

            } catch (e: Exception) {
            }
        })
    }
    fun setUpUi() {
        homeViewModel.getOuthProfile().observe(viewLifecycleOwner, {
            try {
                name.setText(it.name)
            } catch (e: Exception) {
            }
        })
        homeViewModel.getYearSale().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                yearSales.text = format.format(it.yearlySales).toString()

            } catch (e: Exception) {
            }
        })
        homeViewModel.getYearCost().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                //   yearCost.text =format.format(it.yearlyCost).toString()

            } catch (e: Exception) {
            }
        })
        homeViewModel.getYearProfit().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                yearProfit.text = format.format(it.yearlyProfit).toString()

            } catch (e: Exception) {
            }
        })
       homeViewModel.getMonthProfit().observe(viewLifecycleOwner, {
           try {
               val format: NumberFormat = NumberFormat.getCurrencyInstance()
               format.setCurrency(Currency.getInstance("KSh"))
               monthProfit.text = format.format(it.monthlyProfit).toString()

           } catch (e: Exception) {
           }
       })
        homeViewModel.getMonthSale().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                monthSales.text = format.format(it.monthlySales).toString()

            } catch (e: Exception) {
            }
        })
        homeViewModel.getMonthCost().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                //   monthCost.text =format.format(it.monthlyCost).toString()

            } catch (e: Exception) {
            }
        })
       homeViewModel.getDayProfit().observe(viewLifecycleOwner, {
           try {
               val format: NumberFormat = NumberFormat.getCurrencyInstance()
               format.setCurrency(Currency.getInstance("KSh"))
               todayProfit.text = format.format(it.dayprofit).toString()

           } catch (e: Exception) {
           }
       })
       homeViewModel.getDaySale().observe(viewLifecycleOwner, {
           try {
               val format: NumberFormat = NumberFormat.getCurrencyInstance()
               format.setCurrency(Currency.getInstance("KSh"))
               todaySales.text = format.format(it.daySales).toString()

           } catch (e: Exception) {
           }
       })
        homeViewModel.getDayCost().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                //  todayCost.text =format.format(it.dayCost).toString()

            } catch (e: Exception) {
            }
        })
        homeViewModel.getYearVat().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                vatValue.text = format.format(it.vat).toString()

            } catch (e: Exception) {
            }
        })
    }

    override fun onResume() {
        super.onResume()


    }


}