package com.denkiri.pharmacy.ui.orderform

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.order.PurchaseOrderData
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.ui.supplier.InvoiceActivity
import com.denkiri.pharmacy.ui.supplier.ui.main.PurchaseOrderViewModel
import com.denkiri.pharmacy.utils.DatePickerHelper
import com.denkiri.pharmacy.utils.RoundedBottomSheetDialogFragment
import com.denkiri.pharmacy.utils.Validator
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_to_cart.*
import kotlinx.android.synthetic.main.fragment_save_invoice.*
import kotlinx.android.synthetic.main.fragment_save_invoice.avi
import kotlinx.android.synthetic.main.fragment_save_invoice.btSelectDate
import kotlinx.android.synthetic.main.fragment_save_invoice.cancel
import kotlinx.android.synthetic.main.fragment_save_invoice.empty_layout
import kotlinx.android.synthetic.main.fragment_save_invoice.empty_text
import kotlinx.android.synthetic.main.fragment_save_invoice.main
import kotlinx.android.synthetic.main.fragment_save_invoice.tvDate
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SaveInvoiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaveInvoiceFragment :  RoundedBottomSheetDialogFragment(){
    var radio: RadioButton?=null
    var invoice:String?=null
    var supplier:String?=null
    lateinit var datePicker: DatePickerHelper
    private lateinit var viewModel: PurchaseOrderViewModel
    var due:String?=null
    var delivery:String?=null
    // TODO: Rename and change types of parameters
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datePicker = DatePickerHelper(requireContext(),true)
        arguments?.let {

        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radioPaymentType.setOnCheckedChangeListener { _, checkedId ->
            radio = view.findViewById(checkedId)
        }
        var id: Int = radioPaymentType.checkedRadioButtonId
        if (id!=-1){ // If any radio button checked from radio group
            // Get the instance of radio button using id
            radio = view.findViewById(id)


        }else{
            // If no radio button checked in this radio group
            Toast.makeText(context,"Select Payment Status",
                    Toast.LENGTH_SHORT).show()
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_invoice, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PurchaseOrderViewModel::class.java)
        val bundle = arguments
        supplier=bundle?.getString("supplier").toString()
        invoice= PreferenceManager(requireContext()).getInvoiceNumber()
        datePicker = DatePickerHelper(requireContext(),true)
        btSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
        btSelectDate2.setOnClickListener {
            showDatePickerDialog2()
        }
        save.setOnClickListener { saveInvoice() }
        cancel.setOnClickListener {
            dismiss()
        }
        observer()
    }
    private fun validate(): Boolean {
        if (!Validator.isValidInvoice(invoiceNumber)) {
            return false
        }
        if (delivery==null){
            Toasty.error(requireContext(),"Select Date Issued", Toast.LENGTH_LONG, false).show()
            return false
        }
        return true
    }
    fun saveInvoice(){
        if (validate()) {
            if (due==null && radio!!.text=="Pending"){
                Toasty.error(requireContext(),"Set Due Date", Toast.LENGTH_LONG, false).show()
                   }
        else{
                viewModel.savePurchasesInvoice(invoice.toString(), supplier.toString(), delivery.toString(), due.toString(), invoiceNumber.text.toString(), radio?.text.toString())

            }
        }

    }
    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        val minDate = Calendar.getInstance()
        minDate.set(Calendar.HOUR_OF_DAY, 0)
        minDate.set(Calendar.MINUTE, 0)
        minDate.set(Calendar.SECOND, 0)
      // datePicker.setMinDate(minDate.timeInMillis)
        datePicker.showDialog(y,m,d, object : DatePickerHelper.Callback {
            override fun onDateSelected(year: Int,month: Int,dayofMonth: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                tvDate.text = "${year}-${monthStr}-${dayStr}"
                due="${dayStr}-${monthStr}-${year}"
            }
        })
    }
    private fun setStatus(data: Resource<PurchaseOrderData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            cancel.visibility=View.INVISIBLE
            save.isEnabled = false
            save.isClickable = false
            save.setBackgroundColor(resources.getColor(R.color.divider))
            activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            save.isEnabled = true
            save.isClickable = true
            save.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                cancel.visibility=View.VISIBLE
                empty_text.text = data.message
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }

            }

            //   empty_layout.visibility = View.VISIBLE
            //   main.visibility = View.GONE
            //    empty_button.setOnClickListener {
            //     saveSale()
            // }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SaveInvoiceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                SaveInvoiceFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    private fun showDatePickerDialog2() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        val minDate = Calendar.getInstance()
        minDate.set(Calendar.HOUR_OF_DAY, 0)
        minDate.set(Calendar.MINUTE, 0)
        minDate.set(Calendar.SECOND, 0)
       // datePicker.setMinDate(minDate.timeInMillis)
        datePicker.showDialog(y,m,d, object : DatePickerHelper.Callback {
            override fun onDateSelected(year: Int,month: Int,dayofMonth: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                tvDate2.text = "${year}-${monthStr}-${dayStr}"
                delivery="${dayStr}-${monthStr}-${year}"
            }
        })
    }
    fun observer(){
        viewModel.observePurchasesOrder().observe(
                viewLifecycleOwner,
                {
                    setStatus(it)
                    if (it.status == Status.SUCCESS) {
                        viewModel.saveInvoiceNumber("")
                        dismiss()
                        //  requireActivity().supportFragmentManager.beginTransaction()
                        // .replace(R.id.container, HomeFragment())
                        //  .commitNow()
                        // val intent = Intent(context, MainActivity::class.java)
                        //  startActivity(intent)
                        val intent = Intent(activity, InvoiceActivity::class.java)
                        intent.putExtra("id",invoice)
                        intent.putExtra("supplier",supplier)
                        startActivity(intent)

                    }
                    if (it.status == Status.ERROR) {
                        Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG,false).show()
                    }
                })

    }
}