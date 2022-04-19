package com.denkiri.pharmacy.ui.customers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.customer.Customer
import com.denkiri.pharmacy.models.customer.CustomerData
import com.denkiri.pharmacy.storage.repository.CategoryActionRepository
import com.denkiri.pharmacy.storage.repository.CustomerActionRepository
import com.denkiri.pharmacy.storage.repository.CustomerRepository
class CustomerViewModel (application: Application) : AndroidViewModel(application) {
    internal var customerRepository: CustomerRepository
    internal var customerActionRepository: CustomerActionRepository
    private val customerActionObservable = MediatorLiveData<Resource<CustomerData>>()
    private val customerObservable = MediatorLiveData<Resource<CustomerData>>()

    init {
        customerActionRepository = CustomerActionRepository(application)
        customerActionObservable.addSource(customerActionRepository.customerActionObservable) { data -> customerActionObservable.setValue(data)}

        customerRepository = CustomerRepository(application)
        customerObservable.addSource(customerRepository.customerObservable) { data -> customerObservable.setValue(data) }
        }
        fun observeCustomer(): LiveData<Resource<CustomerData>> {
            return customerObservable
        }

        fun getCustomer() {
            customerRepository.getCustomers()
        }
    fun addCustomer(firstName:String?,middleName:String?,lastName:String?,address:String?,contact:String?){
        customerActionRepository.addCustomer(firstName!!, middleName!!, lastName!!, address!!, contact!!)
    }
    fun editCustomer(firstName:String?,middleName:String?,lastName:String?,address:String?,contact:String?,cid:String?,code:String?,name:String?){
        customerActionRepository.editCustomer(firstName!!, middleName!!, lastName!!, address!!, contact!!, cid!!,code!!,name!!)
    }
    fun observeCustomerAction(): LiveData<Resource<CustomerData>> {
        return customerActionObservable
    }
    fun deleteCustomer(cid: Int?,customerName:String?){
        if (customerName != null) {
            customerActionRepository.deleteCustomer(cid!!,customerName)
        }
    }
    fun activateCustomer(cid: Int?){
            customerActionRepository.activateCustomer(cid!!)
        }
    fun deactivateCustomer(cid: Int?){
        customerActionRepository.deactivateCustomer(cid!!)
    }

    }
