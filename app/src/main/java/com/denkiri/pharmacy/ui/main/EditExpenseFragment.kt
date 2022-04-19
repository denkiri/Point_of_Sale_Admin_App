package com.denkiri.pharmacy.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.expense.ExpenseData
import com.denkiri.pharmacy.ui.categories.CategoriesFragment
import com.denkiri.pharmacy.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_edit_expense.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [EditExpenseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditExpenseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: ExpenseViewModel
    var expenseId:Int?=null
    var expenseName:String?=null
    var expenseAmount:String?=null

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
        return inflater.inflate(R.layout.fragment_edit_expense, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditExpenseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            EditExpenseFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    private fun setStatus(data: Resource<ExpenseData>) {
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
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        val bundle = arguments
        expenseId=(bundle?.getInt("expenseId"))
        expenseName=(bundle?.getString("expenseName"))
        expenseAmount=(bundle?.getString("expenseAmount"))
        expense_name.setText((bundle?.getString("expenseName")))
        expense_amount.setText((bundle?.getString("expenseAmount")))
        editExpense.setOnClickListener {
            editExpense()
        }
        observers()
        back.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ExpenseCategoryFragment())
                .commitNow()
        }
    }
    fun observers(){
        viewModel.observeExpenseAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.expenses != null && it.data.expenses!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                    //   val navController = Navigation.findNavController(requireView())
                    //   navController.navigate(R.id.nav_category)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ExpenseCategoryFragment())
                        .commitNow()

                }
                else{
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })

    }
    private fun validate(): Boolean {
        if (!Validator.isValidName(expense_name)) {
            return false
        }
        if (!Validator.isValidPrice(expense_amount)) {
            return false
        }
        return true
    }
    fun  editExpense(){
        if (validate()) {
            viewModel.editExpense(expense_name.text.toString(),expense_amount.text.toString(),expenseId.toString())
        }

    }



}