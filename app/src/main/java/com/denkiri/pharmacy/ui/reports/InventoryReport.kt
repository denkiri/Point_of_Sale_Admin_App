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
import com.denkiri.pharmacy.adapters.InventoryAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.reports.collectionReport.CollectionReportData
import com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReportData
import com.denkiri.pharmacy.utils.OnReportClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_transactions.*
import kotlinx.android.synthetic.main.inventory_report_fragment.*
import kotlinx.android.synthetic.main.inventory_report_fragment.avi
import kotlinx.android.synthetic.main.inventory_report_fragment.empty_button
import kotlinx.android.synthetic.main.inventory_report_fragment.empty_layout
import kotlinx.android.synthetic.main.inventory_report_fragment.itemsswipetorefresh
import kotlinx.android.synthetic.main.inventory_report_fragment.main
import kotlinx.android.synthetic.main.inventory_report_fragment.recyclerView
import kotlinx.android.synthetic.main.inventory_report_fragment.searchView

class InventoryReport : Fragment() {
    lateinit var inventoryAdapter: InventoryAdapter
    private lateinit var inventory: ArrayList<com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReport>

    companion object {
        fun newInstance() = InventoryReport()
    }

    private lateinit var viewModel: InventoryReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inventory_report_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InventoryReportViewModel::class.java)
        initView()
        init()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
          //  initView()
            init()
         //   observers()
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
        inventory= ArrayList()
        inventoryAdapter = context?.let {
            InventoryAdapter( inventory, object : OnReportClick {

                override fun selected(pos: Int) {
                    TODO("Not yet implemented")
                }

                override fun onClickListener(position1: Int) {
                    TODO("Not yet implemented")
                }


            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = inventoryAdapter
        inventoryAdapter.notifyDataSetChanged()
    }
    fun filter(text: String) {
        val filteredInventoryAry: ArrayList<com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReport> = ArrayList()
        val inventoryAry : ArrayList<com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReport> = inventory
        for (eachProduct in inventoryAry) {
            if (eachProduct.name!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredInventoryAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        inventoryAdapter.filterList(filteredInventoryAry)
        inventory=filteredInventoryAry

        inventoryAdapter.notifyDataSetChanged()
        if (inventoryAdapter.itemCount==0){
            noResults.visibility=View.INVISIBLE
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
        viewModel.observeInventory().observe(
                viewLifecycleOwner,
                {
                    setStatus(it)
                    if (it.status == Status.SUCCESS) {
                        if (it.data?.inventoryreport != null && !it.data.inventoryreport!!.isEmpty()) {
                            setUpInventory(it.data.inventoryreport as ArrayList<com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReport>)
                        } else {
                            setUpInventory(ArrayList())
                        }
                    }

                })

    }
    private fun setUpInventory(collection: ArrayList<com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReport>) {
        this.inventory = collection
        inventoryAdapter.refresh(this.inventory)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<InventoryReportData>) {
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
            empty_button.setOnClickListener({ init() })
        }
    }
    fun init() {
        viewModel.getInventoryReport()
    }


}