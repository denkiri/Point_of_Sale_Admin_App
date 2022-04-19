package com.denkiri.pharmacy.ui.brand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.brand.BrandData
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.ui.categories.CategoriesFragment
import com.denkiri.pharmacy.ui.categories.CategoryViewModel
import com.denkiri.pharmacy.ui.categories.EditFragment
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_edit_brand.*
import kotlinx.android.synthetic.main.fragment_edit_category.*
import kotlinx.android.synthetic.main.fragment_edit_category.avi
import kotlinx.android.synthetic.main.fragment_edit_category.back
import kotlinx.android.synthetic.main.fragment_edit_category.category_title


class EditBrandFragment : Fragment() {
    var brandName:String?=null
    var brandId:Int?=null

    companion object {
        fun newInstance() = EditFragment()
    }

    private lateinit var viewModel: BrandViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_edit_brand, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(BrandViewModel::class.java)
        val bundle = arguments
        brand_title.setText((bundle?.getString("brandName")))
        brandName=(bundle?.getString("brandName"))
       // Toasty.error(requireContext(),(bundle?.getString("brandName").toString()), Toast.LENGTH_LONG, true).show()
        brandId=(bundle?.getInt("brandId"))
        editBrand.setOnClickListener { editBrand() }
        back.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, BrandFragment())
                .commitNow()  }
        observers()

    }
    private fun setStatus(data: Resource<BrandData>) {
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
        viewModel.observeBrandsAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.brands != null && it.data.brands!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                    //   val navController = Navigation.findNavController(requireView())
                    //   navController.navigate(R.id.nav_category)
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, BrandFragment())
                            .commitNow()


                }
                else{
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })

    }

    private fun validate(): Boolean {
        if (!Validator.isValidName(brand_title)) {
            return false
        }

        return true
    }
    fun  editBrand(){
        if (validate()) {
            viewModel.editBrand(brand_title.text.toString(),brandId.toString(),brandName.toString())
        }

    }

}