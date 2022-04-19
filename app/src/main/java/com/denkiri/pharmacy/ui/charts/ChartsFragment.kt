package com.denkiri.pharmacy.ui.charts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.ui.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_charts.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ChartsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChartsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_charts, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChartsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                ChartsFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        card_categoryStat.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, CategoryChartFragment())
                .commitNow()

        }
        card_brandStat.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, BrandChartFragment())
                .commitNow()
        }
        card_paymentStat.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, PaymentModeFragment())
                .commitNow()

        }
        card_monthlyStat.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, MonthFragment())
                .commitNow()
        }
        card_yearlyStat.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, YearlySalesFragment())
                .commitNow()

        }
        card_loseStat.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoseChartFragment())
                .commitNow()

        }
        card_vat.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, VatChartFragment())
                    .commitNow()

        }
    }
}