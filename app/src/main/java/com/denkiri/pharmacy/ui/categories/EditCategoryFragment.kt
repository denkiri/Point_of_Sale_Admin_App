package com.denkiri.pharmacy.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_edit_category.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [EditCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditCategoryFragment :  Fragment() {
    var categoryId:Int?=null
    var categoryName:String?=null
    private lateinit var categoriesViewModel: CategoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_category, container, false)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            EditCategoryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        categoriesViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        val bundle = arguments
        categoryId=(bundle?.getInt("categoryId"))
        categoryName=(bundle?.getString("categoryName"))
        category_title.setText((bundle?.getString("categoryName")))
        editCategory.setOnClickListener { editCategory() }
        observers()




    }
    fun observers(){
        categoriesViewModel.observeCategoriesAction().observe(viewLifecycleOwner, {
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
            categoriesViewModel.editCategory(category_title.text.toString(),categoryId.toString(),categoryName.toString())
        }

    }

}