package com.denkiri.pharmacy.ui.orderform

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.CartAdapter
import com.denkiri.pharmacy.adapters.OnCartItemClick
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.order.CartData
import com.denkiri.pharmacy.models.order.Order
import com.denkiri.pharmacy.models.order.OrderData
import com.denkiri.pharmacy.models.purchases.PurchaseOrderData
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.ui.receipt.ReceiptActivity
import com.denkiri.pharmacy.ui.supplier.PurchaseOrderActivity
import com.denkiri.pharmacy.ui.supplier.PurchasesOrderFragment
import com.denkiri.pharmacy.ui.supplier.ui.main.PurchaseOrderFragment
import com.denkiri.pharmacy.ui.supplier.ui.main.PurchaseOrderViewModel
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_cart.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {

    private lateinit var viewModel: PurchaseOrderViewModel
    var invoice:String?=null
    var totalAmount:String?="0"
    var amount:String?="0"
    var id:String?=null
    var supplierId: String?=null
    lateinit var cartAdapter: CartAdapter
    private lateinit var order: ArrayList<Order>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.getOfflineOrders()
        viewModel.getTotalAmount()
        initView()
        observers()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PurchaseOrderViewModel::class.java)
        id=PreferenceManager(requireContext()).getInvoiceNumber()
        supplierId=(activity as PurchaseOrderActivity).supplierId
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                )
        )
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            //  initView()
            //  observers()
            itemsswipetorefresh.isRefreshing = false
        }
        viewModel.getOfflineOrders()
        viewModel.getTotalAmount()
        initView()
        observers()
        back.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment())
                    .commitNow()
        }
        empty_iconB.setOnClickListener {   requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, PurchaseOrderFragment())
                .commitNow() }
        btnCheckOut.setOnClickListener {
            val args = Bundle()
            args.putString("supplier",supplierId)
            val bottomSheet = SaveInvoiceFragment()
            bottomSheet.arguments = args
            bottomSheet.isCancelable = false
            bottomSheet.show((context as FragmentActivity).supportFragmentManager, bottomSheet.tag) }




    }
    private fun setStatus(data: Resource<OrderData>) {
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
                viewModel.getOfflineOrders()

            }
        }

    }

    fun init() {
        if (PreferenceManager(requireContext()).getInvoiceNumber()==""){
            invoice= viewModel.getRandPassword(8)
            viewModel.saveInvoiceNumber(invoice!!)
            viewModel.getTotalAmount()
            viewModel.getOrder(PreferenceManager(requireContext()).getInvoiceNumber())
            //  view?.let { Snackbar.make(it, invoice!!, Snackbar.LENGTH_LONG).show() }
        }
        else{
            viewModel.getTotalAmount()
            viewModel.getOrder(PreferenceManager(requireContext()).getInvoiceNumber())
            // view?.let { Snackbar.make(it, PreferenceManager(requireContext()).getInvoiceNumber(), Snackbar.LENGTH_LONG).show() }

        }

    }
    private fun setUpOrders(order: ArrayList<Order>) {
        this.order=order
        cartAdapter.refresh(this.order)
        totalItemsValue.text= order.size.toString()
        Handler().postDelayed(Runnable {
        }, 1)
    }

    private fun initView(){
        order = ArrayList()
        cartAdapter = context?.let {
            CartAdapter(0, it, order, object : OnCartItemClick {

                override fun delete(pos: Int) {
                   viewModel.deleteCartItem(order[pos].transactionId.toString(),order[pos].qty.toString(),order[pos].code.toString(),id.toString())
                }
                override fun increase(pos: Int) {
                    }
                override fun decrease(pos: Int) {

                }

                override fun onClickListener(position1: Int) {
                }

                override fun onLongClickListener(position1: Int) {

                }
            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = cartAdapter
        cartAdapter.notifyDataSetChanged()
    }
    fun observers(){
        viewModel.observeOfflineOrders().observe(viewLifecycleOwner, {
            if (it.data.isNullOrEmpty()){
                // button.visibility=View.VISIBLE
                viewModel.saveSupplierId("0")
                main.visibility = View.GONE
                empty_layoutB.visibility=View.VISIBLE
            }
            else{
                setUpOrders(it.data as ArrayList<Order>)}
        })
        viewModel.observeCartAction().observe(viewLifecycleOwner, {
            setStatus(it)
            if (it.status == Status.SUCCESS) {
                it.data?.let { it1 -> viewModel.saveOrders(it1) }
                 // it.data?.let { it1 -> viewModel.saveTotalAmount(it1) }
                if (it.data?.order != null && it.data.order!!.isNotEmpty()) {

                    setUpOrders(it.data.order as ArrayList<Order>)

                } else {
                    viewModel.saveSupplierId("0")
                    main.visibility = View.GONE
                    empty_layoutB.visibility=View.VISIBLE
                    setUpOrders(ArrayList())

                }
            }

        })
       viewModel.observeCartData().observe(viewLifecycleOwner, {
            setStatus(it)
            if (it.status == Status.SUCCESS) {

                it.data?.let { it1 -> viewModel.saveOrders(it1) }
                if (it.data?.order != null && it.data.order!!.isNotEmpty()) {


                    setUpOrders(it.data.order as ArrayList<Order>)


                } else {
                    viewModel.saveSupplierId("0")
                    setUpOrders(ArrayList())
                    main.visibility = View.GONE
                    empty_layoutB.visibility=View.VISIBLE
                }
            }

        })


        viewModel.getTotalAmount().observe(viewLifecycleOwner, {
            try {
                totalAmount=it.totalAmount.toString()
                amount=it.totalAmount.toString()
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.currency = Currency.getInstance("KSh")
                totalValue.text =format.format(it.totalAmount).toString()

            } catch (e: Exception) {
            }
        })
/*
        viewModel.observeTransactionData().observe(
                viewLifecycleOwner,
                {
                    setStatusD(it)
                    if (it.status == Status.SUCCESS) {
                        viewModel.saveInvoiceNumber("")
                        val intent = Intent(activity, ReceiptActivity::class.java)
                        intent.putExtra("id",id)
                        intent.putExtra("paymentMode",radio?.text.toString())
                        startActivity(intent)

                    }
                    if (it.status == Status.ERROR) {
                        Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG,false).show()
                    }
                })

 */


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    private fun setStatusD(data: Resource<PurchaseOrderData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            btnCheckOut.isEnabled = false
            btnCheckOut.isClickable = false
            btnCheckOut.text=getString(R.string.save)
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            btnCheckOut.isEnabled = true
            btnCheckOut.isClickable = true
            btnCheckOut.text=getString(R.string.save_receipt)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                empty_text.text = data.message
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }

            }
        }
    }



}