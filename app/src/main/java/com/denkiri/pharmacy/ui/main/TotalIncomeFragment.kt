package com.denkiri.pharmacy.ui.main

import android.app.AlertDialog
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
import com.denkiri.pharmacy.adapters.IncomeAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.expense.Income
import com.denkiri.pharmacy.models.expense.IncomeData
import com.denkiri.pharmacy.models.expense.RecordedExpense
import com.denkiri.pharmacy.models.product.Product
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.utils.RecordedExpenseItemClick
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_total_expenses.*
import kotlinx.android.synthetic.main.fragment_total_income.*
import kotlinx.android.synthetic.main.fragment_total_income.avi
import kotlinx.android.synthetic.main.fragment_total_income.back
import kotlinx.android.synthetic.main.fragment_total_income.collectionTotal
import kotlinx.android.synthetic.main.fragment_total_income.empty_button
import kotlinx.android.synthetic.main.fragment_total_income.empty_layout
import kotlinx.android.synthetic.main.fragment_total_income.fab
import kotlinx.android.synthetic.main.fragment_total_income.itemsswipetorefresh
import kotlinx.android.synthetic.main.fragment_total_income.main
import kotlinx.android.synthetic.main.fragment_total_income.recyclerView
import kotlinx.android.synthetic.main.fragment_total_income.searchView
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [TotalIncomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TotalIncomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: ExpenseViewModel
    lateinit var incomeAdapter: IncomeAdapter
    private lateinit var income: ArrayList<Income>
    private fun initView(){
        income = ArrayList()
        incomeAdapter = context?.let {
            IncomeAdapter(0, it, income, object : RecordedExpenseItemClick {
                override fun delete(pos: Int) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Income")
                    builder.setMessage("Are you sure you want to delete?")
                    builder.setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        viewModel.deleteIncome((income as ArrayList<Income>)[pos].serviceId)
                    }
                    builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()

                }

                override fun edit(pos: Int) {
                    val bundle = Bundle()
                    bundle.putInt( "serviceId" , income[pos].serviceId!!)
                    bundle.putString( "service" , income[pos].service)
                    bundle.putString( "serviceName" , income[pos].name)
                    bundle.putString( "serviceAmount" , income[pos].amount.toString())
                    bundle.putString( "serviceMonth" , income[pos].month)
                    bundle.putString( "serviceDate" , income[pos].date)
                    bundle.putString( "serviceYear" , income[pos].year)
                    bundle.putString( "serviceCashierId" , income[pos].serviceCashier)
                    bundle.putString( "serviceCashierName" , income[pos].cashierName)
                    bundle.putString( "serviceReceiptNumber" , income[pos].receiptNumber)
                    val fragment = EditRecordedIncomeFragment()
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
        recyclerView.adapter = incomeAdapter
        incomeAdapter.notifyDataSetChanged()
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getIncome()
        }
    }
    private fun setUpIncome(income: ArrayList<Income>) {
        this.income = income
        incomeAdapter.refresh(this.income)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<IncomeData>) {
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
        viewModel.observeIncomeData().observe(
            viewLifecycleOwner,
            {
                setStatus(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.income != null && !it.data.income!!.isEmpty()) {
                        setUpIncome(it.data.income as ArrayList<Income>)
                    } else {
                        setUpIncome(ArrayList())
                    }
                }

            })
        viewModel.observeIncomeActionData().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.income != null ) {

                    setUpIncome(it.data.income as ArrayList<Income>)

                } else {

                    setUpIncome(ArrayList())
                }
            }

        })
        viewModel.getTotalIncome().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                if(it.totalIncome!=0.0){
                    collectionTotal.text =format.format(+it.totalIncome).toString()}
            } catch (e: Exception) {
            }
        })
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
        return inflater.inflate(R.layout.fragment_total_income, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TotalIncomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            TotalIncomeFragment().apply {
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
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, RecordIncomeFragment())
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
    fun filter(text: String) {
        val filteredProductAry: ArrayList<Income> = ArrayList()
        val productAry : ArrayList<Income> = income
        for (eachProduct in productAry) {
            if (eachProduct.name!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredProductAry.add(eachProduct)

            }
        }
        //calling a method of the adapter class and passing the filtered list
        incomeAdapter.filterList(filteredProductAry)
        income=filteredProductAry

        incomeAdapter.notifyDataSetChanged()
        if (incomeAdapter.itemCount==0){
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