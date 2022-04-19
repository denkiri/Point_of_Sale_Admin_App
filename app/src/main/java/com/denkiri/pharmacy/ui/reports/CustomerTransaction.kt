package com.denkiri.pharmacy.ui.reports

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.CustomerAdapter
import com.denkiri.pharmacy.adapters.CustomerTransactionAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.customer.Customer
import com.denkiri.pharmacy.models.customer.CustomerData
import com.denkiri.pharmacy.ui.categories.EditFragment
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.customer_fragment.*
import kotlinx.android.synthetic.main.customer_fragment.avi
import kotlinx.android.synthetic.main.customer_fragment.empty_button
import kotlinx.android.synthetic.main.customer_fragment.empty_layout
import kotlinx.android.synthetic.main.customer_fragment.itemsswipetorefresh
import kotlinx.android.synthetic.main.customer_fragment.main
import kotlinx.android.synthetic.main.customer_fragment.noResults
import kotlinx.android.synthetic.main.customer_fragment.recyclerView
import kotlinx.android.synthetic.main.customer_fragment.searchView
import kotlinx.android.synthetic.main.expired_products_fragment.*

class CustomerTransaction : Fragment() {
    lateinit var customerAdapter: CustomerTransactionAdapter
    private lateinit var customers: ArrayList<Customer>
    companion object {
        fun newInstance() = CustomerTransaction()
    }

    private lateinit var viewModel: CustomerTransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.customer_transaction_fragment, container, false)
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
    fun init() {
        viewModel.getCustomer()
    }
    private fun setUpCustomers(customers: ArrayList<Customer>) {
        this.customers = customers
        customerAdapter.refresh(this.customers)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<CustomerData>) {
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
               // empty_text.text = data.message
               // view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    private fun initView(){
        customers = ArrayList()
        customerAdapter = context?.let {
            CustomerTransactionAdapter(0, it, customers, object : OnCategoryItemClick {
                override fun delete(pos: Int) {
                }
                override fun edit(pos: Int) {
                    val bundle = Bundle()
                    bundle.putString( "name" , customers[pos].customerName)
                   // val navController = Navigation.findNavController(requireView())
                  //  navController.navigate(R.id.nav_edit_customer, bundle)
                    val fragment = Transactions()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commitNow()
                }

                override fun dial(pos: Int) {
                    TODO("Not yet implemented")
                }

                override fun onClickListener(position1: Int) {

                }

                override fun onLongClickListener(position1: Int) {
                    TODO("Not yet implemented")
                }
            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = customerAdapter
        customerAdapter.notifyDataSetChanged()
    }
    fun observers() {
        viewModel.observeCustomer().observe(
                viewLifecycleOwner,
                {
                    setStatus(it)
                    if (it.status == Status.SUCCESS) {
                        if (it.data?.customers != null && !it.data.customers!!.isEmpty()) {
                            setUpCustomers(it.data.customers as ArrayList<Customer>)
                        } else {
                            setUpCustomers(ArrayList())
                        }
                    }

                })

    }
    fun filter(text: String) {
        val filteredCustomerAry: ArrayList<Customer> = ArrayList()
        val customerAry : ArrayList<Customer> = customers
        for (eachProduct in customerAry) {
            if (eachProduct.customerName!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredCustomerAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        customerAdapter.filterList(filteredCustomerAry)
        customers=filteredCustomerAry

        customerAdapter.notifyDataSetChanged()
        if (customerAdapter.itemCount==0){
            noResults.visibility=View.INVISIBLE
            recyclerView.visibility=View.VISIBLE
           // Toasty.error(requireContext(),"No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            noResults.visibility=View.INVISIBLE
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomerTransactionViewModel::class.java)
        initView()
        init()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            viewModel = ViewModelProvider(this).get(CustomerTransactionViewModel::class.java)
         //   initView()
            init()
          //  observers()
            itemsswipetorefresh.isRefreshing = false
        }
    }

}