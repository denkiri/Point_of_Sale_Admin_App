package com.denkiri.pharmacy.ui.products

import android.app.Application
import androidx.lifecycle.*
import com.denkiri.pharmacy.models.brand.BrandData
import com.denkiri.pharmacy.models.category.Category
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.oauth.Profile
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.storage.repository.*

class ProductsViewModel (application: Application) : AndroidViewModel(application){
    internal var brandsRepository: BrandRepository
    internal var supplierRepository: SupplierRepository
    private val supplierObservable = MediatorLiveData<Resource<SupplierData>>()
    internal var categoriesRepository: CategoryRepository
    private val categoriesObservable = MediatorLiveData<Resource<CategoryData>>()
    internal var productsRepository: ProductsRepository
    internal var productsActionRepository:ProductActionRepository
    private val productActionObservable= MediatorLiveData<Resource<ProductData>>()
    private val productsObservable = MediatorLiveData<Resource<ProductData>>()
    private val brandsObservable = MediatorLiveData<Resource<BrandData>>()
    init {
        supplierRepository = SupplierRepository(application)
        productsActionRepository = ProductActionRepository(application)
        brandsRepository = BrandRepository(application)
        brandsObservable.addSource(brandsRepository.brandsObservable) { data -> brandsObservable.setValue(data) }
        supplierObservable.addSource(supplierRepository.supplierObservable) { data -> supplierObservable.setValue(data) }
        productsRepository = ProductsRepository(application)
        productsObservable.addSource(productsRepository.productObservable) { data -> productsObservable.setValue(data) }
        categoriesRepository = CategoryRepository(application)
        categoriesObservable.addSource(categoriesRepository.categoriesObservable) { data -> categoriesObservable.setValue(data) }
        productActionObservable.addSource(productsActionRepository.productActionObservable) { data -> productActionObservable.setValue(data)

        }
    }
    fun observeSuppliers(): LiveData<Resource<SupplierData>> {
        return supplierObservable
    }
    fun observeBrands(): LiveData<Resource<BrandData>> {
        return brandsObservable
    }
    fun getBrands() {
        brandsRepository.getActiveBrands()
    }
    fun getSuppliers() {
        supplierRepository.getActiveSuppliers()
    }
    fun getProducts() {
        productsRepository.getProducts()
    }
    fun observeProducts(): LiveData<Resource<ProductData>> {
        return productsObservable
    }
    fun observeCategories(): LiveData<Resource<CategoryData>> {
        return categoriesObservable
    }
    fun getCategories() {
        categoriesRepository.getActiveCategories()
    }
    fun pullOutProduct(productCode:String?,productName:String?,productDescription:String?,cost:String?,quantity:String?,category: String?,expiryDate:String?){
        productsActionRepository.pullOutProduct(productCode!!,productName!!, productDescription!!,cost!!,quantity!!,category!!,expiryDate!!)
    }
    fun addProduct(price:String?,cost:String?,productName:String?,productDescription:String?,supplier:String?,quantity:String?,category:String?,deliveryDate:String?,expiryDate:String?,unit:String?,reorder:String?,brand:String?,vat:String?){
        productsActionRepository.addProduct(price!!,cost!!,productName!!, productDescription!!, supplier!!, quantity!!, category!!, deliveryDate!!, expiryDate!!, unit!!, reorder!!,brand!!,vat!!)
    }
    fun editProduct(price:String?,cost:String?,productName:String?,productDescription:String?,supplier:String?,quantity:String?,category:String?,brand: String?,vat: String?,unit:String?,reorder:String?,expiryDate:String?,pid:String?){
        productsActionRepository.editProduct(price!!,cost!!,productName!!, productDescription!!, supplier!!, quantity!!, category!!,brand!!,vat!!, unit!!, reorder!!,expiryDate!!,pid!!)
    }
    fun increaseProduct(price:String?,cost:String?,quantity:String?,expiryDate:String?,pid:String?){
        productsActionRepository.increaseStock(price!!,cost!!, quantity!!,expiryDate!!,pid!!)
    }
    fun observeProductsAction(): LiveData<Resource<ProductData>> {
        return productActionObservable
    }
    fun getCategoriesList():LiveData <List<Category>> {
        return productsRepository.getCategories()
    }
    fun deleteProduct(pid:String?){
        productsActionRepository.deleteProduct(pid!!)
    }
}