package com.denkiri.pharmacy.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.collectionReport.TotalCollection
import com.denkiri.pharmacy.models.reports.salesReport.*
import com.denkiri.pharmacy.storage.repository.CustomerTransactionsRepository
import com.denkiri.pharmacy.storage.repository.SalesRepository
class SalesReportViewModel (application: Application) : AndroidViewModel(application) {
    internal var salesRepository: SalesRepository
    private val salesObservable = MediatorLiveData<Resource<SalesReportData>>()
    internal var customerTransactionsRepository:CustomerTransactionsRepository
    private val  customerTransactionsObservable= MediatorLiveData<Resource<CustomerTransactionsData>>()
    init {
        customerTransactionsRepository= CustomerTransactionsRepository(application)
        salesRepository = SalesRepository(application)
        customerTransactionsObservable.addSource(customerTransactionsRepository.salesObservable) { data -> customerTransactionsObservable.setValue(data) }
        salesObservable.addSource(salesRepository.salesObservable) { data -> salesObservable.setValue(data) }
    }
    fun getSalesReport() {
        salesRepository.getSales()
    }
    fun getCustomerTransactions(name:String?) {
        customerTransactionsRepository.getCustomerTransactions(name!!)
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
    fun observeCustomerTransactions(): LiveData<Resource<CustomerTransactionsData>> {
        return customerTransactionsObservable
    }
}