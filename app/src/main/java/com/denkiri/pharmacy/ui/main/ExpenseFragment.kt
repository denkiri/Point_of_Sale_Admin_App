package com.denkiri.pharmacy.ui.main
import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.denkiri.pharmacy.MainActivity
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.expense.ExpenseReportData
import com.denkiri.pharmacy.models.expense.IncomeReportData
import com.denkiri.pharmacy.network.NetworkUtils
import kotlinx.android.synthetic.main.expense_fragment.*
import java.text.NumberFormat
import java.util.*
class ExpenseFragment : Fragment() {
    companion object {
        fun newInstance() = ExpenseFragment()
    }

    private lateinit var viewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.expense_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        init()
        observers()
        viewModel.year.observe(viewLifecycleOwner, {
            yearText.text =it
        })
        viewModel.month.observe(viewLifecycleOwner, {
            monthText.text =it
        })
        viewModel.time.observe(viewLifecycleOwner, {
            dateText.text =it
        })
        card_viewF.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, ExpenseCategoryFragment())
            .commitNow() }
        card_viewE.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, TotalExpensesFragment())
                .commitNow()
        }
        card_viewD.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, TotalIncomeFragment())
                .commitNow()
        }
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            //  observers()
            //   setUpUi()
            itemsswipetorefresh.isRefreshing = false
        }
        card_view_total.setOnClickListener {
            startActivity(Intent(activity, ExpenseIncomeActivity::class.java))
            activity?.finish()
        }
        back.setOnClickListener { activity?.finish() }
    }
    private fun setStatus(data: Resource<ExpenseReportData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
          //  activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
          //  activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                 empty_text.text = data.message
                //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    private fun setStatusB(data: Resource<IncomeReportData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
          //  activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
           // activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                empty_text.text = data.message
                //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    fun init() {

        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getExpenseReport()
            viewModel.getIncomeReport()

        }
    }
    fun observers(){
        viewModel.observeExpenseReport().observe(viewLifecycleOwner, { data ->
            run {
                setStatus(data)
            }
        })
        viewModel.observeIncomeReport().observe(viewLifecycleOwner, { data ->
            run {
                setStatusB(data)
            }
        })
        viewModel.getDayExpense().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    expensesText.text = format.format(-it.dayExpense).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getMonthlyExpense().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    expensesTextB.text = format.format(-it.monthlyExpense).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getYearExpense().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    expensesTextC.text =format.format(-it.yearlyExpense).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getDayIncome().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    incomeText.text = format.format(+it.dayIncome).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getMonthlyIncome().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    expensesTextD.text = format.format(+it.monthlyIncome).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getYearIncome().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    expensesTextE.text =format.format(+it.yearlyIncome).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getDayNetProfit().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    netProfit.text =format.format(+it.dayNetProfit).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getMonthlyNetProfit().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    netMonthlyProfit.text =format.format(+it.monthlyNetProfit).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getYearlyNetProfit().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    netYearlyProfit.text =format.format(+it.yearlyNetReport).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getTotalIncome().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    totalIncomeText.text =format.format(+it.totalIncome).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getTotalExpenses().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    expenses2Text.text =format.format(+it.totalExpense).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.getNetProfit().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                    totalProfit.text =format.format(+it.netIncome).toString()

            } catch (e: Exception) {
            }
        })
    }




}