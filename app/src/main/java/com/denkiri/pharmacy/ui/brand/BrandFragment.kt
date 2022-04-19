package com.denkiri.pharmacy.ui.brand

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.BrandAdapter
import com.denkiri.pharmacy.adapters.CategoryAdapter
import com.denkiri.pharmacy.models.brand.Brand
import com.denkiri.pharmacy.models.brand.BrandData
import com.denkiri.pharmacy.models.category.Category
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.ui.categories.AddCategoryFragment
import com.denkiri.pharmacy.ui.categories.EditCategoryFragment
import com.denkiri.pharmacy.ui.categories.EditFragment
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.utils.OnSupplierItemClick
import kotlinx.android.synthetic.main.fragment_categories.*
class BrandFragment : Fragment() {
    lateinit var brandAdapter: BrandAdapter
    private lateinit var brands: List<Brand>
    val addCategoryFragment = AddCategoryFragment()
    val editCategoryFragment = EditCategoryFragment()
    private lateinit var brandsViewModel: BrandViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        brandsViewModel = ViewModelProvider(this).get(BrandViewModel::class.java)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView!!, dx, dy)
                if (dy > 0 && fab.visibility === View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility !== View.VISIBLE) {
                    fab.show()
                }
            }
        })
        initView()
        init()
        observers()
       fab.setOnClickListener {
            //ddCategoryFragment.show(requireFragmentManager(), addCategoryFragment.tag )
            //  val navController = Navigation.findNavController(requireView())
            // navController.navigate(R.id.nav_add_category)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddBrandFragment())
                .commitNow()
        }
        back.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment())
            .commitNow()  }
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            //   observers()
            //   initView()
            itemsswipetorefresh.isRefreshing = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_brand, container, false)

        return root
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            brandsViewModel.getBrands()
        }
    }
    private fun initView(){
        brands = ArrayList()
        brandAdapter = context?.let {
            BrandAdapter(0, it, brands, object : OnSupplierItemClick {
                override fun delete(pos: Int) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Brand")
                    builder.setMessage("Are you sure you want to delete?")
                    builder.setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        brandsViewModel.deleteBrand((brands as ArrayList<Brand>)[pos].brandId)
                    }
                    builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()

                }

                override fun edit(pos: Int) {
                    /*  val intent = Intent(activity, CategoryActivity::class.java)
                    intent.putExtra(
                        "categoryId",
                        (categories as ArrayList<Category>)[pos].categoryId
                    )
                    intent.putExtra(
                        "categoryName",
                        (categories as ArrayList<Category>)[pos].categoryName
                    )
                    startActivity(intent)*/
                    val bundle = Bundle()
                    bundle.putInt( "brandId" , (brands as ArrayList<Brand>)[pos].brandId)
                    bundle.putString( "brandName" , (brands as ArrayList<Brand>)[pos].brandName)
                    // val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_edit_category, bundle)
                    val fragment = EditBrandFragment()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow()

                }

                override fun dial(pos: Int) {

                }

                override fun view(pos: Int) {

                }

                override fun enable(pos: Int) {
                    brandsViewModel.activateBrand(brands[pos].brandId.toString())

                }

                override fun disable(pos: Int) {
                    brandsViewModel.deactivateBrand(brands[pos].brandId.toString())
                }

                override fun onClickListener(pos: Int) {
                    val bundle = Bundle()
                    bundle.putString( "brand", (brands as ArrayList<Brand>)[pos].brandName)
                    // val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_edit_category, bundle)
                    val fragment = BrandProductsFragment()
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
        recyclerView.adapter = brandAdapter
        brandAdapter.notifyDataSetChanged()
    }
    private fun setUpBrands(brands: ArrayList<Brand>) {
        this.brands = brands
        productsTotal.text= brands.size.toString()
        brandAdapter.refresh(this.brands)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<BrandData>) {
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
                //   empty_text.text = data.message
                //   view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    fun observers(){
        brandsViewModel.observeBrands().observe(
            viewLifecycleOwner,
            {
                setStatus(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.brands!= null && !it.data.brands!!.isEmpty()) {
                        setUpBrands(it.data.brands as ArrayList<Brand>)
                    } else {
                        setUpBrands(ArrayList())
                    }
                }

            })
        brandsViewModel.observeBrandsAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.brands != null && !it.data.brands!!.isEmpty()) {

                    setUpBrands(it.data.brands as ArrayList<Brand>)

                } else {

                    setUpBrands(ArrayList())
                }
            }

        })
    }







}