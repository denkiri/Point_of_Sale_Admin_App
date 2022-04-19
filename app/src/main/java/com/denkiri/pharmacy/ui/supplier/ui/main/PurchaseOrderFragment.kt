package com.denkiri.pharmacy.ui.supplier.ui.main

import android.annotation.SuppressLint
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
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.SupplierProductAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.order.CartData
import com.denkiri.pharmacy.models.order.Order
import com.denkiri.pharmacy.models.order.OrderData
import com.denkiri.pharmacy.models.product.Product
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.ui.orderform.AddToCartFragment
import com.denkiri.pharmacy.ui.orderform.CartFragment
import com.denkiri.pharmacy.ui.supplier.PurchaseOrderActivity
import com.denkiri.pharmacy.utils.BadgeView.BadgeView
import com.denkiri.pharmacy.utils.OnProductClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.purchase_order_fragment.*
class PurchaseOrderFragment : Fragment() {
    lateinit var productAdapter: SupplierProductAdapter
    private lateinit var products: ArrayList<Product>
    var supplierId: String?=null
    var supplierName:String?=null
    lateinit var qBadgeView: BadgeView
    var invoice:String?=null
    companion object {
        fun newInstance() = PurchaseOrderFragment()
    }
    private lateinit var viewModel: PurchaseOrderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.purchase_order_fragment, container, false)
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
        qBadgeView = BadgeView(context)
        supplierId=(activity as PurchaseOrderActivity).supplierId
        supplierName=(activity as PurchaseOrderActivity).supplierName
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            itemsswipetorefresh.isRefreshing = false
        }
        supplierCompany.text= "$supplierName Products"
        init()
        viewModel.getOfflineOrders()
        initView()
        invoice()
        observers()
    //    refreshBadge()
        fab.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, CartFragment())
                .commitNow()


        }

    }
    fun invoice(){
        if (PreferenceManager(requireContext()).getInvoiceNumber()==""){
            invoice= viewModel.getRandPassword(8)
            viewModel.saveInvoiceNumber("PO-"+invoice!!)

            //  view?.let { Snackbar.make(it, invoice!!, Snackbar.LENGTH_LONG).show() }
        }
        else{
           //  view?.let { Snackbar.make(it,PreferenceManager(requireContext()).getInvoiceNumber(), Snackbar.LENGTH_LONG).show() }
        }
    }
    private fun setCartCount(count: Int?) {
        qBadgeView.bindTarget(fab).badgeText = "" + count
    }
    fun refreshBadge() {
        setCartCount(0)
        viewModel.observeOfflineOrders().observe(viewLifecycleOwner, {
            if (it.data!=null){
                setCartCount(it.data.size)
            } })

    }
    fun init() {


            viewModel.getSupplierProducts(supplierId.toString())
        if (PreferenceManager(requireContext()).getInvoiceNumber()==""){
            invoice= viewModel.getRandPassword(8)
            viewModel.saveInvoiceNumber(invoice!!)
            viewModel.getInvoiceItems(PreferenceManager(requireContext()).getInvoiceNumber())
            //  view?.let { Snackbar.make(it, invoice!!, Snackbar.LENGTH_LONG).show() }
        }
        else{
            viewModel.getInvoiceItems(PreferenceManager(requireContext()).getInvoiceNumber())
            //  view?.let { Snackbar.make(it,PreferenceManager(requireContext()).getInvoiceNumber(), Snackbar.LENGTH_LONG).show() }
        }


    }
    private fun setStatus(data: Resource<ProductData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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
    private fun setStatusB(data: Resource<OrderData>) {
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
            viewModel.observeOfflineOrders()

            //   empty_layout.visibility = View.VISIBLE
            // main.visibility = View.GONE
            //empty_button.setOnClickListener({ init() })
        }
    }
    private fun setUpProducts(products: ArrayList<Product>) {
        this.products = products
        productAdapter.refresh(this.products)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun initView(){
        products = ArrayList()
        productAdapter = context?.let {
            SupplierProductAdapter(0, it, products, object : OnProductClick {
                override fun selected(pos: Int) {
                  /*  val args = Bundle()
                    //   args.putDouble("price", products[pos].price)
                    args.putString("productName", products[pos].productName)
                    args.putString("key", "value")
                    val bottomSheet = AddToCartFragment()
                    bottomSheet.setArguments(args)
                    bottomSheet.show((context as FragmentActivity).supportFragmentManager, bottomSheet.tag)

                   */
                }

                override fun onClickListener(position1: Int) {
                        val args = Bundle()
                 args.putString("productCode", products[position1].productCode)
                 args.putString("productId", products[position1].productId.toString())
                 args.putString("price", products[position1].price.toString())
                 args.putString("cost", products[position1].cost.toString())
                 args.putString("vat", products[position1].vat.toString())
                 args.putString("productName", products[position1].productName)
                 args.putString("quantityLeft", products[position1].qtyLeft.toString())
                 args.putString("supplierId",supplierId.toString())
                  args.putString("invoiceNumber",PreferenceManager(requireContext()).getInvoiceNumber())
                 val bottomSheet = AddToCartFragment()
                 bottomSheet.arguments = args
                 bottomSheet.isCancelable = false
                 bottomSheet.show((context as FragmentActivity).supportFragmentManager, bottomSheet.tag)
                }




            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = productAdapter
        productAdapter.notifyDataSetChanged()
    }
    fun observers(){
        viewModel.observeProducts().observe(viewLifecycleOwner, {
            setStatus(it)
            if (it.status == Status.SUCCESS) {
                if (it.data?.products != null && !it.data.products!!.isEmpty()) {
                    setUpProducts(it.data.products as ArrayList<Product>)
                } else {
                    setUpProducts(ArrayList())
                }
            }

        })
        viewModel.observeCartData().observe(viewLifecycleOwner, {
            setStatusB(it)
            if (it.status == Status.SUCCESS) {
                viewModel.saveTotal(it.data!!)



            }

        })
        viewModel.observeOfflineOrders().observe(viewLifecycleOwner, {
            if (it.data.isNullOrEmpty()){
                //  init()

            }})

    }
    fun filter(text: String) {
        val filteredProductAry: ArrayList<Product> = ArrayList()
        val productAry : ArrayList<Product> = products
        for (eachProduct in productAry) {
            if (eachProduct.productName!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredProductAry.add(eachProduct)

            }
        }
        //calling a method of the adapter class and passing the filtered list
        productAdapter.filterList(filteredProductAry)
        products=filteredProductAry

        productAdapter.notifyDataSetChanged()
        if (productAdapter.itemCount==0){
          //  noResults.visibility=View.INVISIBLE
            recyclerView.visibility=View.VISIBLE
            Toasty.error(requireContext(), "No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
         //   noResults.visibility=View.INVISIBLE
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
    override fun onStart() {
        super.onStart()
        refreshBadge()
    }

    override fun onPause() {
        super.onPause()
        refreshBadge()
    }

    override fun onResume() {
        super.onResume()
        refreshBadge()

    }


}