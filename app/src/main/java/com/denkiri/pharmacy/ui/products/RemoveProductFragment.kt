package com.denkiri.pharmacy.ui.products

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
import com.denkiri.pharmacy.adapters.ExpiringAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.product.Product
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.ui.expiry.ProductExpiryViewModel
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_remove_product.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RemoveProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RemoveProductFragment : Fragment() {
    lateinit var productAdapter: ExpiringAdapter
    private lateinit var products: ArrayList<Product>
    private lateinit var viewModel: ProductsViewModel
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        initView()
        init()
        observers()
        back.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductsFragment())
                .commitNow()  }

        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            observers()
            initView()
            itemsswipetorefresh.isRefreshing = false
        }
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
              //  empty_text.text = data.message
              //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
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
            ExpiringAdapter(0, it, products, object : OnCategoryItemClick {

                override fun delete(pos: Int) {
                    val bundle = Bundle()
                    products[pos].productId?.let { it1 -> bundle.putInt("productId", it1) }
                    bundle.putString("productName", products[pos].productName)
                    bundle.putString("productDescription", products[pos].descriptionName)
                    bundle.putString("category", products[pos].category)
                    bundle.putDouble("cost", products[pos].cost)
                    bundle.putString("code", products[pos].productCode)
                    products[pos].qtyLeft?.let { it1 -> bundle.putInt("qleft", it1) }
                    products[pos].reorder?.let { it1 -> bundle.putInt("reorder", it1) }
                    bundle.putString("date", products[pos].expirationDate)
                    //  val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_edit_product, bundle)
                    val fragment = RemoveStockB()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow()

                }

                override fun edit(pos: Int) {

                    /*     val bundle = Bundle()
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

                override fun onClickListener(position1: Int) {

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remove_product, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RemoveProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                RemoveProductFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
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
}