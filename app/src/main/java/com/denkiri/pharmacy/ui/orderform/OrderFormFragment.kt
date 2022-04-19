package com.denkiri.pharmacy.ui.orderform

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denkiri.pharmacy.R

class OrderFormFragment : Fragment() {

    companion object {
        fun newInstance() = OrderFormFragment()
    }

    private lateinit var viewModel: OrderFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_form_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderFormViewModel::class.java)
        // TODO: Use the ViewModel
    }

}