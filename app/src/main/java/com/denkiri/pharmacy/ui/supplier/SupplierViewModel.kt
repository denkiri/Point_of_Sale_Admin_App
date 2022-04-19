package com.denkiri.pharmacy.ui.supplier
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.storage.repository.ProductsRepository
import com.denkiri.pharmacy.storage.repository.SupplierActionRepository
import com.denkiri.pharmacy.storage.repository.SupplierRepository
class SupplierViewModel (application: Application) : AndroidViewModel(application){
    internal var supplierRepository: SupplierRepository
    internal var productsRepository: ProductsRepository
    internal var supplierActionRepository:SupplierActionRepository
    private val productsObservable = MediatorLiveData<Resource<ProductData>>()
    private  var supplierActionObservable=MediatorLiveData<Resource<SupplierData>>()
    private val supplierObservable = MediatorLiveData<Resource<SupplierData>>()
    init {
        supplierActionRepository= SupplierActionRepository(application)
        supplierRepository = SupplierRepository(application)
        productsRepository = ProductsRepository(application)
        productsObservable.addSource(productsRepository.productObservable) { data -> productsObservable.setValue(data) }
        supplierActionObservable.addSource(supplierActionRepository.supplierActionObservable) { data -> supplierActionObservable.setValue(data) }
        supplierObservable.addSource(supplierRepository.supplierObservable) { data -> supplierObservable.setValue(data) }
    }
    fun observeSuppliers(): LiveData<Resource<SupplierData>> {
        return supplierObservable
    }
    fun observeSupplierAction(): LiveData<Resource<SupplierData>> {
        return supplierActionObservable
    }
    fun deleteSupplier(supplier: String?){

        supplierActionRepository.deleteSupplier(supplier!!)

    }
    fun getSupplierProducts(supplier: String?){

        productsRepository.getSupplierProducts(supplier!!)

    }
    fun observeProducts(): LiveData<Resource<ProductData>> {
        return productsObservable
    }
    fun getSuppliers() {
        supplierRepository.getSuppliers()
    }
    fun getActiveSuppliers() {
        supplierRepository.getActiveSuppliers()
    }
    fun addSupplier(supplierName:String?,supplierAddress:String?,supplierContact:String?,contactPerson:String?){
        if (supplierContact != null) {
            if (contactPerson != null) {
                if (supplierName != null) {
                    if (supplierAddress != null) {
                        supplierActionRepository.addSupplier(supplierName, supplierAddress,supplierContact,contactPerson)
                    }
                }
            }
        }
    }
    fun editSupplier(supplierName:String?,supplierAddress:String?,supplierContact:String?,contactPerson:String?,supplierId:String?){
        if (supplierContact != null) {
            if (contactPerson != null) {
                if (supplierName != null) {
                    if (supplierAddress != null) {
                        if (supplierId != null) {
                            supplierActionRepository.editSupplier(supplierName, supplierAddress,supplierContact,contactPerson,supplierId)
                        }
                    }
                }
            }
        }
    }
    fun activateSupplier(sid: String?){
        supplierActionRepository.activateSupplier(sid!!)
    }
    fun deactivateSupplier(sid: String?){
        supplierActionRepository.deactivateSupplier(sid!!)
    }
}