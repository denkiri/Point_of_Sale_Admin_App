package com.denkiri.pharmacy.ui.creditDue

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.invoice.InvoiceData
import com.denkiri.pharmacy.storage.repository.CreditDueRepository
class CreditDueViewModel (application: Application) : AndroidViewModel(application)  {
    internal var creditDueRepository: CreditDueRepository
    private val creditDueObservable = MediatorLiveData<Resource<InvoiceData>>()
    init {
        creditDueRepository = CreditDueRepository(application)
        creditDueObservable.addSource(creditDueRepository.invoiceObservable) { data -> creditDueObservable.setValue(data) }

    }
    fun getInvoices() {
        creditDueRepository.getInvoices()
    }
    fun observeInvoices(): LiveData<Resource<InvoiceData>> {
        return creditDueObservable
    }
}