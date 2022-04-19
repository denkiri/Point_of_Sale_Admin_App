package com.denkiri.pharmacy.ui.products

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.BrandItemAdapter
import com.denkiri.pharmacy.adapters.CategoryItemAdapter
import com.denkiri.pharmacy.adapters.SupplierItemAdapter
import com.denkiri.pharmacy.models.brand.Brand
import com.denkiri.pharmacy.models.brand.BrandData
import com.denkiri.pharmacy.models.category.Category
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.models.supplier.Supplier
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.ui.supplier.SupplierProductsFragment
import com.denkiri.pharmacy.utils.DatePickerHelper
import com.denkiri.pharmacy.utils.OnCategoryClick
import com.denkiri.pharmacy.utils.OnSupplierClick
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_edit_product_b.avi
import kotlinx.android.synthetic.main.fragment_edit_product_b.back
import kotlinx.android.synthetic.main.fragment_edit_product_b.bran
import kotlinx.android.synthetic.main.fragment_edit_product_b.btSelectDate
import kotlinx.android.synthetic.main.fragment_edit_product_b.categ
import kotlinx.android.synthetic.main.fragment_edit_product_b.cost
import kotlinx.android.synthetic.main.fragment_edit_product_b.description
import kotlinx.android.synthetic.main.fragment_edit_product_b.editProduct
import kotlinx.android.synthetic.main.fragment_edit_product_b.empty_button
import kotlinx.android.synthetic.main.fragment_edit_product_b.empty_layout
import kotlinx.android.synthetic.main.fragment_edit_product_b.itemsswipetorefresh
import kotlinx.android.synthetic.main.fragment_edit_product_b.main
import kotlinx.android.synthetic.main.fragment_edit_product_b.price
import kotlinx.android.synthetic.main.fragment_edit_product_b.productName
import kotlinx.android.synthetic.main.fragment_edit_product_b.pullOut
import kotlinx.android.synthetic.main.fragment_edit_product_b.qty
import kotlinx.android.synthetic.main.fragment_edit_product_b.recyclerView
import kotlinx.android.synthetic.main.fragment_edit_product_b.recyclerView2
import kotlinx.android.synthetic.main.fragment_edit_product_b.recyclerView3
import kotlinx.android.synthetic.main.fragment_edit_product_b.reorder
import kotlinx.android.synthetic.main.fragment_edit_product_b.spinner
import kotlinx.android.synthetic.main.fragment_edit_product_b.supp
import kotlinx.android.synthetic.main.fragment_edit_product_b.tvDate
import kotlinx.android.synthetic.main.fragment_edit_product_b.vat
import java.util.*
import kotlin.collections.ArrayList
class EditProductFragmentB : Fragment() {
    lateinit var datePicker: DatePickerHelper
    private lateinit var productsViewModel: ProductsViewModel
    lateinit var categoryAdapter: CategoryItemAdapter
    private lateinit var categories: List<Category>
    lateinit var supplierAdapter: SupplierItemAdapter
    private lateinit var suppliers: List<Supplier>
    lateinit var brandAdapter: BrandItemAdapter
    private lateinit var brands:List<Brand>
    var supplierId:String?=null
    var category:String?=null
    var supplier:String?=null
    var brand:String?=null
    var unit:String?=null
    var productId:Int?=null
    var expDate:String?=null
    var qtyLeft:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product_b, container, false)
    }
    fun init() {
        productsViewModel.getSuppliers()
        productsViewModel.getCategories()
        productsViewModel.getBrands()
    }
    private fun setStatusSupplier(data: Resource<SupplierData>) {
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
            empty_button.setOnClickListener { init() }
        }
    }
    private fun setStatusBrand(data: Resource<BrandData>) {
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
    private fun setStatus(data: Resource<CategoryData>) {
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
                //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener { init() }
        }
    }
    private fun setUpSuppliers(suppliers: ArrayList<Supplier>) {
        this.suppliers = suppliers
        supplierAdapter.refresh(this.suppliers)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setUpBrands(brands: ArrayList<Brand>) {
        this.brands = brands
        brandAdapter.refresh(this.brands)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun spinnerItem(){
        val units = resources.getStringArray(R.array.units)
        val spinner=spinner
        if (spinner != null) {
            val adapter = context?.let {
                ArrayAdapter(it,
                    R.layout.spinner_item, units)
            }
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    unit=units[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }



    }
    private fun setUpCategories(categories: ArrayList<Category>) {
        this.categories = categories
        categoryAdapter.refresh(this.categories)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        val minDate = Calendar.getInstance()
        minDate.set(Calendar.HOUR_OF_DAY, 0)
        minDate.set(Calendar.MINUTE, 0)
        minDate.set(Calendar.SECOND, 0)
        datePicker.setMinDate(minDate.timeInMillis)
        datePicker.showDialog(y,m,d, object : DatePickerHelper.Callback {
            override fun onDateSelected(year: Int,month: Int,dayofMonth: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                tvDate.text = "${year}-${monthStr}-${dayStr}"
                expDate="${dayStr}-${monthStr}-${year}"
            }
        })
    }
    fun setUpUi() {

        productsViewModel.observeCategories().observe(
            viewLifecycleOwner,
            {
                setStatus(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.categories != null && it.data.categories!!.isNotEmpty()) {
                        setUpCategories(it.data.categories as ArrayList<Category>)
                    } else {
                        setUpCategories(ArrayList())
                    }
                }

            })
        productsViewModel.observeSuppliers().observe(viewLifecycleOwner, {
            setStatusSupplier(it)
            if (it.status == Status.SUCCESS) {
                if (it.data?.suppliers != null && it.data.suppliers!!.isNotEmpty()) {
                    setUpSuppliers(it.data.suppliers as ArrayList<Supplier>)
                } else {
                    setUpSuppliers(ArrayList())
                }
            }

        })
        productsViewModel.observeProductsAction().observe(viewLifecycleOwner, {
            status(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.products != null && it.data.products!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                    //    val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_product)
                    val bundle = Bundle()

                    bundle.putInt("supplierId", supplierId!!.toInt())
                    val fragment = SupplierProductsFragment()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow()
                }
                else{
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })
        productsViewModel.observeBrands().observe(
                viewLifecycleOwner,
                {
                    setStatusBrand(it)
                    if (it.status == Status.SUCCESS) {
                        if (it.data?.brands != null && !it.data.brands!!.isEmpty()) {
                            setUpBrands(it.data.brands as ArrayList<Brand>)
                        } else {
                            setUpBrands(ArrayList())
                        }
                    }

                })
    }
    private fun initViewBrand(){
        brands = ArrayList()
        brandAdapter = context?.let {
            BrandItemAdapter(0, it, brands, object : OnCategoryClick {

                override fun selected(pos: Int) {
                    (brands as ArrayList<Brand>)[pos].brandName?.let { it1 -> Toasty.success(requireContext(), it1, Toast.LENGTH_SHORT, true).show() }
                    brand= (brands as ArrayList<Brand>)[pos].brandName.toString()
                    bran.text=(brands as ArrayList<Brand>)[pos].brandName.toString()
                }


                override fun onClickListener(position1: Int) {

                }


            })
        }!!
        recyclerView3.layoutManager = LinearLayoutManager(this.context)
        recyclerView3.adapter = brandAdapter
        brandAdapter.notifyDataSetChanged()

    }
    private fun initView(){
        categories = ArrayList()
        categoryAdapter = context?.let {
            CategoryItemAdapter(0, it, categories, object : OnCategoryClick {

                override fun selected(pos: Int) { (categories as ArrayList<Category>)[pos].categoryName?.let { it1 -> Toasty.success(requireContext(), it1, Toast.LENGTH_SHORT, true).show() }
                    category=(categories as ArrayList<Category>)[pos].categoryName
                    categ.text=(categories as ArrayList<Category>)[pos].categoryName
                }

                override fun onClickListener(position1: Int) {

                }


            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = categoryAdapter
        categoryAdapter.notifyDataSetChanged()
    }
    private fun initViewSupplier(){
        suppliers = ArrayList()
        supplierAdapter = context?.let {
            SupplierItemAdapter(0, it, suppliers, object : OnSupplierClick {

                override fun selected(pos: Int) { (suppliers as ArrayList<Supplier>)[pos].suplierName?.let { it1 -> Toasty.success(requireContext(), it1, Toast.LENGTH_SHORT, true).show() }
                    supplier= (suppliers as ArrayList<Supplier>)[pos].suplierId.toString()
                    supp.text=(suppliers as ArrayList<Supplier>)[pos].suplierName.toString()
                }

                override fun delete(pos: Int) {

                }

                override fun onClickListener(position1: Int) {

                }


            })
        }!!
        recyclerView2.layoutManager = LinearLayoutManager(this.context)
        recyclerView2.adapter = supplierAdapter
        supplierAdapter.notifyDataSetChanged()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditProduct.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            EditProductFragmentB().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        productsViewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        initView()
        initViewSupplier()
        initViewBrand()
        setUpUi()
        init()
        spinnerItem()
        datePicker = DatePickerHelper(requireContext(),true)
        btSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        val bundle = arguments
        // Toasty.error(requireContext(),(bundle?.getInt("productId")).toString(), Toast.LENGTH_LONG, true).show()
        productId=(bundle?.getInt("productId"))
        unit=(bundle?.getString("unit"))
        category=(bundle?.getString("category"))
        supplier=(bundle?.getString("supplier"))
        brand=(bundle?.getString("brand"))
        supplierId=(bundle?.getString("supplier"))
        productName.setText((bundle?.getString("productName")))
        supp.text=(bundle?.getString("supplierName"))
        categ.text=(bundle?.getString("category"))
        bran.text=(bundle?.getString("brand"))
        vat.setText((bundle?.getDouble("vat")).toString())
        description.setText((bundle?.getString("productDescription")))
        cost.setText((bundle?.getDouble("cost")).toString())
        price.setText((bundle?.getDouble("price")).toString())
        reorder.setText((bundle?.getInt("reorder")).toString())
        pullOut.setOnClickListener { deleteProduct() }
        tvDate.text = (bundle?.getString("date"))
        expDate=(bundle?.getString("date"))
        qtyLeft=(bundle?.getInt("qleft"))
        if(qtyLeft!! <1){
            pullOut.visibility=View.VISIBLE
        }

        qty.setText((bundle?.getInt("qleft")).toString())
        editProduct.setOnClickListener { editProduct() }
        back.setOnClickListener {
            val bundle = Bundle()

            bundle.putInt("supplierId", supplierId!!.toInt())
            val fragment = SupplierProductsFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
         }
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            //  initView()
            //  observers()
            itemsswipetorefresh.isRefreshing = false
        }
    }
    fun editProduct(){
        if (validate()){
            productsViewModel.editProduct(price.text.toString(),cost.text.toString(),productName.text.toString(),description.text.toString(),supplier.toString(),qty.text.toString(),category.toString(),brand.toString(),vat.text.toString(),unit.toString(),reorder.text.toString(),expDate.toString(),productId.toString().trim())
        }
    }
    fun deleteProduct(){
        if (qtyLeft!! >0){
            Toasty.error(requireContext(),"Failed!Product Items Exist", Toast.LENGTH_LONG, true).show()

        }
        else{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Product")
            builder.setMessage("Are you sure you want to delete?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                productsViewModel.deleteProduct(productId.toString().trim())
            }
            builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            val alert = builder.create()
            alert.show()

        }

    }
    private fun validate(): Boolean {
        if (!Validator.isValidName(productName)) {
            return false
        }
        if (!Validator.isValidPrice(cost)) {
            return false
        }
        if (!Validator.isValidPrice(price)) {
            return false
        }
        if (!Validator.isValidAmount(qty)) {
            return false
        }
        if (!Validator.isValidAmount(reorder)) {
            return false
        }
        if (!Validator.isValidAmount(vat)) {
            return false
        }
        if (category==null){
            Toasty.error(requireContext(),"Select Product Category", Toast.LENGTH_LONG, false).show()
            return false
        }
        if (supplier==null){
            Toasty.error(requireContext(),"Select Product Supplier", Toast.LENGTH_LONG, false).show()
            return false
        }
        if (tvDate.text=="null"){
            Toasty.error(requireContext(),"Set Expiry Date", Toast.LENGTH_LONG, false).show()
            return false
        }



        return true
    }
    private fun status(data: Resource<ProductData>) {
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
                Toasty.error(requireContext(),data.message.toString(), Toast.LENGTH_LONG, true).show()
            }

        }
    }

}