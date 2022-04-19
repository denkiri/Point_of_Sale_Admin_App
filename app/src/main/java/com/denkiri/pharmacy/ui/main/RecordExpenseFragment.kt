package com.denkiri.pharmacy.ui.main
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.ExpenseItemAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.expense.ExpenseData
import com.denkiri.pharmacy.models.expense.Expenses
import com.denkiri.pharmacy.models.expense.RecordedExpenseData
import com.denkiri.pharmacy.utils.DatePickerHelper
import com.denkiri.pharmacy.utils.OnExpenseClick
import com.denkiri.pharmacy.utils.Validator
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import es.dmoral.toasty.Toasty.*
import kotlinx.android.synthetic.main.fragment_record_expense.*
import java.util.*
import kotlin.collections.ArrayList

class RecordExpenseFragment : Fragment() {
    private lateinit var viewModel: ExpenseViewModel
    lateinit var datePicker: DatePickerHelper
    lateinit var expenseAdapter: ExpenseItemAdapter
    private lateinit var expenses: ArrayList<Expenses>
    var expenseType:String?=null
    var date:String?=null
    var selectedMonth:String?=null
    var selectedYear:String?=null
    var expenseId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_expense, container, false)
    }
    fun filter(text: String) {
        val filteredExpenseAry: ArrayList<Expenses> = ArrayList()
        val expenseAry : ArrayList<Expenses> = expenses
        for (eachProduct in expenseAry) {
            if (eachProduct.name!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredExpenseAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        expenseAdapter.filterList(filteredExpenseAry)
        expenses=filteredExpenseAry

        expenseAdapter.notifyDataSetChanged()
        if ( expenseAdapter.itemCount==0){
            recyclerView.visibility=View.VISIBLE
        //    Toasty.error(requireContext(), "No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            Snackbar.make(requireView(),"No Matching Search Results", Snackbar.LENGTH_LONG)
            observers()

        }
        else{
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            RecordExpenseFragment().apply {
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
    back.setOnClickListener {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, TotalExpensesFragment())
            .commitNow()
    }
        receiptNumber.setText(viewModel.getRandPassword(8))
        datePicker = DatePickerHelper(requireContext(), true)
        btSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
        record.setOnClickListener {
        recordExpense()
        }
    }
    fun init() {
        viewModel.getActiveExpenses()

    }
    private fun setStatusExpense(data: Resource<ExpenseData>) {
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
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener {init() }
        }
    }
    private fun validate(): Boolean {
        if (!Validator.isValidAmount(amount)) {
            return false
        }
        if (expense.text==""){
            error(requireContext(),"Select Expense", Toast.LENGTH_LONG, false).show()
            return false
        }
        return true
    }
    private fun initView(){
        expenses = ArrayList()
        expenseAdapter = context?.let {
            ExpenseItemAdapter(0, it, expenses, object : OnExpenseClick {
                override fun selected(pos: Int) {
                    (expenses as ArrayList<Expenses>)[pos].name?.let { it1 ->
                        Toasty.success(
                            requireContext(),
                            it1,
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    }
                    expenseType = (expenses as ArrayList<Expenses>)[pos].name.toString()
                    expense.text = (expenses as ArrayList<Expenses>)[pos].name.toString()
                    expenseId=(expenses as ArrayList<Expenses>)[pos].id.toString()
                    amount.setText((expenses as ArrayList<Expenses>)[pos].amount.toString())

                }

                override fun delete(pos: Int) {


                }


                override fun onClickListener(pos: Int) {

                }

            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = expenseAdapter
        expenseAdapter.notifyDataSetChanged()
    }
    fun recordExpense(){
        if (validate()){
            viewModel.recordExpense(expenseId.toString(),amount.text.toString(),selectedMonth.toString(),tvDate.text.toString(),selectedYear.toString(),payee.text.toString(),receiptNumber.text.toString())
        }
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
                date = "${monthStr}/${dayStr}/${year}"
                selectedYear= "$year"
                if (monthStr=="01"){
                selectedMonth="January"
                }
                if (monthStr=="02"){
                    selectedMonth="February"
                }
                if (monthStr=="03"){
                    selectedMonth="March"
                }
                if (monthStr=="04"){
                    selectedMonth="April"
                }
                if (monthStr=="05"){
                    selectedMonth="May"
                }
                if (monthStr=="06"){
                    selectedMonth="June"
                }
                if (monthStr=="07"){
                    selectedMonth="July"
                }
                if (monthStr=="08"){
                    selectedMonth="August"
                }
                if (monthStr=="09"){
                    selectedMonth="September"
                }
                if (monthStr=="10"){
                    selectedMonth="October"
                }
                if (monthStr=="11"){
                    selectedMonth="November"
                }
                if (monthStr=="12"){
                    selectedMonth="December"
                }
            }
        })
    }


    private fun setUpExpenses(expenses: ArrayList<Expenses>) {
        this.expenses = expenses
        expenseAdapter.refresh(this.expenses)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    fun observers() {
        viewModel.observeExpenses().observe(
            viewLifecycleOwner,
            {
                setStatusExpense(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.expenses != null && !it.data.expenses!!.isEmpty()) {
                        setUpExpenses(it.data.expenses as ArrayList<Expenses>)
                    } else {
                        setUpExpenses(ArrayList())
                    }
                }

            })
        viewModel.observeRecordedExpenseAction().observe(viewLifecycleOwner, {
            status(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.expenses!= null ) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                    //    val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_product)
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, TotalExpensesFragment())
                            .commitNow()

                }
                if (it.status == Status.ERROR){
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })
    viewModel.timeB.observe(viewLifecycleOwner,{
        tvDate.text=it

    })
        viewModel.month.observe(viewLifecycleOwner,{
       selectedMonth=it


        })
        viewModel.year.observe(viewLifecycleOwner,{
            selectedYear= it

        })

    }
    private fun status(data: Resource<RecordedExpenseData>) {
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