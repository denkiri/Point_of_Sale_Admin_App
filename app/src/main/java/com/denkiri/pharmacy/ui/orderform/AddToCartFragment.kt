package com.denkiri.pharmacy.ui.orderform

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
import com.denkiri.pharmacy.models.order.OrderData
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.ui.supplier.ui.main.PurchaseOrderFragment
import com.denkiri.pharmacy.ui.supplier.ui.main.PurchaseOrderViewModel
import com.denkiri.pharmacy.utils.DatePickerHelper
import com.denkiri.pharmacy.utils.RoundedBottomSheetDialogFragment
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_to_cart.*
import java.text.NumberFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AddToCartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddToCartFragment : RoundedBottomSheetDialogFragment(){
    lateinit var datePicker: DatePickerHelper
    private lateinit var viewModel: PurchaseOrderViewModel
    var count = 1
    var productCode:String?=null
    var price:String?=null
    var vatValue:String?=null
    var cost:String?=null
    var expirationDate:String?=null
    var descriptionName:String?=null
    var category:String?=null
    var brand:String?=null
    var quantityLeft:String?=null
    var invoiceNumber:String?=null
    var productId:String?=null
    var delivery:String?=null
    var supplierId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datePicker = DatePickerHelper(requireContext(),true)
        arguments?.let {
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PurchaseOrderViewModel::class.java)
        setUpUi()
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val bundle = arguments
        productName.text=bundle?.getString("productName").toString()
        quantityleft.text=bundle?.getString("quantityLeft").toString()
        priceValue.setText((bundle?.getString("price")?.toDouble()).toString())
        costValue.setText(bundle?.getString("cost").toString())
        productCode=bundle?.getString("productCode").toString()
        productId=bundle?.getString("productId").toString()
        expirationDate=bundle?.getString("expirationDate").toString()
        delivery=bundle?.getString("expirationDate").toString()
        supplierId=bundle?.getString("supplierId").toString()
        invoiceNumber= PreferenceManager(requireContext()).getInvoiceNumber()
        add.setOnClickListener {
            count++
            qty.setText(count.toString())}
        remove.setOnClickListener {
            if (qty.text.toString()>"1"){
                count--
                qty.setText(count.toString())}
        }
        cancel.setOnClickListener {
            dismiss()
        }
        datePicker = DatePickerHelper(requireContext(),true)
        btSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
        addToCart.setOnClickListener { addOrder() }
    }
    fun addOrder(){
        if (validate()){
            viewModel.addOrder(priceValue.text.toString(),costValue.text.toString(),qty.text.toString(),delivery.toString(),productCode.toString(),invoiceNumber.toString(),"Received",vat.text.toString(),discount.text.toString())
        }
    }
    private fun status(data: Resource<OrderData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            cancel.visibility=View.INVISIBLE
            addToCart.isEnabled = false
            addToCart.isClickable = false
            addToCart.setBackgroundColor(resources.getColor(R.color.divider))
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            addToCart.isEnabled = true
            addToCart.isClickable = true
            addToCart.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                Toasty.error(requireContext(),data.message.toString(), Toast.LENGTH_LONG, true).show()
                cancel.visibility=View.VISIBLE
            }

        }
    }
    fun setUpUi() {
        viewModel.observeAddOrderAction().observe(viewLifecycleOwner, {
            status(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.order != null && it.data.order!!.isNotEmpty()) {
                    viewModel.saveSupplierId(supplierId)
                    viewModel.saveOrders(it.data)
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, PurchaseOrderFragment())
                            .commitNow()

                    dismiss()


                }
                else{
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG,false).show()

                }

            }

        })
    }

    private fun validate(): Boolean {
        if (!Validator.isValidAmount(qty)) {
            return false
        }
        if(qty.text.toString()=="0"){
            Toasty.error(requireContext(),"Enter Valid Quantity", Toast.LENGTH_LONG, true).show()
            return false

        }
        if (!Validator.isValidAmount(priceValue)) {
            return false
        }
        if (!Validator.isValidAmount(costValue)) {
            return false
        }
        if (!Validator.isValidAmount(discount)) {
            return false
        }
        return true
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
        datePicker.setMinDate(minDate.timeInMillis)
        datePicker.showDialog(y,m,d, object : DatePickerHelper.Callback {
            override fun onDateSelected(year: Int,month: Int,dayofMonth: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                tvDate.text = "${year}-${monthStr}-${dayStr}"
                delivery="${dayStr}-${monthStr}-${year}"
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_cart, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddToCartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                AddToCartFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}