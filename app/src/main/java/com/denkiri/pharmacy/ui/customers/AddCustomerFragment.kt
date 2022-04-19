package com.denkiri.pharmacy.ui.customers
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.customer.CustomerData
import com.denkiri.pharmacy.ui.categories.AddCategoryFragment
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_customer.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddCustomerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCustomerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var customerViewModel: CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        customerViewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)
        addCustomer.setOnClickListener { addCustomer() }
        back.setOnClickListener {   requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container,CustomerFragment())
                .commitNow() }
        observers()
    }
    private fun setStatus(data: Resource<CustomerData>) {
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
    private fun validate(): Boolean {
        if (!Validator.isValidName(firstName)) {
            return false
        }
        if (!Validator.isValidName(middleName)) {
            return false
        }
        if (!Validator.isValidName(lastName)) {
            return false
        }
        if (!Validator.isValidPhone(contact)) {
            return false
        }
        return true
    }
    fun  addCustomer(){
        if (validate()) {
            customerViewModel.addCustomer(firstName.text.toString(),middleName.text.toString(),lastName.text.toString(),address.text.toString(),contact.text.toString())
        }

    }
    fun observers(){
        customerViewModel.observeCustomerAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.customers != null && it.data.customers!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                //    val navController = Navigation.findNavController(requireView())
                  //  navController.navigate(R.id.nav_customer)
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, CustomerFragment())
                            .commitNow()

                }
                else{
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_customer, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddCustomerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AddCustomerFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}