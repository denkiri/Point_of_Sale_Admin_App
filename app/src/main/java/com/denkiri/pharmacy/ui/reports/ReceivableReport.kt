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
import com.denkiri.pharmacy.adapters.AccountsReceivableAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivable
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivableData
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.receivable_report_fragment.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ReceivableReport : Fragment() {
    lateinit var accountsReceivableAdapter: AccountsReceivableAdapter
    private lateinit var accountReceivable: ArrayList<AccountsReceivable>

    companion object {
        fun newInstance() = ReceivableReport()
    }

    private lateinit var viewModel: ReceivableReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.receivable_report_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReceivableReportViewModel::class.java)
        initView()
        init()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
         //   initView()
            init()
        //    observers()
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
        accountReceivable = ArrayList()
        accountsReceivableAdapter = context?.let {
            AccountsReceivableAdapter(0,it, accountReceivable, object : OnCategoryItemClick {

                override fun delete(pos: Int) {
                    TODO("Not yet implemented")
                }

                override fun edit(pos: Int) {


                }

                override fun dial(pos: Int) {


                }

                override fun onClickListener(position1: Int) {

                }

                override fun onLongClickListener(position1: Int) {
                    TODO("Not yet implemented")
                }
            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = accountsReceivableAdapter
        accountsReceivableAdapter.notifyDataSetChanged()
    }

    fun filter(text: String) {
        val filteredAccountsReceivableAry: ArrayList<AccountsReceivable> = ArrayList()
        val accountsReceivableAry : ArrayList<AccountsReceivable> = accountReceivable
        for (eachProduct in accountsReceivableAry) {
            if (eachProduct.name!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredAccountsReceivableAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        accountsReceivableAdapter.filterList(filteredAccountsReceivableAry)
        accountReceivable=filteredAccountsReceivableAry

        accountsReceivableAdapter.notifyDataSetChanged()
        if (accountsReceivableAdapter.itemCount==0){
            recyclerView.visibility=View.VISIBLE
           // Toasty.error(requireContext(),"No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
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
                        if (it.data?.accountreceivablereport != null && !it.data.accountreceivablereport!!.isEmpty()) {
                            setUpAccountsReceivable(it.data.accountreceivablereport as ArrayList<AccountsReceivable>)
                        } else {
                            setUpAccountsReceivable(ArrayList())
                        }
                    }

                })
        viewModel.getTotalBalance().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                balance.text =format.format(it.balance).toString()

            } catch (e: Exception) {
            }
        })


    }
    private fun setUpAccountsReceivable(accountReceivable: ArrayList<AccountsReceivable>) {
        this.accountReceivable = accountReceivable
        accountsReceivableAdapter.refresh(this.accountReceivable)
        Handler().postDelayed(Runnable {
        }, 1)
    }

    fun init() {
        viewModel.getAccountsReceivables()
    }
    private fun setStatus(data: Resource<AccountsReceivableData>) {
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
               // view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }


}