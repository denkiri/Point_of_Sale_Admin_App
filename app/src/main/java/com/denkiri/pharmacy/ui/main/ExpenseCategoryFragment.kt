package com.denkiri.pharmacy.ui.main

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
import com.denkiri.pharmacy.adapters.ExpenseAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.expense.ExpenseData
import com.denkiri.pharmacy.models.expense.Expenses
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.utils.OnExpenseItemClick
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_expense_category.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ExpenseCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpenseCategoryFragment : Fragment() {
    private lateinit var viewModel: ExpenseViewModel
    lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var expenses: List<Expenses>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_category, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExpenseCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ExpenseCategoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        back.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ExpenseFragment())
                .commitNow()
        }
        fab.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddExpense())
                .commitNow()
        }
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
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            //   observers()
            //   initView()
            itemsswipetorefresh.isRefreshing = false
        }

    }
    private fun initView(){
        expenses = ArrayList()
        expenseAdapter = context?.let {
            ExpenseAdapter(0, it, expenses, object : OnExpenseItemClick {
                override fun delete(pos: Int) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Expense")
                    builder.setMessage("Are you sure you want to delete?")
                    builder.setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        viewModel.deleteExpense((expenses as ArrayList<Expenses>)[pos].id)
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
                    bundle.putInt( "expenseId" , (expenses as ArrayList<Expenses>)[pos].id!!)
                    bundle.putString( "expenseName" , (expenses as ArrayList<Expenses>)[pos].name)
                    bundle.putString( "expenseAmount" , (expenses as ArrayList<Expenses>)[pos].amount.toString()
                    )
                    // val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_edit_category, bundle)
                    val fragment = EditExpenseFragment()
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
                    viewModel.activateExpense(expenses[pos].id.toString())

                }

                override fun disable(pos: Int) {
                    viewModel.deactivateExpense(expenses[pos].id.toString())
                }

                override fun onClickListener(pos: Int) {
                  /*
                    val bundle = Bundle()
                    bundle.putString( "category" , (expenses as ArrayList<Expenses>)[pos].name)
                    // val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_edit_category, bundle)
                    val fragment = CategoryProductsFragment()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow()


                   */
                }

                override fun onLongClickListener(position1: Int) {
                    TODO("Not yet implemented")
                }
            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = expenseAdapter
        expenseAdapter.notifyDataSetChanged()
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getExpenses()
        }
    }
    private fun setUpExpenses(expenses: ArrayList<Expenses>) {
        this.expenses = expenses
        expenseAdapter.refresh(this.expenses)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<ExpenseData>) {
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
    private fun setStatusB(data: Resource<ExpenseData>) {
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
            main.visibility = View.VISIBLE
        }


        if (status == Status.ERROR) {
            if (data.message != null) {
                fab.visibility=View.GONE
                // empty_text.text = data.message
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            //   empty_layout.visibility = View.VISIBLE
            main.visibility = View.VISIBLE
            //    empty_button.setOnClickListener({ init() })
        }
    }
    fun observers(){
        viewModel.observeExpenses().observe(
            viewLifecycleOwner,
            {
                setStatus(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.expenses != null && !it.data.expenses!!.isEmpty()) {
                        setUpExpenses(it.data.expenses as ArrayList<Expenses>)
                    } else {
                        setUpExpenses(ArrayList())
                    }
                }

            })
        viewModel.observeExpenseAction().observe(viewLifecycleOwner, {
            setStatusB(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.expenses != null && it.data.expenses!!.isNotEmpty()) {
                    setUpExpenses(it.data.expenses as ArrayList<Expenses>)
                } else {
                    setUpExpenses(ArrayList())
                }
            }

        })
    }

}