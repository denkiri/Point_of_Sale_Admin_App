package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class CategoryActionRepository (application: Application) {
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val categoriesActionObservable = MutableLiveData<Resource<CategoryData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext

    }
    fun addCategory(categoryName: String) {
        setIsLoading(Observable.ADD_CATEGORY)

        if (NetworkUtils.isConnected(context)) {
            execute(categoryName)
        } else {
            setIsError(Observable.ADD_CATEGORY, context.getString(R.string.no_connection))
        }
    }
    fun editCategory(categoryName: String,categoryId: String,category: String) {
        setIsLoading(Observable.EDIT_CATEGORY)

        if (NetworkUtils.isConnected(context)) {
            updateCategory(categoryName,categoryId,category)
        } else {
            setIsError(Observable.EDIT_CATEGORY, context.getString(R.string.no_connection))
        }
    }
    fun activateCategory(categoryId:String) {
        setIsLoading(Observable.ACTIVATE_CATEGORY)

        if (NetworkUtils.isConnected(context)) {
            activate(categoryId)
        } else {
            setIsError(Observable.ACTIVATE_CATEGORY, context.getString(R.string.no_connection))
        }
    }
    fun deactivateCategory(categoryId:String) {
        setIsLoading(Observable.DEACTIVATE_CATEGORY)

        if (NetworkUtils.isConnected(context)) {
            deactivate(categoryId)
        } else {
            setIsError(Observable.DEACTIVATE_CATEGORY, context.getString(R.string.no_connection))
        }
    }
    fun execute(name:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addCategory(name)
            call.enqueue(object : Callback<CategoryData> {
                override fun onFailure(call: Call<CategoryData>?, t: Throwable?) {
                    setIsError(Observable.ADD_CATEGORY, t.toString())
                }
                override fun onResponse(call: Call<CategoryData>?, response: Response<CategoryData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_CATEGORY,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_CATEGORY,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_CATEGORY, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_CATEGORY, "Error Loading In")
                    }
                }
            })
        }

    }

    fun deleteCategory( categoryId: Int){

        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteCategory( categoryId.toString()
            )
            call.enqueue(object : Callback<CategoryData> {
                override fun onFailure(call: Call<CategoryData>?, t: Throwable?) {
                    setIsError(Observable.DELETE_CATEGORY, t.toString())
                }
                override fun onResponse(call: Call<CategoryData>?, response: Response<CategoryData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DELETE_CATEGORY,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_CATEGORY,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_CATEGORY, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_CATEGORY, "Error Loading In")
                    }
                }
            })
        }
    }
    fun updateCategory( name: String,cid: String,category:String){

        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).editCategory(name,cid,category
            )
            call.enqueue(object : Callback<CategoryData> {
                override fun onFailure(call: Call<CategoryData>?, t: Throwable?) {
                    setIsError(Observable.EDIT_CATEGORY, t.toString())
                }
                override fun onResponse(call: Call<CategoryData>?, response: Response<CategoryData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.EDIT_CATEGORY,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.EDIT_CATEGORY,it) }
                            }
                        }
                        else {
                            setIsError(Observable.EDIT_CATEGORY, response.toString())
                        }
                    } else {
                        setIsError(Observable.EDIT_CATEGORY, "Error Loading In")
                    }
                }
            })
        }
    }

    fun activate( categoryId: String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).activateCategory( categoryId.toString()
            )
            call.enqueue(object : Callback<CategoryData> {
                override fun onFailure(call: Call<CategoryData>?, t: Throwable?) {
                    setIsError(Observable.ACTIVATE_CATEGORY, t.toString())
                }
                override fun onResponse(call: Call<CategoryData>?, response: Response<CategoryData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ACTIVATE_CATEGORY,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ACTIVATE_CATEGORY,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ACTIVATE_CATEGORY, response.toString())
                        }
                    } else {
                        setIsError(Observable.ACTIVATE_CATEGORY, "Error Loading In")
                    }
                }
            })
        }
    }
    fun deactivate( categoryId: String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deactivateCategory( categoryId.toString()
            )
            call.enqueue(object : Callback<CategoryData> {
                override fun onFailure(call: Call<CategoryData>?, t: Throwable?) {
                    setIsError(Observable.DEACTIVATE_CATEGORY, t.toString())
                }
                override fun onResponse(call: Call<CategoryData>?, response: Response<CategoryData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DEACTIVATE_CATEGORY,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DEACTIVATE_CATEGORY,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DEACTIVATE_CATEGORY, response.toString())
                        }
                    } else {
                        setIsError(Observable.DEACTIVATE_CATEGORY, "Error Loading In")
                    }
                }
            })
        }
    }
    enum class Observable {
        DELETE_CATEGORY,
        EDIT_CATEGORY,
        ACTIVATE_CATEGORY,
        DEACTIVATE_CATEGORY,
        ADD_CATEGORY
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.EDIT_CATEGORY -> categoriesActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_CATEGORY -> categoriesActionObservable.postValue(Resource.loading(null))
           Observable.ADD_CATEGORY -> categoriesActionObservable.postValue(Resource.loading(null))
            Observable.ACTIVATE_CATEGORY -> categoriesActionObservable.postValue(Resource.loading(null))
            Observable.DEACTIVATE_CATEGORY -> categoriesActionObservable.postValue(Resource.loading(null))
        }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.EDIT_CATEGORY-> categoriesActionObservable.postValue(Resource.success(data as CategoryData))
            Observable.DELETE_CATEGORY-> categoriesActionObservable.postValue(Resource.success(data as CategoryData))
           Observable.ADD_CATEGORY-> categoriesActionObservable.postValue(Resource.success(data as CategoryData))
            Observable.ACTIVATE_CATEGORY-> categoriesActionObservable.postValue(Resource.success(data as CategoryData))
            Observable.DEACTIVATE_CATEGORY-> categoriesActionObservable.postValue(Resource.success(data as CategoryData))
        }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.EDIT_CATEGORY-> categoriesActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_CATEGORY-> categoriesActionObservable.postValue(Resource.error(message, null))
            Observable.ADD_CATEGORY-> categoriesActionObservable.postValue(Resource.error(message, null))
            Observable.ACTIVATE_CATEGORY-> categoriesActionObservable.postValue(Resource.error(message, null))
            Observable.DEACTIVATE_CATEGORY-> categoriesActionObservable.postValue(Resource.error(message, null))
        }
    }
}