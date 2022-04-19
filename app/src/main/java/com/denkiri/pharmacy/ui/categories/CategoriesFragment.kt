package com.denkiri.pharmacy.ui.categories

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.CategoryAdapter
import com.denkiri.pharmacy.models.category.Category
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.utils.OnSupplierItemClick
import kotlinx.android.synthetic.main.fragment_categories.avi
import kotlinx.android.synthetic.main.fragment_categories.back
import kotlinx.android.synthetic.main.fragment_categories.empty_button
import kotlinx.android.synthetic.main.fragment_categories.empty_layout
import kotlinx.android.synthetic.main.fragment_categories.fab
import kotlinx.android.synthetic.main.fragment_categories.itemsswipetorefresh
import kotlinx.android.synthetic.main.fragment_categories.main
import kotlinx.android.synthetic.main.fragment_categories.productsTotal
import kotlinx.android.synthetic.main.fragment_categories.recyclerView

class CategoriesFragment : Fragment() {
    lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categories: List<Category>
    val addCategoryFragment = AddCategoryFragment()
    val editCategoryFragment =EditCategoryFragment()
    private lateinit var categoriesViewModel: CategoryViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        categoriesViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
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
                   .replace(R.id.container, AddCategoryFragment())
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

        val root = inflater.inflate(R.layout.fragment_categories, container, false)

        return root
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            categoriesViewModel.getCategories()
        }
    }
    private fun initView(){
        categories = ArrayList()
        categoryAdapter = context?.let {
            CategoryAdapter(0, it, categories, object : OnSupplierItemClick {
                override fun delete(pos: Int) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Category")
                    builder.setMessage("Are you sure you want to delete?")
                    builder.setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        categoriesViewModel.deleteCategory((categories as ArrayList<Category>)[pos].categoryId)
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
                    bundle.putInt( "categoryId" , (categories as ArrayList<Category>)[pos].categoryId)
                    bundle.putString( "categoryName" , (categories as ArrayList<Category>)[pos].categoryName)
                   // val navController = Navigation.findNavController(requireView())
                  //  navController.navigate(R.id.nav_edit_category, bundle)
                    val fragment = EditFragment()
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
                    categoriesViewModel.activateCategory(categories[pos].categoryId.toString())

                }

                override fun disable(pos: Int) {
                    categoriesViewModel.deactivateCategory(categories[pos].categoryId.toString())
                }

                override fun onClickListener(pos: Int) {
                    val bundle = Bundle()
                    bundle.putString( "category" , (categories as ArrayList<Category>)[pos].categoryName)
                    // val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_edit_category, bundle)
                    val fragment = CategoryProductsFragment()
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
        recyclerView.adapter = categoryAdapter
        categoryAdapter.notifyDataSetChanged()
    }
    private fun setUpCategories(categories: ArrayList<Category>) {
        this.categories = categories
        productsTotal.text= categories.size.toString()
        categoryAdapter.refresh(this.categories)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<CategoryData>) {
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
        categoriesViewModel.observeCategories().observe(
            viewLifecycleOwner,
            {
                setStatus(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.categories != null && !it.data.categories!!.isEmpty()) {
                        setUpCategories(it.data.categories as ArrayList<Category>)
                    } else {
                        setUpCategories(ArrayList())
                    }
                }

            })
        categoriesViewModel.observeCategoriesAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.categories != null && !it.data.categories!!.isEmpty()) {

                    setUpCategories(it.data.categories as ArrayList<Category>)

                } else {

                    setUpCategories(ArrayList())
                }
            }

        })
    }







}