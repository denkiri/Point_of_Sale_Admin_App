package com.denkiri.pharmacy.ui.categories
import android.app.Application
import androidx.lifecycle.*
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.storage.repository.CategoryActionRepository
import com.denkiri.pharmacy.storage.repository.CategoryRepository
import com.denkiri.pharmacy.storage.repository.ProductsRepository

class CategoryViewModel (application: Application) : AndroidViewModel(application) {
    internal var productsRepository: ProductsRepository
    internal var categoriesRepository: CategoryRepository
    internal var categoriesActionRepository: CategoryActionRepository
    private val categoriesObservable = MediatorLiveData<Resource<CategoryData>>()
    private val categoryActionObservable=MediatorLiveData<Resource<CategoryData>>()
    private val productsObservable = MediatorLiveData<Resource<ProductData>>()
init {
    productsRepository = ProductsRepository(application)
    categoriesActionRepository= CategoryActionRepository(application)
    categoriesRepository = CategoryRepository(application)
    categoriesObservable.addSource(categoriesRepository.categoriesObservable) { data -> categoriesObservable.setValue(data) }
    categoryActionObservable.addSource(categoriesActionRepository.categoriesActionObservable){ data ->categoryActionObservable.setValue(data)}
    productsObservable.addSource(productsRepository.productObservable) { data -> productsObservable.setValue(data) }
}
    fun getCategoryProducts(category: String?){
        productsRepository.getCategoryProducts(category!!)
    }
    fun observeProducts(): LiveData<Resource<ProductData>> {
        return productsObservable
    }
    fun observeCategories(): LiveData<Resource<CategoryData>> {
        return categoriesObservable
    }
    fun getCategories() {
        categoriesRepository.getCategories()
    }
    fun deleteCategory(category: Int?){

        categoriesActionRepository.deleteCategory(category!!)

    }
    fun observeCategoriesAction(): LiveData<Resource<CategoryData>> {
        return categoryActionObservable
    }
    fun addCategory(categoryName:String?){
        categoriesActionRepository.addCategory(categoryName!!)
    }
    fun editCategory(categoryName:String?,categoryId:String?,category:String?){
        if (categoryId != null) {
            if (categoryName != null) {
                categoriesActionRepository.editCategory(categoryName,categoryId,category!!)
            }
        }
    }
    fun activateCategory(cid: String?){
        categoriesActionRepository.activateCategory(cid!!)
    }
    fun deactivateCategory(cid: String?){
        categoriesActionRepository.deactivateCategory(cid!!)
    }
    }

