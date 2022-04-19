package com.denkiri.pharmacy.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.salesReport.*
import com.denkiri.pharmacy.storage.repository.SalesRepository

class DayReportViewModel(application: Application) : AndroidViewModel(application) {
    internal var salesRepository: SalesRepository
    private val salesObservable = MediatorLiveData<Resource<SalesReportData>>()
    init {
        salesRepository = SalesRepository(application)
        salesObservable.addSource(salesRepository.salesObservable) { data -> salesObservable.setValue(data) }
    }
    fun getDaySalesReport(date:String?) {
        salesRepository.getDaySales(date!!)
    }
    fun getTotalSales(): LiveData<TotalSales> {
        return salesRepository.getTotalSales()
    }
    fun getTotalCost(): LiveData<TotalCost> {
        return salesRepository.getTotalCost()
    }
    fun getTotalProfit(): LiveData<TotalProfit> {
        return salesRepository.getTotalProfit()
    }
    fun getTotalSalesBalance(): LiveData<TotalSalesBalance> {
        return salesRepository.getTotalSalesBalance()
    }
    fun observeSalesReport(): LiveData<Resource<SalesReportData>> {
        return salesObservable
    }
}