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
import com.denkiri.pharmacy.adapters.MpesaAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.reports.mpesareport.MpesaReport
import com.denkiri.pharmacy.models.reports.mpesareport.MpesaReportData
import com.denkiri.pharmacy.ui.charts.MonthFragment
import com.denkiri.pharmacy.ui.receipt.ReceiptActivity
import com.denkiri.pharmacy.ui.receipt.ReceiptFragment
import com.denkiri.pharmacy.utils.OnReportClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.mpesa_sale_report_fragment.*
import kotlinx.android.synthetic.main.mpesa_sale_report_fragment.avi
import kotlinx.android.synthetic.main.mpesa_sale_report_fragment.empty_button
import kotlinx.android.synthetic.main.mpesa_sale_report_fragment.empty_layout
import kotlinx.android.synthetic.main.mpesa_sale_report_fragment.itemsswipetorefresh
import kotlinx.android.synthetic.main.mpesa_sale_report_fragment.main
import kotlinx.android.synthetic.main.mpesa_sale_report_fragment.productsTotal
import kotlinx.android.synthetic.main.mpesa_sale_report_fragment.recyclerView
import kotlinx.android.synthetic.main.mpesa_sale_report_fragment.searchView


class MpesaSaleReport : Fragment() {
    lateinit var mpesaSalesAdapter: MpesaAdapter
    private lateinit var mpesaSales: ArrayList<MpesaReport>

    companion object {
        fun newInstance() = MpesaSaleReport()
    }

    private lateinit var viewModel: MpesaSaleReportViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    private fun initView(){
        mpesaSales= ArrayList()
        mpesaSalesAdapter = context?.let {
            MpesaAdapter(0,it, mpesaSales, object : OnReportClick {

                override fun selected(pos: Int) {
                    val intent = Intent(activity, ReceiptActivity::class.java)
                    intent.putExtra("id",  mpesaSales[pos].invoiceNumber )
                    intent.putExtra("paymentMode",  mpesaSales[pos].type)
                    startActivity(intent)
                }

                override fun onClickListener(position1: Int) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ReceiptFragment())
                        .commitNow()
                }


            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = mpesaSalesAdapter
        mpesaSalesAdapter.notifyDataSetChanged()
    }
    fun filter(text: String) {
        val filteredMpesaAry: ArrayList<MpesaReport> = ArrayList()
        val mpesaAry : ArrayList<MpesaReport> = mpesaSales
        for (eachProduct in mpesaAry) {
            if (eachProduct.invoiceNumber!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredMpesaAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        mpesaSalesAdapter.filterList(filteredMpesaAry)
        mpesaSales=filteredMpesaAry

        mpesaSalesAdapter.notifyDataSetChanged()
        if (mpesaSalesAdapter.itemCount==0){
            recyclerView.visibility=View.VISIBLE
          //  Toasty.error(requireContext(),"No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            // noResults.visibility=View.INVISIBLE
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
    fun observers() {
        viewModel.observeMpesaReport().observe(
                viewLifecycleOwner,
                {
                    setStatus(it)
                    if (it.status == Status.SUCCESS) {
                        if (it.data?.mpesareport != null && !it.data.mpesareport!!.isEmpty()) {
                            setUpSales(it.data.mpesareport as ArrayList<MpesaReport>)
                        } else {
                            setUpSales(ArrayList())
                        }
                    }

                })

    }
    private fun setUpSales(sales: ArrayList<MpesaReport>) {
        this.mpesaSales = sales
        productsTotal.text= mpesaSales.size.toString()
        mpesaSalesAdapter.refresh(this.mpesaSales)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<MpesaReportData>) {
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
              //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    fun init() {
        viewModel.getMpesaReport()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mpesa_sale_report_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MpesaSaleReportViewModel::class.java)
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
         //   observers()
            itemsswipetorefresh.isRefreshing = false
        }
    }

}