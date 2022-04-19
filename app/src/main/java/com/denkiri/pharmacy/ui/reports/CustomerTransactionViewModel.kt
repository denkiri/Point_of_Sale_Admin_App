package com.denkiri.pharmacy.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.customer.CustomerData
import com.denkiri.pharmacy.storage.repository.CustomerRepository

class CustomerTransactionViewModel (application: Application) : AndroidViewModel(application){
    internal var customerRepository: CustomerRepository
    private val customerObservable = MediatorLiveData<Resource<CustomerData>>()
    init {
        customerRepository = CustomerRepository(application)
        customerObservable.addSource(customerRepository.customerObservable) { data -> customerObservable.setValue(data) }
    }
    fun observeCustomer(): LiveData<Resource<CustomerData>> {
        return customerObservable
    }

    fun getCustomer() {
        customerRepository.getCustomers()
    }

}