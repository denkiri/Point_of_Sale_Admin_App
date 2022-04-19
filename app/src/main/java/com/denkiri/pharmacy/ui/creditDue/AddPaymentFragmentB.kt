package com.denkiri.pharmacy.ui.creditDue

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
import com.denkiri.pharmacy.models.invoice.InvoiceData
import com.denkiri.pharmacy.ui.ledger.LedgerViewModel
import com.denkiri.pharmacy.ui.products.RemoveProductFragment
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_payment_b.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddPaymentFragmentB.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddPaymentFragmentB : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var invoice:String?=null
    var name:String?=null
    var totalAmount:String?=null
    var balance:String?=null
    private lateinit var viewModel: LedgerViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LedgerViewModel::class.java)
        val bundle = arguments
        invoice=(bundle?.getString("invoiceNumber"))
        name=(bundle?.getString("invoiceNumber"))
        totalAmount=(bundle?.getString("totalAmount"))
        balance=(bundle?.getString("amount"))
        amount.setText((bundle?.getString("amount")))
        back.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, CreditDueFragment())
            .commitNow()
        }
        addPayment.setOnClickListener { addPayment() }
        observers()
    }
    private fun validate(): Boolean {
        if (!Validator.isValidAmount(amount)) {
            return false
        }
        return true
    }
    fun  addPayment(){
        if (validate()) {
            viewModel.addPayment(name.toString(),invoice.toString(),totalAmount.toString(),amount.text.toString(),remarks.text.toString(),balance.toString())
        }

    }
    private fun setStatus(data: Resource<InvoiceData>) {
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
    fun observers(){
        viewModel.observeInvoiceAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {

                    Toasty.success(requireContext(), it.data?.message.toString(), Toast.LENGTH_LONG, true).show()
                   // val navController = Navigation.findNavController(requireView())
                  //  navController.navigate(R.id.nav_ledger)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, CreditDueFragment())
                        .commitNow()

                }



            else{
                Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

            }

        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_payment_b, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddPaymentFragmentB.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AddPaymentFragmentB().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}