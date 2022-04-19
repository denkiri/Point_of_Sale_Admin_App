package com.denkiri.pharmacy.ui.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_supplier.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Use the [AddSupplierFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddSupplierFragment : Fragment() {
    private lateinit var viewModel: SupplierViewModel
    // TODO: Rename and change types of parameters
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_supplier, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
        addSupplier.setOnClickListener { addSupplier() }
        back.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, SupplierFragment())
                .commitNow()  }
        observers()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddSupplierFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                AddSupplierFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    private fun validate(): Boolean {
        if (!Validator.isValidName(supplierName)) {
            return false
        }
        if (!Validator.isValidName(contactPerson)) {
            return false
        }
        if (!Validator.isValidPhone(contact)) {
            return false
        }

        return true
    }
    fun  addSupplier(){
        if (validate()) {
            viewModel.addSupplier(supplierName.text.toString(),address.text.toString(),contact.text.toString(),contactPerson.text.toString())
        }

    }
    fun observers(){
        viewModel.observeSupplierAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.suppliers != null && it.data.suppliers!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                 //   val navController = Navigation.findNavController(requireView())
                  //  navController.navigate(R.id.nav_supplier)
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, SupplierFragment())
                            .commitNow()

                }
                else{
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })

    }
    private fun setStatus(data: Resource<SupplierData>) {
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
}