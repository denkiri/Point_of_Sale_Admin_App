package com.denkiri.pharmacy.ui.cashier

import android.app.AlertDialog
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.UserAdapter
import com.denkiri.pharmacy.models.cashier.Users
import com.denkiri.pharmacy.models.cashier.UsersData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.ui.accounts.CashierAccountsFragment
import com.denkiri.pharmacy.ui.supplier.SupplierFragment
import com.denkiri.pharmacy.ui.users.AddUserFragment
import com.denkiri.pharmacy.ui.users.EditUserFragment
import com.denkiri.pharmacy.ui.users.UsersViewModel
import com.denkiri.pharmacy.utils.OnCustomerItemClick
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.cashier_fragment.*

class CashierFragment : Fragment() {
    lateinit var userAdapter: UserAdapter
    private lateinit var users: List<Users>
    private lateinit var usersViewModel: UsersViewModel
    var userTotal:Int?=null
    companion object {
        fun newInstance() = CashierFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.cashier_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        usersViewModel= ViewModelProvider(this).get(UsersViewModel::class.java)
        initView()
        init()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            //  initView()
            //  observers()
            itemsswipetorefresh.isRefreshing = false
        }
        fab.setOnClickListener {
            if (userTotal==10){
                view?.let { Snackbar.make(it,"Maximum Number of Users", Snackbar.LENGTH_LONG).show() }
            }
else{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddUserFragment())
                .commitNow()
        }
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
        back.setOnClickListener {  }
    }
    private fun setStatus(data: Resource<UsersData>) {
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
    fun init() {

            usersViewModel.getUsers()

    }
    private fun initView(){
        users = ArrayList()
        userAdapter = context?.let {
            UserAdapter(0, it, users, object : OnCustomerItemClick {
                override fun delete(pos: Int) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Cashier")
                    builder.setMessage("Are You Sure you want to delete?")
                    builder.setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                       // usersViewModel.deleteUser(users[pos].cashierId)
                    }
                    builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()
                }
                override fun edit(pos: Int) {
                    val bundle = Bundle()
                    bundle.putInt( "userId" , (users as ArrayList<Users>)[pos].cashierId)
                    bundle.putString( "username" , (users as ArrayList<Users>)[pos].username)
                    bundle.putString( "name" , (users as ArrayList<Users>)[pos].cashierName)
                    bundle.putString( "password" , (users as ArrayList<Users>)[pos].password)
                    val fragment = EditUserFragment()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow()

                }

                override fun dial(pos: Int) {

                }

                override fun enable(pos: Int) {
                    // viewModel.activateCustomer(customers[pos].customerId)

                }

                override fun disable(pos: Int) {
                    //     viewModel.deactivateCustomer(customers[pos].customerId)

                }

                override fun onClickListener(position1: Int) {
                    val bundle = Bundle()
                    bundle.putInt( "userId" , (users as ArrayList<Users>)[position1].cashierId)
                    val fragment = CashierAccountsFragment()
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
        recyclerView.adapter = userAdapter
        userAdapter.notifyDataSetChanged()
    }
    private fun setUpUsers(users: ArrayList<Users>) {
        this.users = users
        userTotal=users.size
        userAdapter.refresh(this.users)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatusB(data: Resource<UsersData>) {
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
    fun observers() {
        usersViewModel.observeUserAction().observe(
            viewLifecycleOwner,
            {
                setStatusB(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.cashier!= null && !it.data.cashier!!.isEmpty()) {
                        setUpUsers(it.data.cashier as ArrayList<Users>)
                    } else {
                        setUpUsers(ArrayList())
                    }
                }

            })
        usersViewModel.observeUsers().observe(
            viewLifecycleOwner,
            {
                setStatus(it)
                if (it.status == Status.SUCCESS) {
                    if (it.data?.cashier != null && it.data.cashier!!.isNotEmpty()) {
                        setUpUsers(it.data.cashier as ArrayList<Users>)
                    } else {
                        setUpUsers(ArrayList())
                    }
                }

            })

    }
}