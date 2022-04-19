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
import com.denkiri.pharmacy.adapters.CollectionAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.ui.home.HomeViewModel
import com.denkiri.pharmacy.utils.OnReportClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.collection_report_fragment.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CollectionReport : Fragment() {
    lateinit var collectionAdapter: CollectionAdapter
    private lateinit var collection: ArrayList<com.denkiri.pharmacy.models.reports.collectionReport.CollectionReport>

    companion object {
        fun newInstance() = CollectionReport()
    }

    private lateinit var viewModel: CollectionReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.collection_report_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CollectionReportViewModel::class.java)
        initView()
        init()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
         //   initView()
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
        collection= ArrayList()
        collectionAdapter = context?.let {
            CollectionAdapter( collection, object : OnReportClick {

                override fun selected(pos: Int) {
                    TODO("Not yet implemented")
                }

                override fun onClickListener(position1: Int) {
                    TODO("Not yet implemented")
                }


            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = collectionAdapter
        collectionAdapter.notifyDataSetChanged()
    }
    fun filter(text: String) {
        val filteredCollectionAry: ArrayList<com.denkiri.pharmacy.models.reports.collectionReport.CollectionReport> = ArrayList()
        val collectionAry : ArrayList<com.denkiri.pharmacy.models.reports.collectionReport.CollectionReport> = collection
        for (eachProduct in collectionAry) {
            if (eachProduct.name!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredCollectionAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        collectionAdapter.filterList(filteredCollectionAry)
        collection=filteredCollectionAry

        collectionAdapter.notifyDataSetChanged()
        if (collectionAdapter.itemCount==0){
            recyclerView.visibility=View.VISIBLE
           // Toasty.error(requireContext(),"No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
           // noResults.visibility=View.INVISIBLE
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
    fun observers() {
        viewModel.observeAccountsReceivables().observe(
                viewLifecycleOwner,
                {
                    setStatus(it)
                    if (it.status == Status.SUCCESS) {
                        if (it.data?.collectionreport != null && !it.data.collectionreport!!.isEmpty()) {
                            setUpCollection(it.data.collectionreport as ArrayList<com.denkiri.pharmacy.models.reports.collectionReport.CollectionReport>)
                        } else {
                            setUpCollection(ArrayList())
                        }
                    }

                })
        viewModel.getTotalCollection().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                collectionTotal.text =format.format(it.totalcollection).toString()

            } catch (e: Exception) {
            }
        })

    }
    private fun setUpCollection(collection: ArrayList<com.denkiri.pharmacy.models.reports.collectionReport.CollectionReport>) {
        this.collection = collection
        collectionAdapter.refresh(this.collection)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<com.denkiri.pharmacy.models.reports.collectionReport.CollectionReportData>) {
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
        viewModel.getCollection()
    }

}