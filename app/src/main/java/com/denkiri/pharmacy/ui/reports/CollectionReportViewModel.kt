package com.denkiri.pharmacy.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivableData
import com.denkiri.pharmacy.models.reports.accountReceivable.TotalBalance
import com.denkiri.pharmacy.models.reports.collectionReport.CollectionReportData
import com.denkiri.pharmacy.models.reports.collectionReport.TotalCollection
import com.denkiri.pharmacy.storage.repository.AccountReceivableRepository
import com.denkiri.pharmacy.storage.repository.CollectionRepository

class CollectionReportViewModel(application: Application) : AndroidViewModel(application){
    internal var collectionRepository: CollectionRepository
    private val collectionObservable = MediatorLiveData<Resource<CollectionReportData>>()
    init {
        collectionRepository = CollectionRepository(application)
        collectionObservable.addSource(collectionRepository.collectionObservable) { data -> collectionObservable.setValue(data) }
    }
    fun getCollection() {
        collectionRepository.getCollection()
    }
    fun getTotalCollection(): LiveData<TotalCollection> {
        return collectionRepository.getTotalCollection()
    }
    fun observeAccountsReceivables(): LiveData<Resource<CollectionReportData>> {
        return collectionObservable
    }
}