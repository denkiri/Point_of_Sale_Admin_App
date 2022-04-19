package com.denkiri.pharmacy.ui.main.ui.main

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
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.RecordedExpenseAdapterB
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.expense.DayIncomeAndExpenseData
import com.denkiri.pharmacy.models.expense.RecordedExpense
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.ui.main.EditRecordedExpenseFragment
import com.denkiri.pharmacy.ui.main.ExpenseViewModel
import com.denkiri.pharmacy.utils.DatePickerHelper
import com.denkiri.pharmacy.utils.RecordedExpenseItemClick
import kotlinx.android.synthetic.main.fragment_day_expense_report.*
import kotlinx.android.synthetic.main.fragment_day_expense_report.avi
import kotlinx.android.synthetic.main.fragment_day_expense_report.empty_button
import kotlinx.android.synthetic.main.fragment_day_expense_report.empty_layout
import kotlinx.android.synthetic.main.fragment_day_expense_report.itemsswipetorefresh
import kotlinx.android.synthetic.main.fragment_day_expense_report.main
import kotlinx.android.synthetic.main.fragment_day_expense_report.productsTotal
import kotlinx.android.synthetic.main.fragment_day_expense_report.recyclerView
import kotlinx.android.synthetic.main.fragment_product.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [DayExpenseReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DayExpenseReportFragment : Fragment() {
    lateinit var datePicker: DatePickerHelper
    private lateinit var viewModel: ExpenseViewModel
    lateinit var expenseAdapter: RecordedExpenseAdapterB
    private lateinit var expenses: ArrayList<RecordedExpense>
    private fun initView(){
        expenses = ArrayList()
        expenseAdapter = context?.let {
            RecordedExpenseAdapterB(0, it, expenses, object : RecordedExpenseItemClick {
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
    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        val maxDate = Calendar.getInstance()
        maxDate.set(Calendar.HOUR_OF_DAY, 0)
        maxDate.set(Calendar.MINUTE, 0)
        maxDate.set(Calendar.SECOND, 0)
        datePicker.setMaxDate(maxDate.timeInMillis)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                tvDate.text = "${monthStr}/${dayStr}/${year}"
                viewModel.getDayIncomeAndExpense("${monthStr}/${dayStr}/${year}")

            }
        })
    }
    private fun setStatus(data: Resource<DayIncomeAndExpenseData>) {
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
    fun observers() {
        viewModel.observeDayIncomeAndExpenseReport().observe(
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

        viewModel.getDayExpenseB().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                expense.text = format.format(-it.dayExpense).toString()
            } catch (e: Exception) {
            }
        })
        viewModel.getDayIncomeB().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                incomeText.text = format.format(+it.dayIncome).toString()
            } catch (e: Exception) {
            }
        })
        viewModel.getDayNetProfitB().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                profit.text =format.format(+it.dayNetProfit).toString()

            } catch (e: Exception) {
            }
        })

    }
    private fun setUpExpenses(expenses: ArrayList<RecordedExpense>) {
        this.expenses = expenses
        productsTotal.text= expenses.size.toString()
        expenseAdapter.refresh(this.expenses)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getDayIncomeAndExpense(tvDate.text.toString())
        }
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
        return inflater.inflate(R.layout.fragment_day_expense_report, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DayExpenseReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DayExpenseReportFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        datePicker = DatePickerHelper(requireContext(),true)
        btSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
        viewModel.timeB.observe(viewLifecycleOwner, {
            tvDate.text =it
            viewModel.getDayIncomeAndExpense(it)
        })
        initView()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            itemsswipetorefresh.isRefreshing = false
        }
    }
}