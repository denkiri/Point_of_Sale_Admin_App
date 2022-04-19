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
import com.denkiri.pharmacy.ui.categories.AddCategoryFragment
import com.denkiri.pharmacy.ui.categories.CategoriesFragment
import com.denkiri.pharmacy.ui.categories.CategoryViewModel
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_brand.*
import kotlinx.android.synthetic.main.fragment_add_category.*
import kotlinx.android.synthetic.main.fragment_add_category.avi
import kotlinx.android.synthetic.main.fragment_add_category.back
import kotlinx.android.synthetic.main.fragment_add_category.category_title


class AddBrandFragment : Fragment() {
    private lateinit var brandsViewModel: BrandViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_brand, container, false)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                AddCategoryFragment().apply {
                    arguments = Bundle().apply {

                    }
                }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        brandsViewModel = ViewModelProvider(this).get(BrandViewModel::class.java)
        addBrand.setOnClickListener { addBrand() }
        back.setOnClickListener {    requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, BrandFragment())
                .commitNow() }
        observers()
    }
    private fun validate(): Boolean {
        if (!Validator.isValidName(brand_title)) {
            return false
        }

        return true
    }
    fun  addBrand(){
        if (validate()) {
            brandsViewModel.addBrand(brand_title.text.toString())
        }

    }
    fun observers(){
        brandsViewModel.observeBrandsAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.brands != null && it.data.brands!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                    //  val navController = Navigation.findNavController(requireView())
                    // navController.navigate(R.id.nav_category)
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

}