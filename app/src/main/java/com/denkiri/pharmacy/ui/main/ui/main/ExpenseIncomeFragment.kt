package com.denkiri.pharmacy.ui.main.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denkiri.pharmacy.R

class ExpenseIncomeFragment : Fragment() {

    companion object {
        fun newInstance() = ExpenseIncomeFragment()
    }

    private lateinit var viewModel: IncomeExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.income_report_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IncomeExpenseViewModel::class.java)
        // TODO: Use the ViewModel
    }

}