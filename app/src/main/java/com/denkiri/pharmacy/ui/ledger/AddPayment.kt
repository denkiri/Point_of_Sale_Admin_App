package com.denkiri.pharmacy.ui.ledger
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
import com.denkiri.pharmacy.ui.creditDue.AddPaymentFragmentB
import com.denkiri.pharmacy.utils.Validator
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_payment.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Use the [AddPayment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddPayment : Fragment() {
    var invoice:String?=null
    var name:String?=null
    var totalAmount:String?=null
    var balance:Double?=null
    private lateinit var viewModel: LedgerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_payment, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddPayment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AddPayment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LedgerViewModel::class.java)
        val bundle = arguments
        invoice=(bundle?.getString("invoiceNumber"))
        name=(bundle?.getString("invoiceNumber"))
        totalAmount=(bundle?.getString("totalAmount"))
        balance=(bundle?.getDouble("amount"))
        amount.setText((bundle?.getDouble("amount")).toString())
        addPayment.setOnClickListener { addPayment() }
        observers()
        back.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, LedgerFragment())
                .commitNow()
        }

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
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }

            }
        }
    }
    fun observers(){
        viewModel.observeInvoiceAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                    Toasty.success(requireContext(), it.data?.message.toString(), Toast.LENGTH_LONG, true).show()
                  //  val navController = Navigation.findNavController(requireView())
                  //  navController.navigate(R.id.nav_ledger)
                    val fragment = LedgerFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commitNow()





            }


        })

    }

}