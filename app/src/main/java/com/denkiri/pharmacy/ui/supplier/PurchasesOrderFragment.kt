package com.denkiri.pharmacy.ui.supplier

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
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.OrderAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.order.PurchaseOrder
import com.denkiri.pharmacy.models.order.PurchaseOrderData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.ui.supplier.ui.main.PurchaseOrderViewModel
import com.denkiri.pharmacy.utils.OnReportClick
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_purchases_order.*
/**
 * A simple [Fragment] subclass.
 * Use the [PurchasesOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PurchasesOrderFragment : Fragment() {
    lateinit var orderAdapter: OrderAdapter
    private lateinit var order: ArrayList<PurchaseOrder>
    private lateinit var viewModel: PurchaseOrderViewModel
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PurchaseOrderViewModel::class.java)
        init()
        initView()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            //  initView()
            init()
            //    observers()
            itemsswipetorefresh.isRefreshing = false
        }
        back.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, SupplierFragment())
            .commitNow()  }
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getPurchasesInvoice()
        }
    }
    fun filter(text: String) {
        val filteredSalesAry: ArrayList<PurchaseOrder> = ArrayList()
        val salesAry : ArrayList<PurchaseOrder> = order
        for (eachProduct in salesAry) {
            if (eachProduct.invoiceNumber!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredSalesAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        orderAdapter.filterList(filteredSalesAry)
        order=filteredSalesAry

        orderAdapter.notifyDataSetChanged()
        if (orderAdapter.itemCount==0){

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
        viewModel.observePurchasesOrder().observe(
            viewLifecycleOwner,
            {
                setStatus(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.purchaseorderlist != null && !it.data.purchaseorderlist!!.isEmpty()) {
                        setUpSales(it.data.purchaseorderlist as ArrayList<PurchaseOrder>)
                    } else {
                        setUpSales(ArrayList())
                    }
                }

            })


    }
    private fun setUpSales(sales: ArrayList<PurchaseOrder>) {
        this.order = sales
        orderAdapter.refresh(this.order)
        invoiceTotal.text=order.size.toString()
        Handler().postDelayed({
        }, 1)
    }
    private fun setStatus(data: Resource<PurchaseOrderData>) {
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
            empty_button.setOnClickListener { viewModel.getPurchasesInvoice() }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchases_order, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PurchasesOrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                PurchasesOrderFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    private fun initView(){
        order= ArrayList()
        orderAdapter = context?.let {
            OrderAdapter(0,it, order, object : OnReportClick {

                override fun selected(pos: Int) {
                    viewModel.completePayment(order[pos].transactionId.toString())
                }

                override fun onClickListener(position1: Int) {
                    requireActivity().supportFragmentManager.beginTransaction()
                    val intent = Intent(activity, InvoiceActivity::class.java)
                    intent.putExtra("id", order[position1].invoiceNumber )
                    intent.putExtra("supplier", order[position1].suplierId.toString())
                    startActivity(intent)
                }


            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = orderAdapter
        orderAdapter.notifyDataSetChanged()
    }
}