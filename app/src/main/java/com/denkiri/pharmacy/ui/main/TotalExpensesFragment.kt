package com.denkiri.pharmacy.ui.main
import android.app.AlertDialog
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
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.RecordedExpenseAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.expense.RecordedExpense
import com.denkiri.pharmacy.models.expense.RecordedExpenseData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.ui.categories.CategoryActivity
import com.denkiri.pharmacy.utils.RecordedExpenseItemClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.expense_fragment.*
import kotlinx.android.synthetic.main.fragment_total_expenses.*
import kotlinx.android.synthetic.main.fragment_total_expenses.avi
import kotlinx.android.synthetic.main.fragment_total_expenses.back
import kotlinx.android.synthetic.main.fragment_total_expenses.empty_button
import kotlinx.android.synthetic.main.fragment_total_expenses.empty_layout
import kotlinx.android.synthetic.main.fragment_total_expenses.itemsswipetorefresh
import kotlinx.android.synthetic.main.fragment_total_expenses.main
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
/**
 * A simple [Fragment] subclass.
 * Use the [TotalExpensesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TotalExpensesFragment : Fragment() {
    private lateinit var viewModel: ExpenseViewModel
    lateinit var expenseAdapter: RecordedExpenseAdapter
    private lateinit var expenses: ArrayList<RecordedExpense>
    // TODO: Rename and change types of parameters
    private fun initView(){
        expenses = ArrayList()
        expenseAdapter = context?.let {
            RecordedExpenseAdapter(0, it, expenses, object : RecordedExpenseItemClick {
                override fun delete(pos: Int) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Expense")
                    builder.setMessage("Are you sure you want to delete?")
                    builder.setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        viewModel.deleteRecordedExpense(expenses[pos].id)
                    }
                    builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()

                }

                override fun edit(pos: Int) {
                    val bundle = Bundle()
                    bundle.putInt( "expenseId" , expenses[pos].id!!)
                    bundle.putString( "expenseName" , expenses[pos].expenseName)
                    bundle.putString( "expenseAmount" , expenses[pos].amount.toString())
                    bundle.putString( "expenseDate" , expenses[pos].date)
                    bundle.putString( "expenseMonth" , expenses[pos].month)
                    bundle.putString( "expenseYear" , expenses[pos].year)
                    bundle.putString( "expensePayee" , expenses[pos].payee)
                    bundle.putString( "expenseReceiptNumber" , expenses[pos].receiptNumber)
                    bundle.putString( "expense" , expenses[pos].expense!!)
                    val fragment = EditRecordedExpenseFragment()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow()

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

                }
            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = expenseAdapter
        expenseAdapter.notifyDataSetChanged()
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getRecordedExpenses()
        }
    }
    private fun setUpExpenses(expenses: ArrayList<RecordedExpense>) {
        this.expenses = expenses
        expenseAdapter.refresh(this.expenses)
        Handler().postDelayed(Runnable {
        }, 1)
    }


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
        return inflater.inflate(R.layout.fragment_total_expenses, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TotalExpensesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            TotalExpensesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
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
        back.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ExpenseFragment())
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
        fab.setOnClickListener {
            //ddCategoryFragment.show(requireFragmentManager(), addCategoryFragment.tag )
            //  val navController = Navigation.findNavController(requireView())
            // navController.navigate(R.id.nav_add_category)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, RecordExpenseFragment())
                .commitNow()
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
    private fun setStatus(data: Resource<RecordedExpenseData>) {
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
                  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    fun observers(){
        viewModel.observeRecordedExpenses().observe(
            viewLifecycleOwner,
            {
                setStatus(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.expenses != null && !it.data.expenses!!.isEmpty()) {
                        setUpExpenses(it.data.expenses as ArrayList<RecordedExpense>)
                    } else {
                        setUpExpenses(ArrayList())
                    }
                }

            })
        viewModel.observeRecordedExpenseAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.expenses != null ) {

                    setUpExpenses(it.data.expenses as ArrayList<RecordedExpense>)

                } else {

                    setUpExpenses(ArrayList())
                }
            }

        })
        viewModel.getTotalExpenses().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                if(it.totalExpense!=0.0){
                    collectionTotal.text =format.format(+it.totalExpense).toString()}

            } catch (e: Exception) {
            }
        })
    }
    fun filter(text: String) {
        val filteredProductAry: ArrayList<RecordedExpense> = ArrayList()
        val productAry : ArrayList<RecordedExpense> = expenses
        for (eachProduct in productAry) {
            if (eachProduct.expenseName!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredProductAry.add(eachProduct)

            }
        }
        //calling a method of the adapter class and passing the filtered list
        expenseAdapter.filterList(filteredProductAry)
        expenses=filteredProductAry

        expenseAdapter.notifyDataSetChanged()
        if (expenseAdapter.itemCount==0){
            //  noResults.visibility=View.INVISIBLE
            recyclerView.visibility=View.VISIBLE
            Toasty.error(requireContext(), "No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            //  noResults.visibility=View.INVISIBLE
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
}