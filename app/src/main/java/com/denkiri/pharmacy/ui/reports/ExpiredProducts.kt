package com.denkiri.pharmacy.ui.reports

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
import com.denkiri.pharmacy.adapters.ExpiryAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.reports.expiredproducts.ExpiryReport
import com.denkiri.pharmacy.models.reports.expiredproducts.ExpiryReportData
import com.denkiri.pharmacy.utils.OnReportClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.collection_report_fragment.*
import kotlinx.android.synthetic.main.expired_products_fragment.*
import kotlinx.android.synthetic.main.expired_products_fragment.avi
import kotlinx.android.synthetic.main.expired_products_fragment.empty_button
import kotlinx.android.synthetic.main.expired_products_fragment.empty_layout
import kotlinx.android.synthetic.main.expired_products_fragment.itemsswipetorefresh
import kotlinx.android.synthetic.main.expired_products_fragment.main
import kotlinx.android.synthetic.main.expired_products_fragment.recyclerView
import kotlinx.android.synthetic.main.expired_products_fragment.searchView
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ExpiredProducts : Fragment() {
    lateinit var expiryAdapter: ExpiryAdapter
    private lateinit var expiry: ArrayList<ExpiryReport>

    companion object {
        fun newInstance() = ExpiredProducts()
    }

    private lateinit var viewModel: ExpiredProductsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.expired_products_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExpiredProductsViewModel::class.java)
        initView()
        init()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
          //  initView()
            init()
           // observers()
            itemsswipetorefresh.isRefreshing = false
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
        expiry= ArrayList()
        expiryAdapter = context?.let {
            ExpiryAdapter( expiry, object : OnReportClick {

                override fun selected(pos: Int) {
                    TODO("Not yet implemented")
                }

                override fun onClickListener(position1: Int) {
                    TODO("Not yet implemented")
                }


            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = expiryAdapter
        expiryAdapter.notifyDataSetChanged()
    }
    fun filter(text: String) {
        val filteredExpiryAry: ArrayList<ExpiryReport> = ArrayList()
        val expiryAry : ArrayList<ExpiryReport> = expiry
        for (eachProduct in expiryAry) {
            if (eachProduct.productName!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredExpiryAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        expiryAdapter.filterList(filteredExpiryAry)
        expiry=filteredExpiryAry

        expiryAdapter.notifyDataSetChanged()
        if (expiryAdapter.itemCount==0){
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
        viewModel.observeExpiry().observe(
                viewLifecycleOwner,
                {
                    setStatus(it)
                    if (it.status == Status.SUCCESS) {
                        if (it.data?.expiryreport != null && !it.data.expiryreport!!.isEmpty()) {
                            setUpExpiry(it.data.expiryreport as ArrayList<ExpiryReport>)
                        } else {
                            setUpExpiry(ArrayList())
                        }
                    }

                })
        viewModel.getTotalLose().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                loseTotal.text =format.format(it.totalLose).toString()

            } catch (e: Exception) {
            }
        })

    }
    private fun setUpExpiry(expiry: ArrayList<ExpiryReport>) {
        this.expiry = expiry
        expiryAdapter.refresh(this.expiry)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<ExpiryReportData>) {
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
        viewModel.getExpiredProducts()
    }

}