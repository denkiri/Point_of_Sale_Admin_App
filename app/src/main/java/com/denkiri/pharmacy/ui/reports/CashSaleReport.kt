package com.denkiri.pharmacy.ui.reports

import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.CashAdapter
import com.denkiri.pharmacy.adapters.SalesAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.reports.cashreport.CashReport
import com.denkiri.pharmacy.models.reports.cashreport.CashReportData
import com.denkiri.pharmacy.ui.receipt.ReceiptActivity
import com.denkiri.pharmacy.utils.OnReportClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.cash_sale_report_fragment.*


class CashSaleReport : Fragment() {
    lateinit var cashSalesAdapter: CashAdapter
    private lateinit var cashSales: ArrayList<CashReport>

    companion object {
        fun newInstance() = CashSaleReport()
    }

    private lateinit var viewModel: CashSaleReportViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    private fun initView(){
        cashSales= ArrayList()
        cashSalesAdapter = context?.let {
            CashAdapter(0,it, cashSales, object : OnReportClick {

                override fun selected(pos: Int) {
                    val intent = Intent(activity, ReceiptActivity::class.java)
                    intent.putExtra("id", cashSales[pos].invoiceNumber )
                    intent.putExtra("paymentMode", cashSales[pos].type)
                    startActivity(intent)

                }

                override fun onClickListener(position1: Int) {

                }


            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = cashSalesAdapter
        cashSalesAdapter.notifyDataSetChanged()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cash_sale_report_fragment, container, false)
    }
    fun filter(text: String) {
        val filteredCashAry: ArrayList<CashReport> = ArrayList()
        val cashAry : ArrayList<CashReport> = cashSales
        for (eachProduct in cashAry) {
            if (eachProduct.invoiceNumber!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredCashAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        cashSalesAdapter.filterList(filteredCashAry)
        cashSales=filteredCashAry

        cashSalesAdapter.notifyDataSetChanged()
        if (cashSalesAdapter.itemCount==0){
            recyclerView.visibility=View.VISIBLE
         //   Toasty.error(requireContext(),"No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            // noResults.visibility=View.INVISIBLE
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
    fun observers() {
        viewModel.observeCashReport().observe(
                viewLifecycleOwner,
                {
                    setStatus(it)
                    if (it.status == Status.SUCCESS) {
                        if (it.data?.cashreport != null && !it.data.cashreport!!.isEmpty()) {
                            setUpSales(it.data.cashreport as ArrayList<CashReport>)
                        } else {
                            setUpSales(ArrayList())
                        }
                    }

                })

    }
    private fun setUpSales(sales: ArrayList<CashReport>) {
        this.cashSales = sales
        productsTotal.text= cashSales.size.toString()
        cashSalesAdapter.refresh(this.cashSales)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<CashReportData>) {
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
               // empty_text.text = data.message
               // view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    fun init() {
        viewModel.getCashReport()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CashSaleReportViewModel::class.java)
        initView()
        init()
        observers()
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
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
          //  initView()
            init()
        //    observers()
            itemsswipetorefresh.isRefreshing = false
        }
    }

}