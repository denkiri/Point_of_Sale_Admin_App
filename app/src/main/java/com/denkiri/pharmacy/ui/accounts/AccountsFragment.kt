package com.denkiri.pharmacy.ui.accounts
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.*
import com.denkiri.pharmacy.adapters.UserAdapter
import com.denkiri.pharmacy.models.cashier.Users
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.ui.profile.ProfileFragment
import com.denkiri.pharmacy.ui.users.UsersViewModel
import kotlinx.android.synthetic.main.fragment_accounts.*
class AccountsFragment : Fragment() {
    lateinit var userAdapter: UserAdapter
    private lateinit var users: List<Users>
    private lateinit var usersViewModel: UsersViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
        edit.setOnClickListener {   requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileFragment())
                .commitNow() }


        signupback.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Exit")
            builder.setMessage("Are You Sure?")

            builder.setPositiveButton("Yes") { dialog, which ->
                dialog.dismiss()
                PreferenceManager(requireContext()).setLoginStatus(0)
                val mDatabase = PharmacyDatabase.getDatabase(requireContext())
                mDatabase?.clearAllTables()
                activity?.finish()
                startActivity(Intent(context, Splashscreen::class.java))
            }

            builder.setNegativeButton("No", { dialog, which -> dialog.dismiss() })
            val alert = builder.create()
            alert.show()
             }
        setUpUi()
        init()
        card_viewCashierList.setOnClickListener {
            val intent = Intent(activity, AcountsActivity::class.java)
            startActivity(intent)
        }
        card_subscription.setOnClickListener {
            val intent = Intent(activity, SubscriptionActivity::class.java)
            startActivity(intent) }
        card_about_app.setOnClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            startActivity(intent) }
        card_expense.setOnClickListener {
            val intent = Intent(activity, ExpenseActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            AccountsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            usersViewModel.getUsers()
        }
    }


    private fun setUpUsers(users: ArrayList<Users>) {
        this.users = users
        userAdapter.refresh(this.users)
        Handler().postDelayed(Runnable {
        }, 1)
    }

    fun setUpUi() {
        usersViewModel.getOuthProfile().observe(viewLifecycleOwner, {
            try {
                name.setText(it.name)
                email.setText(it.email)
                username.setText(it.username)
                mobile.setText(it.mobile)

            } catch (e: Exception) {
            }
        })
    }
}