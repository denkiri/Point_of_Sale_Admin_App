package com.denkiri.pharmacy.ui.expiry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.storage.repository.ExpiringProductsRepository
import com.denkiri.pharmacy.storage.repository.ProductActionRepository
import com.denkiri.pharmacy.storage.repository.ProductsRepository

class ProductExpiryViewModel (application: Application) : AndroidViewModel(application) {
    internal var productsRepository: ProductsRepository
    internal var productsActionRepository: ProductActionRepository
    private val productActionObservable= MediatorLiveData<Resource<ProductData>>()
    private val productsObservable = MediatorLiveData<Resource<ProductData>>()
    internal var expiringProductsRepository: ExpiringProductsRepository
    private val expiringProductsObservable = MediatorLiveData<Resource<ProductData>>()
    init {
        productsActionRepository = ProductActionRepository(application)
        productActionObservable.addSource(productsActionRepository.productActionObservable) { data -> productActionObservable.setValue(data)}
        productsRepository = ProductsRepository(application)
        productsObservable.addSource(productsRepository.productObservable) { data -> productsObservable.setValue(data) }
        expiringProductsRepository = ExpiringProductsRepository(application)
        expiringProductsObservable.addSource(expiringProductsRepository.productObservable) { data -> expiringProductsObservable.setValue(data) }

    }
    fun getProducts() {
        expiringProductsRepository.getProducts()
    }
    fun observeProducts(): LiveData<Resource<ProductData>> {
        return expiringProductsObservable
    }
    fun pullOutProduct(productCode:String?,productName:String?,productDescription:String?,cost:String?,quantity:String?,category: String?,expiryDate:String?){
        productsActionRepository.pullOutProduct(productCode!!,productName!!, productDescription!!,cost!!,quantity!!,category!!,expiryDate!!)
    }
    fun observeProductsAction(): LiveData<Resource<ProductData>> {
        return productActionObservable
    }

}