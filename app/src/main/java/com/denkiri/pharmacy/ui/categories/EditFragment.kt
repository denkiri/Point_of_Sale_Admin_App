package com.denkiri.pharmacy.ui.categories

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_edit_category.*

class EditFragment : Fragment() {
    var categoryName:String?=null
    var categoryId:Int?=null

    companion object {
        fun newInstance() = EditFragment()
    }

    private lateinit var viewModel: CategoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_edit_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        val bundle = arguments
        category_title.setText((bundle?.getString("categoryName")))
       // categoryName=(activity as CategoryActivity).categoryName
        categoryId=(bundle?.getInt("categoryId"))
        categoryName=(bundle?.getString("categoryName"))
     //   category_title.setText(  categoryName)
        editCategory.setOnClickListener { editCategory() }
        back.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, CategoriesFragment())
                .commitNow()  }
        observers()

    }
    private fun setStatus(data: Resource<CategoryData>) {
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
        viewModel.observeCategoriesAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.categories != null && it.data.categories!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                 //   val navController = Navigation.findNavController(requireView())
                 //   navController.navigate(R.id.nav_category)
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, CategoriesFragment())
                            .commitNow()


                }
                else{
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })

    }

    private fun validate(): Boolean {
        if (!Validator.isValidName(category_title)) {
            return false
        }

        return true
    }
    fun  editCategory(){
        if (validate()) {
            viewModel.editCategory(category_title.text.toString(),categoryId.toString(),categoryName.toString())
        }

    }

}