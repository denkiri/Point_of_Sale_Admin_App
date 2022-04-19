package com.denkiri.pharmacy.ui.reorder

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
import com.denkiri.pharmacy.adapters.ProductAdapter
import com.denkiri.pharmacy.adapters.ReOrderAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.product.Product
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.ui.products.EditProduct
import com.denkiri.pharmacy.ui.products.IncreaseProductFragment
import com.denkiri.pharmacy.ui.products.IncreaseProductFragmentB
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.product_expiry_fragment.*
import kotlinx.android.synthetic.main.reorder_fragment.*
import kotlinx.android.synthetic.main.reorder_fragment.avi
import kotlinx.android.synthetic.main.reorder_fragment.back
import kotlinx.android.synthetic.main.reorder_fragment.empty_button
import kotlinx.android.synthetic.main.reorder_fragment.empty_layout
import kotlinx.android.synthetic.main.reorder_fragment.empty_layoutB
import kotlinx.android.synthetic.main.reorder_fragment.itemsswipetorefresh
import kotlinx.android.synthetic.main.reorder_fragment.main
import kotlinx.android.synthetic.main.reorder_fragment.noResults
import kotlinx.android.synthetic.main.reorder_fragment.recyclerView
import kotlinx.android.synthetic.main.reorder_fragment.searchView

class ReorderFragment : Fragment() {
    lateinit var productAdapter: ReOrderAdapter
    private lateinit var products: ArrayList<Product>
    companion object {
        fun newInstance() = ReorderFragment()
    }

    private lateinit var viewModel: ReorderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.reorder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReorderViewModel::class.java)
        initView()
        init()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
          //  initView()
            init()
          //  observers()
            itemsswipetorefresh.isRefreshing = false
        }
        back.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment())
            .commitNow()  }


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
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getProducts()
    }}
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
               // empty_text.text = data.message
               // view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
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
            ReOrderAdapter(0, it, products, object : OnCategoryItemClick {

                override fun delete(pos: Int) {
                    TODO("Not yet implemented")
                }

                override fun edit(pos: Int) {

                /*    val bundle = Bundle()
                    products[pos].productId?.let { it1 -> bundle.putInt("productId", it1) }

                    bundle.putString("productName", products[pos].productName)
                    bundle.putString("productDescription", products[pos].descriptionName)
                    bundle.putString("category", products[pos].category)
                    bundle.putString("supplier", products[pos].suplierId.toString())
                    bundle.putDouble("cost", products[pos].cost)
                    bundle.putDouble("price", products[pos].price)
                    bundle.putString("unit", products[pos].unit)
                    bundle.putString("code", products[pos].productCode)
                    products[pos].qtyLeft?.let { it1 -> bundle.putInt("qleft", it1) }
                    products[pos].reorder?.let { it1 -> bundle.putInt("reorder", it1) }
                    bundle.putString("date", products[pos].expirationDate)
                    //  val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_edit_product, bundle)
                    val fragment = EditProduct()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow()

                 */
                }

                override fun dial(pos: Int) {
                    TODO("Not yet implemented")

                }

                override fun onClickListener(pos: Int) {
                    val bundle = Bundle()
                    products[pos].productId?.let { it1 -> bundle.putInt("productId", it1) }

                    bundle.putString("productName", products[pos].productName)
                    bundle.putDouble("cost", products[pos].cost)
                    bundle.putDouble("price", products[pos].price)
                    bundle.putString("code", products[pos].productCode)
                    bundle.putString("date", products[pos].expirationDate)
                    //  val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_edit_product, bundle)
                    val fragment = IncreaseProductFragmentB()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow()

                }

                override fun onLongClickListener(position1: Int) {
                    TODO("Not yet implemented")
                }
            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = productAdapter
        productAdapter.notifyDataSetChanged()
    }
    fun observers(){
        viewModel.observeProducts().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            setStatus(it)
            if (it.status == Status.SUCCESS) {
                if (it.data?.products != null && !it.data.products!!.isEmpty()) {
                    setUpProducts(it.data.products as ArrayList<Product>)
                } else {
                    setUpProducts(ArrayList())
                    main.visibility = View.GONE
                    empty_layoutB.visibility=View.VISIBLE
                }
            }

        })
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
            noResults.visibility=View.INVISIBLE
            recyclerView.visibility=View.VISIBLE
            Toasty.error(requireContext(), "No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            noResults.visibility=View.INVISIBLE
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }

}