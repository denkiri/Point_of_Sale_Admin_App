package com.denkiri.pharmacy.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.collectionReport.TotalCollection
import com.denkiri.pharmacy.models.reports.expiredproducts.ExpiryReportData
import com.denkiri.pharmacy.models.reports.expiredproducts.TotalLose
import com.denkiri.pharmacy.storage.repository.ExpiryRepository

class ExpiredProductsViewModel (application: Application) : AndroidViewModel(application) {
    internal var expiryRepository: ExpiryRepository
    private val expiryObservable = MediatorLiveData<Resource<ExpiryReportData>>()
    init {
        expiryRepository = ExpiryRepository(application)
        expiryObservable.addSource(expiryRepository.expiryObservable) { data -> expiryObservable.setValue(data) }
    }
    fun getExpiredProducts() {
        expiryRepository.getExpiryReport()
    }
    fun getTotalLose(): LiveData<TotalLose> {
        return expiryRepository.getTotalLose()
    }
    fun observeExpiry(): LiveData<Resource<ExpiryReportData>> {
        return expiryObservable
    }
}