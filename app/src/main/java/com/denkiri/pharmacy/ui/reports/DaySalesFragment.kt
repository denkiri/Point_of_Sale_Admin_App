package com.denkiri.pharmacy.ui.reports

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.MainActivity
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.SalesAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.reports.salesReport.SalesReportData
import com.denkiri.pharmacy.ui.receipt.ReceiptActivity
import com.denkiri.pharmacy.ui.receipt.ReceiptFragment
import com.denkiri.pharmacy.utils.OnReportClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.credit_sale_report_fragment.*
import kotlinx.android.synthetic.main.fragment_day_sales.*
import kotlinx.android.synthetic.main.fragment_day_sales.avi
import kotlinx.android.synthetic.main.fragment_day_sales.empty_button
import kotlinx.android.synthetic.main.fragment_day_sales.empty_layout
import kotlinx.android.synthetic.main.fragment_day_sales.itemsswipetorefresh
import kotlinx.android.synthetic.main.fragment_day_sales.main
import kotlinx.android.synthetic.main.fragment_day_sales.productsTotal
import kotlinx.android.synthetic.main.fragment_day_sales.recyclerView
import kotlinx.android.synthetic.main.fragment_day_sales.searchView
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [DaySalesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DaySalesFragment : Fragment() {
    private lateinit var viewModel: DayReportViewModel
    var date:String?=null
    lateinit var salesAdapter: SalesAdapter
    private lateinit var sales: ArrayList<com.denkiri.pharmacy.models.reports.salesReport.SalesReport>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //SEARCH FILTER
                filter(charSequence.toString())
            }
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })

    }
    private fun initView(){
        sales= ArrayList()
        salesAdapter = context?.let {
            SalesAdapter(0,it, sales, object : OnReportClick {

                override fun selected(pos: Int) { requireActivity().supportFragmentManager.beginTransaction()
                      // .replace(R.id.container, ReceiptFragment())
                     //   .commitNow()
                  //  startActivity(Intent(context,ReceiptActivity::class.java))
                  //  activity?.finish()
                    val intent = Intent(activity, ReceiptActivity::class.java)
                    intent.putExtra("id", sales[pos].invoiceNumber )
                    intent.putExtra("paymentMode", sales[pos].type)
                    startActivity(intent)
                }

                override fun onClickListener(position1: Int) {
                 //  requireActivity().supportFragmentManager.beginTransaction()
                      // .replace(R.id.container, ReceiptFragment())
                     //  .commitNow()
                   // startActivity(Intent(context,ReceiptActivity::class.java))
                  //  activity?.finish()
                }


            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = salesAdapter
        salesAdapter.notifyDataSetChanged()
    }
    fun filter(text: String) {
        val filteredSalesAry: ArrayList<com.denkiri.pharmacy.models.reports.salesReport.SalesReport> = ArrayList()
        val salesAry : ArrayList<com.denkiri.pharmacy.models.reports.salesReport.SalesReport> = sales
        for (eachProduct in salesAry) {
            if (eachProduct.invoiceNumber!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredSalesAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        salesAdapter.filterList(filteredSalesAry)
        sales=filteredSalesAry

        salesAdapter.notifyDataSetChanged()
        if (salesAdapter.itemCount==0){

            recyclerView.visibility=View.VISIBLE
            Toasty.error(requireContext(),"No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            // noResults.visibility=View.INVISIBLE
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
    fun observers() {
        viewModel.observeSalesReport().observe(
            viewLifecycleOwner,
            {
                setStatus(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.salesreport != null && !it.data.salesreport!!.isEmpty()) {
                        setUpDaySales(it.data.salesreport as ArrayList<com.denkiri.pharmacy.models.reports.salesReport.SalesReport>)
                    } else {
                        setUpDaySales(ArrayList())
                    }
                }

            })
        viewModel.getTotalSales().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                sale.text =format.format(it.totalsales).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getTotalCost().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                cost.text =format.format(it.totalcost).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getTotalProfit().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                profit.text =format.format(it.totalprofit).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getTotalSalesBalance().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                balance.text =format.format(it.balance).toString()

            } catch (e: Exception) {
            }
        })

    }
    private fun setUpDaySales(sales: ArrayList<com.denkiri.pharmacy.models.reports.salesReport.SalesReport>) {
        this.sales = sales
        productsTotal.text= sales.size.toString()
        salesAdapter.refresh(this.sales)
        Handler().postDelayed({
        }, 1)
    }
    private fun setStatus(data: Resource<SalesReportData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
              //  empty_text.text = data.message
              //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener { viewModel.getDaySalesReport(date.toString()) }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_sales, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DaySalesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DaySalesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DayReportViewModel::class.java)
        val bundle = arguments
        date=bundle?.getString("date").toString()
      //  Toasty.error(requireContext(), bundle?.getString("date")?.trim().toString(), Toast.LENGTH_SHORT, true).show()
        viewModel.getDaySalesReport(bundle?.getString("date")?.trim().toString())
        initView()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
          //  initView()
            viewModel.getDaySalesReport(bundle?.getString("date")?.trim().toString())
        //    observers()
            itemsswipetorefresh.isRefreshing = false
        }
    }




}