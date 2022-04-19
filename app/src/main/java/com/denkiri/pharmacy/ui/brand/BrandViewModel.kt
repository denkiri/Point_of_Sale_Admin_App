package com.denkiri.pharmacy.ui.brand
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.brand.BrandData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.storage.repository.BrandActionRepository
import com.denkiri.pharmacy.storage.repository.BrandRepository
import com.denkiri.pharmacy.storage.repository.ProductsRepository

class BrandViewModel (application: Application) : AndroidViewModel(application) {
    internal var productsRepository: ProductsRepository
    internal var brandsRepository: BrandRepository
    internal var brandsActionRepository: BrandActionRepository
    private val brandsObservable = MediatorLiveData<Resource<BrandData>>()
    private val brandActionObservable= MediatorLiveData<Resource<BrandData>>()
    private val productsObservable = MediatorLiveData<Resource<ProductData>>()
    init {
        productsRepository = ProductsRepository(application)
        brandsActionRepository= BrandActionRepository(application)
        brandsRepository = BrandRepository(application)
        brandsObservable.addSource(brandsRepository.brandsObservable) { data -> brandsObservable.setValue(data) }
        brandActionObservable.addSource(brandsActionRepository.brandActionObservable){ data ->brandActionObservable.setValue(data)}
        productsObservable.addSource(productsRepository.productObservable) { data -> productsObservable.setValue(data) }


    }
    fun getBrandProducts(brand: String?){
        productsRepository.getBrandProducts(brand!!)
    }
    fun observeProducts(): LiveData<Resource<ProductData>> {
        return productsObservable
    }
    fun observeBrands(): LiveData<Resource<BrandData>> {
        return brandsObservable
    }
    fun getBrands() {
        brandsRepository.getBrands()
    }
    fun deleteBrand(brand: Int?){

        brandsActionRepository.deleteBrand(brand!!)

    }
    fun observeBrandsAction(): LiveData<Resource<BrandData>> {
        return brandActionObservable
    }
    fun addBrand(brandName:String?){
        brandsActionRepository.addBrand(brandName!!)
    }
    fun editBrand(brandName:String?,brandId:String?,brands:String?){
        if (brandId != null) {
            if (brandName != null) {
                brandsActionRepository.editBrand(brandName,brandId,brands!!)
            }
        }
    }
    fun activateBrand(cid: String?){
        brandsActionRepository.activateBrand(cid!!)
    }
    fun deactivateBrand(cid: String?){
        brandsActionRepository.deactivateBrand(cid!!)
    }
}
