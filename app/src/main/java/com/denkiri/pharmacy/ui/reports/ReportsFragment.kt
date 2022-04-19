package com.denkiri.pharmacy.ui.reports

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.*
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.product.ProductReportData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.ui.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_reports.*
import java.text.NumberFormat
import java.util.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ReportsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportsFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private fun setStatus(data: Resource<ProductReportData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
           // activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
           // activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            homeViewModel.getProductReport()
        }
    }
    fun observers(){
        homeViewModel.observeProductReport().observe(viewLifecycleOwner, { data ->
            run {
                setStatus(data)
                if (data.status == Status.SUCCESS && data.data != null) {
                    totalValue.text=homeViewModel.getTotalProduct().toString()


                }
            }
        })
        homeViewModel.getEstimatedSales().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                salesValue.text =format.format(it.estimatedSales).toString()

            } catch (e: Exception) {
            }
        })
        homeViewModel.getEstimatedProfit().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                estimatedProfitValue.text =format.format(it.estimatedProfit).toString()

            } catch (e: Exception) {
            }
        })
        homeViewModel.getEstimatedSales().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                salesValue.text =format.format(it.estimatedSales).toString()

            } catch (e: Exception) {
            }
        })
        homeViewModel.getStockValue().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                stockValue.text =format.format(it.stockValue).toString()

            } catch (e: Exception) {
            }
        })
        homeViewModel.getItems().observe(viewLifecycleOwner, {
            try {

                itemsValue.text =it.itemsTotal.toString()

            } catch (e: Exception) {
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reports, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        init()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
         //   observers()
            itemsswipetorefresh.isRefreshing = false
        }
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
            card_viewSales.setOnClickListener {
                val intent = Intent(activity, SalesActivity::class.java)
                startActivity(intent)
            }
        card_viewDaySales.setOnClickListener {
            val intent = Intent(activity, DaySalesActivity::class.java)
            startActivity(intent)
        }
        card_viewInventory.setOnClickListener {
            val intent = Intent(activity, InventoryActivity::class.java)
            startActivity(intent)
        }
        card_viewExpiry.setOnClickListener {
            val intent = Intent(activity, ExpiryActivity::class.java)
            startActivity(intent)
        }
        card_viewReceivables.setOnClickListener {
            val intent = Intent(activity, ReceivableActivity::class.java)
            startActivity(intent)
        }
        card_viewCollection.setOnClickListener {
            val intent = Intent(activity, CollectionActivity::class.java)
            startActivity(intent)
        }
        card_viewCustomerTransaction.setOnClickListener {
            val intent = Intent(activity, CustomerActivity::class.java)
            startActivity(intent)
        }
        card_viewCharts.setOnClickListener {
          val intent = Intent(activity, ChartActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                ReportsFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}