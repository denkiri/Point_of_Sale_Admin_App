package com.denkiri.pharmacy.ui.reorder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.storage.repository.ExpiringProductsRepository
import com.denkiri.pharmacy.storage.repository.ReOrderRepository

class ReorderViewModel (application: Application) : AndroidViewModel(application) {
    internal var reOrderRepository: ReOrderRepository
    private val reOrderObservable = MediatorLiveData<Resource<ProductData>>()
    init {
        reOrderRepository = ReOrderRepository(application)
        reOrderObservable.addSource(reOrderRepository.productObservable) { data -> reOrderObservable.setValue(data) }

    }
    fun getProducts() {
        reOrderRepository.getProducts()
    }
    fun observeProducts(): LiveData<Resource<ProductData>> {
        return reOrderObservable
    }
}