package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.category.Category
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.oauth.Profile
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.daos.CategoryDao
import com.denkiri.pharmacy.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ProductsRepository (application: Application)  {
    private val context: Context
    private val profileDao: ProfileDao
    private val categoryDao: CategoryDao
    private val db: PharmacyDatabase
    val productObservable = MutableLiveData<Resource<ProductData>>()
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        categoryDao=db.categoryDao()
        context =application.applicationContext
    }
    fun getProducts() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun getSupplierProducts(supplierId:String) {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            executeB(supplierId)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun getBrandProducts(brandName:String) {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            executeBrands(brandName)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun getCategoryProducts(categoryName:String) {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            executeCategories(categoryName)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).products()
            call.enqueue(object : Callback<ProductData> {
                override fun onFailure(call: Call<ProductData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<ProductData>?, response: Response<ProductData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {

                                setIsSuccessful(response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(it) }
                            }
                        } else {
                            setIsError("Error Loading Data")
                        }
                    } else {
                        setIsError("Error Loading Data")
                    }
                }
            })
        }
    }
    private fun executeB(sid:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).supplierProducts(sid)
            call.enqueue(object : Callback<ProductData> {
                override fun onFailure(call: Call<ProductData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<ProductData>?, response: Response<ProductData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {

                                setIsSuccessful(response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(it) }
                            }
                        } else {
                            setIsError("Error Loading Data")
                        }
                    } else {
                        setIsError("Error Loading Data")
                    }
                }
            })
        }
    }
    private fun executeBrands(sid:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).brandProducts(sid)
            call.enqueue(object : Callback<ProductData> {
                override fun onFailure(call: Call<ProductData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<ProductData>?, response: Response<ProductData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {

                                setIsSuccessful(response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(it) }
                            }
                        } else {
                            setIsError("Error Loading Data")
                        }
                    } else {
                        setIsError("Error Loading Data")
                    }
                }
            })
        }
    }
    private fun executeCategories(sid:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).categoryProducts(sid)
            call.enqueue(object : Callback<ProductData> {
                override fun onFailure(call: Call<ProductData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<ProductData>?, response: Response<ProductData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {

                                setIsSuccessful(response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(it) }
                            }
                        } else {
                            setIsError("Error Loading Data")
                        }
                    } else {
                        setIsError("Error Loading Data")
                    }
                }
            })
        }
    }
    private fun setIsLoading() {
        productObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: ProductData) {
        productObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        productObservable.postValue(Resource.error(message, null))
    }
    fun getCategories(): LiveData<List<Category>> {
        return categoryDao.getAllCategories()
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}