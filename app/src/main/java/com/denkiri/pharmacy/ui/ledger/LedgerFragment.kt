package com.denkiri.pharmacy.ui.ledger

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.Splashscreen
import com.denkiri.pharmacy.adapters.InvoiceAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.invoice.Invoice
import com.denkiri.pharmacy.models.invoice.InvoiceData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager

import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.ledger_fragment.*
class LedgerFragment : Fragment() {
    lateinit var invoiceAdapter: InvoiceAdapter
    private lateinit var invoices: ArrayList<Invoice>
    internal  var number:String?=""
    companion object {
        fun newInstance() = LedgerFragment()
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
    fun filter(text: String) {
        val filteredInvoiceAry: ArrayList<Invoice> = ArrayList()
        val invoiceAry : ArrayList<Invoice> = invoices
        for (eachProduct in invoiceAry) {
            if (eachProduct.customerName!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredInvoiceAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        invoiceAdapter.filterList(filteredInvoiceAry)
        invoices=filteredInvoiceAry

        invoiceAdapter.notifyDataSetChanged()
        if (invoiceAdapter.itemCount==0){
            noResults.visibility=View.INVISIBLE
            recyclerView.visibility=View.VISIBLE
            Toasty.error(requireContext(),"No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            noResults.visibility=View.INVISIBLE
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
    fun observers() {
        viewModel.observeInvoices().observe(
                viewLifecycleOwner,
                {
                    setStatus(it)
                    if (it.status == Status.SUCCESS) {
                        if (it.data?.invoices != null && !it.data.invoices!!.isEmpty()) {
                            setUpInvoices(it.data.invoices as ArrayList<Invoice>)
                        } else {
                            setUpInvoices(ArrayList())
                            main.visibility = View.GONE
                            empty_layoutB.visibility=View.VISIBLE
                        }

                    }

                })

    }
    private fun setUpInvoices(invoices: ArrayList<Invoice>) {
        this.invoices = invoices
        invoiceAdapter.refresh(this.invoices)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<InvoiceData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
          //  activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE )
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
    fun init() {

            viewModel.getInvoices()

    }
    private fun initView(){
        invoices = ArrayList()
        invoiceAdapter = context?.let {
            InvoiceAdapter(0,it, invoices, object : OnCategoryItemClick {

                override fun delete(pos: Int) {
                    TODO("Not yet implemented")
                }

                override fun edit(pos: Int) {

                   val bundle = Bundle()
                    bundle.putString( "invoiceNumber" , invoices[pos].invoiceNumber)
                    bundle.putString( "totalAmount" , invoices[pos].totalAmount.toString())
                    bundle.putDouble( "amount" , invoices[pos].balance)
                  //  val navController = Navigation.findNavController(requireView())
                   // navController.navigate(R.id.nav_add_payment, bundle)
                    val fragment = AddPayment()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commitNow()
                }

                override fun dial(pos: Int) {
                    number = (invoices as ArrayList<Invoice>)[pos].contact?.trim()
                    //Dialer intent
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number)))
                    startActivity(intent)

                }

                override fun onClickListener(position1: Int) {

                }

                override fun onLongClickListener(position1: Int) {
                    TODO("Not yet implemented")
                }
            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = invoiceAdapter
        invoiceAdapter.notifyDataSetChanged()
    }
    private lateinit var viewModel: LedgerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ledger_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LedgerViewModel::class.java)
        initView()
        init()
        observers()
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
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
          //  observers()
         //   initView()
            itemsswipetorefresh.isRefreshing = false
        }
    }

}