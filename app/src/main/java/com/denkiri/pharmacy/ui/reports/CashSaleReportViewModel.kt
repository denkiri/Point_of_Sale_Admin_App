package com.denkiri.pharmacy.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.cashreport.CashReport
import com.denkiri.pharmacy.models.reports.cashreport.CashReportData
import com.denkiri.pharmacy.models.reports.collectionReport.CollectionReportData
import com.denkiri.pharmacy.storage.repository.CashRepository
import com.denkiri.pharmacy.storage.repository.CollectionRepository

class CashSaleReportViewModel (application: Application) : AndroidViewModel(application){
    internal var cashRepository: CashRepository
    private val cashObservable = MediatorLiveData<Resource<CashReportData>>()
    init {
        cashRepository = CashRepository(application)
        cashObservable.addSource(cashRepository.cashObservable) { data -> cashObservable.setValue(data) }
    }
    fun getCashReport() {
        cashRepository.getCashReport()
    }
    fun observeCashReport(): LiveData<Resource<CashReportData>> {
        return cashObservable
    }
}