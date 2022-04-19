package com.denkiri.pharmacy.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.cashier.UsersData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.ui.accounts.AccountsFragment
import com.denkiri.pharmacy.ui.cashier.CashierFragment
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_user.*
/**
 * A simple [Fragment] subclass.
 * Use the [AddUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddUserFragment : Fragment() {
    private lateinit var usersViewModel: UsersViewModel
    var businessCode:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_user, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                AddUserFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    private fun setStatus(data: Resource<UsersData>) {
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
                //view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
                Toasty.error(requireContext(), data.message.toString(), Toast.LENGTH_LONG, true).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)

        add.setOnClickListener { addUser() }
        observers()
    }
    private fun validate(): Boolean {
        if (!Validator.isValidName(username)) {
            return false
        }
        if (!Validator.isValidName(name)) {
            return false
        }
        if (!Validator.isValidPassword(password)) {
            return false
        }

        return true
    }
    fun  addUser(){
        if (validate()) {
            usersViewModel .addUser(username.text.toString(),password.text.toString(),name.text.toString(),businessCode.toString())
        }

    }
    fun observers(){
        usersViewModel.observeUserAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.cashier != null && it.data.cashier!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                    //   val navController = Navigation.findNavController(requireView())
                    //   navController.navigate(R.id.nav_category)
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, CashierFragment())
                            .commitNow()

                }
                else{
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })
        usersViewModel.getOuthProfile().observe(viewLifecycleOwner, {
            try {
                businessCode= it.businessCode.toString()
            } catch (e: Exception) {
            }
        })

    }
}