package com.denkiri.pharmacy.ui.products

import android.app.AlertDialog
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
import com.denkiri.pharmacy.ui.expiry.ProductExpiryFragment
import com.denkiri.pharmacy.ui.expiry.ProductExpiryViewModel
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_remove_stock.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RemoveStock.newInstance] factory method to
 * create an instance of this fragment.
 */
class RemoveStock : Fragment() {
    private lateinit var viewModel: ProductExpiryViewModel
    var name:String?=null
    var code:String?=null
    var cost:String?=null
    var qty:String?=null
    var description:String?=null
    var category:String?=null
    var expDate:String?=null
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductExpiryViewModel::class.java)
        observers()
        val bundle = arguments
        // Toasty.error(requireContext(),(bundle?.getInt("productId")).toString(), Toast.LENGTH_LONG, true).show()
       code=(bundle?.getString("code"))
        category=(bundle?.getString("category"))
        productName.setText((bundle?.getString("productName")))
        name=(bundle?.getString("productName"))
        description=(bundle?.getString("productDescription"))
        cost=(bundle?.getDouble("cost")).toString()
        expDate= (bundle?.getString("date"))
        quantity.setText((bundle?.getInt("qleft")).toString())
        qty=(bundle?.getInt("qleft")).toString()

        removeStock.setOnClickListener { removeStock() }
        back.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductExpiryFragment())
                .commitNow()  }
    }
    private fun validate(): Boolean {
        if (!Validator.isValidName(productName)) {
            return false
        }
        if(quantity.text.toString().toInt() > qty.toString().toInt()!!){

            Toasty.error(requireContext(),"Excess Quantity", Toast.LENGTH_LONG, false).show()
            return false
        }


        return true
    }
    private fun status(data: Resource<ProductData>) {
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
    fun observers(){
        viewModel.observeProductsAction().observe(viewLifecycleOwner, {
            status(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.products != null && it.data.products!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                    //    val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_product)
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, ProductExpiryFragment())
                            .commitNow()

                }
                if (it.status == Status.ERROR){
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })

    }
    fun removeStock(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Remove Stock")
        builder.setMessage("Are you sure you want to remove these items?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            if (validate()) {
                viewModel.pullOutProduct(code.toString(), name.toString(), description.toString(), cost.toString(), quantity.text.toString(), category.toString(), expDate.toString())
            }

        }

        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remove_stock, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RemoveStock.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                RemoveStock().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}