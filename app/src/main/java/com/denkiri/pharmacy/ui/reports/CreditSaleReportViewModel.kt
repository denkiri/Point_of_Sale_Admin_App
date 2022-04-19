package com.denkiri.pharmacy.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.creditreport.CreditReportData
import com.denkiri.pharmacy.storage.repository.CreditRepository
class CreditSaleReportViewModel  (application: Application) : AndroidViewModel(application){
    internal var creditRepository: CreditRepository
    private val creditObservable = MediatorLiveData<Resource<CreditReportData>>()
    init {
        creditRepository = CreditRepository(application)
        creditObservable.addSource(creditRepository.creditObservable) { data -> creditObservable.setValue(data) }
    }
    fun getCreditReport() {
        creditRepository.getCredit()
    }
    fun observeCashReport(): LiveData<Resource<CreditReportData>> {
        return creditObservable
    }
}