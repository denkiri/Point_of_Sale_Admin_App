package com.denkiri.pharmacy.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.cashreport.CashReportData
import com.denkiri.pharmacy.models.reports.mpesareport.MpesaReportData
import com.denkiri.pharmacy.storage.repository.CashRepository
import com.denkiri.pharmacy.storage.repository.MpesaRepository

class MpesaSaleReportViewModel (application: Application) : AndroidViewModel(application) {
    internal var mpesaRepository: MpesaRepository
    private val mpesaObservable = MediatorLiveData<Resource<MpesaReportData>>()
    init {
        mpesaRepository = MpesaRepository(application)
        mpesaObservable.addSource(mpesaRepository.mpesaObservable) { data -> mpesaObservable.setValue(data) }
    }
    fun getMpesaReport() {
        mpesaRepository.mpesaReport()
    }
    fun observeMpesaReport(): LiveData<Resource<MpesaReportData>> {
        return mpesaObservable
    }
}