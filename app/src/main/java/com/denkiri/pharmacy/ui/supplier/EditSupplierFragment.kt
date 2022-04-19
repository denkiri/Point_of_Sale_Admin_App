package com.denkiri.pharmacy.ui.supplier

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.Navigation
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.supplier.Supplier
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.edit_supplier_fragment.*
class EditSupplierFragment : Fragment() {
    var supplierName:String?=null
    var supplierId:Int?=null
    var supplierAddress:String?=null
    var contactPerson:String?=null
    var supplierContact:String?=null
    companion object {
        fun newInstance() = EditSupplierFragment()
    }

    private lateinit var viewModel: SupplierViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.edit_supplier_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
        val bundle = arguments
        supplierId=(bundle?.getInt("supplierId"))
        suppliername.setText((bundle?.getString("supplierName")))
        contactperson.setText((bundle?.getString("contactPerson")))
        address.setText((bundle?.getString("supplierAddress")))
        contact.setText((bundle?.getString("supplierContact")))
        editSupplier.setOnClickListener { editSupplier() }
        back.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, SupplierFragment())
                .commitNow()  }
        observers()

    }
    fun  editSupplier(){
        if (validate()) {
            viewModel.editSupplier(suppliername.text.toString(),address.text.toString(),contact.text.toString(),contactperson.text.toString(),supplierId.toString())
        }

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
    private fun validate(): Boolean {
        if (!Validator.isValidName(suppliername)) {
            return false
        }
        if (!Validator.isValidName(contactperson)) {
            return false
        }
        if (!Validator.isValidPhone(contact)) {
            return false
        }


        return true
    }
    fun observers(){
        viewModel.observeSupplierAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.suppliers != null && it.data.suppliers!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                  //  val navController = Navigation.findNavController(requireView())
                   // navController.navigate(R.id.nav_supplier)
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


}