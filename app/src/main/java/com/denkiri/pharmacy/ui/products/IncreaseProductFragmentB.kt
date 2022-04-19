package com.denkiri.pharmacy.ui.products

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
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.ui.reorder.ReorderFragment
import com.denkiri.pharmacy.utils.DatePickerHelper
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_increase_product_b.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IncreaseProductFragmentB.newInstance] factory method to
 * create an instance of this fragment.
 */
class IncreaseProductFragmentB : Fragment() {
    private lateinit var productsViewModel: ProductsViewModel
    lateinit var datePicker: DatePickerHelper
    var expDate:String?=null
    var productId:Int?=null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_increase_product_b, container, false)
    }
    private fun validate(): Boolean {
        if (!Validator.isValidName(productName)) {
            return false
        }
        if (!Validator.isValidPrice(cost)) {
            return false
        }
        if (!Validator.isValidPrice(price)) {
            return false
        }
        if (!Validator.isValidAmount(qty)) {
            return false
        }
        if (expDate=="null"){
            Toasty.error(requireContext(),"Set Expiry Date", Toast.LENGTH_SHORT, false).show()
            return false
        }


        return true
    }
    fun increaseProduct(){
        if (validate()){
            productsViewModel.increaseProduct(price.text.toString(),cost.text.toString(),qty.text.toString(),expDate.toString(),productId.toString().trim())

        }
    }
    fun observers(){
        productsViewModel.observeProductsAction().observe(viewLifecycleOwner, {
            status(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.products != null && it.data.products!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                    //    val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_product)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ReorderFragment())
                        .commitNow()

                }
                if (it.status == Status.ERROR){
                    //  avi.visibility = View.GONE
                    // activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })
    }
    private fun status(data: Resource<ProductData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
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
                Toasty.error(requireContext(),data.message.toString(), Toast.LENGTH_LONG, true).show()
            }

        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IncreaseProductFragmentB.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IncreaseProductFragmentB().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        productsViewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        observers()
        val bundle = arguments
        productId=(bundle?.getInt("productId"))
        productName.setText((bundle?.getString("productName")))
        cost.setText((bundle?.getDouble("cost")).toString())
        price.setText((bundle?.getDouble("price")).toString())
        tvDate.text=(bundle?.getString("date"))
        expDate=(bundle?.getString("date"))
        back.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, ReorderFragment())
            .commitNow()  }
        datePicker = DatePickerHelper(requireContext(),true)
        btSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
        increaseProduct.setOnClickListener { increaseProduct()
            //   avi.visibility = View.VISIBLE
            //    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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
        datePicker.setMinDate(minDate.timeInMillis)
        datePicker.showDialog(y,m,d, object : DatePickerHelper.Callback {
            override fun onDateSelected(year: Int,month: Int,dayofMonth: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                tvDate.text = "${year}-${monthStr}-${dayStr}"
                expDate="${dayStr}-${monthStr}-${year}"
            }
        })
    }
}