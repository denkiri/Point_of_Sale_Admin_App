package com.denkiri.pharmacy.ui.ledger
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.customer.CustomerData
import com.denkiri.pharmacy.models.invoice.Invoice
import com.denkiri.pharmacy.models.invoice.InvoiceData
import com.denkiri.pharmacy.storage.repository.CustomerActionRepository
import com.denkiri.pharmacy.storage.repository.InvoiceActionRepository
import com.denkiri.pharmacy.storage.repository.InvoicesRepository
class LedgerViewModel (application: Application) : AndroidViewModel(application)  {
    internal var invoiceRepository: InvoicesRepository
    private val invoicesObservable = MediatorLiveData<Resource<InvoiceData>>()
    internal var invoiceActionRepository: InvoiceActionRepository
    private val invoiceActionObservable = MediatorLiveData<Resource<InvoiceData>>()
    init {
        invoiceActionRepository = InvoiceActionRepository(application)
        invoiceActionObservable.addSource(invoiceActionRepository.invoiceActionObservable) { data -> invoiceActionObservable.setValue(data)}
        invoiceRepository = InvoicesRepository(application)
        invoicesObservable.addSource(invoiceRepository.invoiceObservable) { data -> invoicesObservable.setValue(data) }

    }
    fun addPayment(name:String?,invoice:String?,totalAmount:String?,amount:String?,remarks:String?,balance:String?){
        invoiceActionRepository.addPayment(name!!, invoice!!, totalAmount!!, amount!!, remarks!!,balance!!)
    }
    fun observeInvoices(): LiveData<Resource<InvoiceData>> {
        return invoicesObservable
    }
    fun observeInvoiceAction(): LiveData<Resource<InvoiceData>> {
        return invoiceActionObservable
    }
    fun getInvoices() {
        invoiceRepository.getInvoices()
    }
}