package com.denkiri.pharmacy.ui.customers
import android.app.AlertDialog
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
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.CustomerAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.customer.Customer
import com.denkiri.pharmacy.models.customer.CustomerData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.ui.categories.AddCategoryFragment
import com.denkiri.pharmacy.ui.categories.EditFragment
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.denkiri.pharmacy.utils.OnCustomerItemClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.customer_fragment.*
import kotlinx.android.synthetic.main.customer_fragment.avi
import kotlinx.android.synthetic.main.customer_fragment.back
import kotlinx.android.synthetic.main.customer_fragment.empty_button
import kotlinx.android.synthetic.main.customer_fragment.empty_layout
import kotlinx.android.synthetic.main.customer_fragment.fab
import kotlinx.android.synthetic.main.customer_fragment.itemsswipetorefresh
import kotlinx.android.synthetic.main.customer_fragment.main
import kotlinx.android.synthetic.main.customer_fragment.noResults
import kotlinx.android.synthetic.main.customer_fragment.productsTotal
import kotlinx.android.synthetic.main.customer_fragment.recyclerView
import kotlinx.android.synthetic.main.customer_fragment.searchView
import kotlinx.android.synthetic.main.fragment_supplier_products.*

class CustomerFragment : Fragment() {
    lateinit var customerAdapter: CustomerAdapter
    private lateinit var customers: ArrayList<Customer>

    companion object {
        fun newInstance() = CustomerFragment()
    }

    private lateinit var viewModel: CustomerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.customer_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)
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
        back.setOnClickListener {    requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commitNow() }
        fab.setOnClickListener {
           // val navController = Navigation.findNavController(requireView())
          //  navController.navigate(R.id.nav_add_customer)
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, AddCustomerFragment())
                    .commitNow()
        }

        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
         //   observers()
         //   initView()
            itemsswipetorefresh.isRefreshing = false
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
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
        viewModel.getCustomer()
    }}
    private fun setUpCustomers(customers: ArrayList<Customer>) {
        this.customers = customers
        productsTotal.text= customers.size.toString()
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
                fab.visibility=View.GONE
               // empty_text.text = data.message
              //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    private fun setStatusB(data: Resource<CustomerData>) {
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
                fab.visibility=View.GONE
                // empty_text.text = data.message
                  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

         //   empty_layout.visibility = View.VISIBLE
            main.visibility = View.VISIBLE
        //    empty_button.setOnClickListener({ init() })
        }
    }
    private fun initView(){
        customers = ArrayList()
        customerAdapter = context?.let {
            CustomerAdapter(0, it, customers, object : OnCustomerItemClick {
                override fun delete(pos: Int) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Customer")
                    builder.setMessage("Are You Sure you want to delete?")
                    builder.setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        viewModel.deleteCustomer(customers[pos].customerId,customers[pos].customerName)
                    }
                    builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()
                }
                override fun edit(pos: Int) {
                    val bundle = Bundle()
                    bundle.putInt( "customerId" , (customers as ArrayList<Customer>)[pos].customerId)
                    bundle.putString( "firstName" , (customers as ArrayList<Customer>)[pos].firstName)
                    bundle.putString( "middleName" , (customers as ArrayList<Customer>)[pos].middleName)
                    bundle.putString( "lastName" , (customers as ArrayList<Customer>)[pos].lastName)
                    bundle.putString( "address" , (customers as ArrayList<Customer>)[pos].address)
                    bundle.putString( "contact" , (customers as ArrayList<Customer>)[pos].contact)
                    bundle.putString( "name" , (customers as ArrayList<Customer>)[pos].customerName)
                    bundle.putString( "code" , (customers as ArrayList<Customer>)[pos].membershipNumber)
                  //  val navController = Navigation.findNavController(requireView())
                 //   navController.navigate(R.id.nav_edit_customer, bundle)
                    val fragment = EditCustomerFragment()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commitNow()

                }

                override fun dial(pos: Int) {

                }

                override fun enable(pos: Int) {
                    viewModel.activateCustomer(customers[pos].customerId)

                }

                override fun disable(pos: Int) {
                    viewModel.deactivateCustomer(customers[pos].customerId)

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
        viewModel.observeCustomerAction().observe(
            viewLifecycleOwner,
            {
                setStatusB(it)
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
            Toasty.error(requireContext(),"No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            noResults.visibility=View.INVISIBLE
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }

}