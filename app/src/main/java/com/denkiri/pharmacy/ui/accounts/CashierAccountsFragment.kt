package com.denkiri.pharmacy.ui.accounts
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.reports.incomeReport.CashierIncomeReportData
import com.denkiri.pharmacy.ui.cashier.CashierFragment
import com.denkiri.pharmacy.ui.main.ExpenseViewModel
import com.denkiri.pharmacy.ui.supplier.SupplierFragment
import kotlinx.android.synthetic.main.fragment_cashier_accounts.*
import kotlinx.android.synthetic.main.fragment_cashier_accounts.avi
import kotlinx.android.synthetic.main.fragment_cashier_accounts.empty_button
import kotlinx.android.synthetic.main.fragment_cashier_accounts.empty_layout
import kotlinx.android.synthetic.main.fragment_cashier_accounts.itemsswipetorefresh
import kotlinx.android.synthetic.main.fragment_cashier_accounts.main
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.NumberFormat
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Use the [CashierAccountsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CashierAccountsFragment : Fragment() {
     private lateinit var incomeViewModel: ExpenseViewModel
    var userId:Int?=null
    // TODO: Rename and change types of parameters
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    fun init() {
        incomeViewModel.getCashierIncome(userId.toString())
    }
    private fun setStatus(data: Resource<CashierIncomeReportData>) {
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
                //fab.visibility=View.GONE
                // empty_text.text = data.message
                //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cashier_accounts, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CashierAccountsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CashierAccountsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        incomeViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        val bundle = arguments
        userId=(bundle?.getInt("userId"))
        init()
        observers()
        incomeViewModel.year.observe(viewLifecycleOwner, {
            yearText.text =it
        })
        incomeViewModel.month.observe(viewLifecycleOwner, {
            monthText.text =it
        })
        incomeViewModel.time.observe(viewLifecycleOwner, {
            dateText.text =it
        })
         back.setOnClickListener {
             requireActivity().supportFragmentManager.beginTransaction()
             .replace(R.id.container, CashierFragment())
             .commitNow() }
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            itemsswipetorefresh.isRefreshing = false
        }

    }
    fun observers(){
        incomeViewModel.observeCashierIncomeData().observe(viewLifecycleOwner, { data ->
            run {
                setStatus(data)
            }
        })
        incomeViewModel.getDayCashierIncome().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                incomeText.text = format.format(it.dayIncome).toString()

            } catch (e: Exception) {
            }
        })
        incomeViewModel.getCashierMonthlyIncome().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                expensesTextD.text = format.format(it.monthIncome).toString()

            } catch (e: Exception) {
            }
        })
        incomeViewModel.getCashierLastMonthIncome().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                expensesTextE.text = format.format(it.lastMonthIncome).toString()

            } catch (e: Exception) {
            }
        })
        incomeViewModel.getCashierYearIncome().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))

                totalIncomeText.text = format.format(it.yearIncome).toString()

            } catch (e: Exception) {
            }
        })




    }
}