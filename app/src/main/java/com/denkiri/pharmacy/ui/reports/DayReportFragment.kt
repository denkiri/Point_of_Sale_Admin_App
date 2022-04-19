package com.denkiri.pharmacy.ui.reports
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.ui.supplier.EditSupplierFragment
import com.denkiri.pharmacy.utils.DatePickerHelper
import kotlinx.android.synthetic.main.day_report_fragment.*
import kotlinx.android.synthetic.main.day_report_fragment.btSelectDate
import java.util.*
class DayReportFragment : Fragment() {
    lateinit var datePicker: DatePickerHelper
    companion object {
        fun newInstance() = DayReportFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        datePicker = DatePickerHelper(requireContext(),true)

    }

    private lateinit var viewModel: DayReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.day_report_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DayReportViewModel::class.java)
        datePicker = DatePickerHelper(requireContext(),true)
        btSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
        viewReport.setOnClickListener {
            val bundle = Bundle()
            bundle.putString( "date" , tvDate.text.toString())
           // val navController = Navigation.findNavController(requireView())
         //   navController.navigate(R.id.nav_daySalesReport, bundle)
            val fragment = DaySalesFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()

        }

    }
    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        val maxDate = Calendar.getInstance()
        maxDate.set(Calendar.HOUR_OF_DAY, 0)
        maxDate.set(Calendar.MINUTE, 0)
        maxDate.set(Calendar.SECOND, 0)
        datePicker.setMaxDate(maxDate.timeInMillis)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                tvDate.text = "${monthStr}/${dayStr}/${year}"

            }
        })
    }

}