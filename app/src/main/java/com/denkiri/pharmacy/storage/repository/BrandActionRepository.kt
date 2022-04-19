package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.brand.BrandData
import com.denkiri.pharmacy.models.custom.Resource
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
class BrandActionRepository (application: Application) {
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val brandActionObservable = MutableLiveData<Resource<BrandData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext

    }
    fun addBrand(brandName: String) {
        setIsLoading(Observable.ADD_BRAND)

        if (NetworkUtils.isConnected(context)) {
            execute(brandName)
        } else {
            setIsError(Observable.ADD_BRAND, context.getString(R.string.no_connection))
        }
    }
    fun editBrand(brandName: String,brandId: String,brand: String) {
        setIsLoading(Observable.EDIT_BRAND)

        if (NetworkUtils.isConnected(context)) {
            updateBrand(brandName,brandId,brand)
        } else {
            setIsError(Observable.EDIT_BRAND, context.getString(R.string.no_connection))
        }
    }
    fun activateBrand(brandId:String) {
        setIsLoading(Observable.ACTIVATE_BRAND)

        if (NetworkUtils.isConnected(context)) {
            activate(brandId)
        } else {
            setIsError(Observable.ACTIVATE_BRAND, context.getString(R.string.no_connection))
        }
    }
    fun deactivateBrand(brandId:String) {
        setIsLoading(Observable.DEACTIVATE_BRAND)

        if (NetworkUtils.isConnected(context)) {
            deactivate(brandId)
        } else {
            setIsError(Observable.DEACTIVATE_BRAND, context.getString(R.string.no_connection))
        }
    }
    fun execute(name:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addBrand(name)
            call.enqueue(object : Callback<BrandData> {
                override fun onFailure(call: Call<BrandData>?, t: Throwable?) {
                    setIsError(Observable.ADD_BRAND, t.toString())
                }
                override fun onResponse(call: Call<BrandData>?, response: Response<BrandData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_BRAND,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_BRAND,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_BRAND, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_BRAND, "Error Loading In")
                    }
                }
            })
        }

    }

    fun deleteBrand( brandId: Int){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteBrand( brandId.toString()
            )
            call.enqueue(object : Callback<BrandData> {
                override fun onFailure(call: Call<BrandData>?, t: Throwable?) {
                    setIsError(Observable.DELETE_BRAND, t.toString())
                }
                override fun onResponse(call: Call<BrandData>?, response: Response<BrandData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DELETE_BRAND,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_BRAND,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_BRAND, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_BRAND, "Error Loading In")
                    }
                }
            })
        }
    }
    fun updateBrand( name: String,cid: String,brand:String){

        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).editBrand(name,cid,brand
            )
            call.enqueue(object : Callback<BrandData> {
                override fun onFailure(call: Call<BrandData>?, t: Throwable?) {
                    setIsError(Observable.EDIT_BRAND, t.toString())
                }
                override fun onResponse(call: Call<BrandData>?, response: Response<BrandData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.EDIT_BRAND,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.EDIT_BRAND,it) }
                            }
                        }
                        else {
                            setIsError(Observable.EDIT_BRAND, response.toString())
                        }
                    } else {
                        setIsError(Observable.EDIT_BRAND, "Error Loading In")
                    }
                }
            })
        }
    }

    fun activate( brandId: String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).activateBrand( brandId.toString()
            )
            call.enqueue(object : Callback<BrandData> {
                override fun onFailure(call: Call<BrandData>?, t: Throwable?) {
                    setIsError(Observable.ACTIVATE_BRAND, t.toString())
                }
                override fun onResponse(call: Call<BrandData>?, response: Response<BrandData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ACTIVATE_BRAND,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ACTIVATE_BRAND,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ACTIVATE_BRAND, response.toString())
                        }
                    } else {
                        setIsError(Observable.ACTIVATE_BRAND, "Error Loading In")
                    }
                }
            })
        }
    }
    fun deactivate( brandId: String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deactivateBrand( brandId.toString()
            )
            call.enqueue(object : Callback<BrandData> {
                override fun onFailure(call: Call<BrandData>?, t: Throwable?) {
                    setIsError(Observable.DEACTIVATE_BRAND, t.toString())
                }
                override fun onResponse(call: Call<BrandData>?, response: Response<BrandData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DEACTIVATE_BRAND,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DEACTIVATE_BRAND,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DEACTIVATE_BRAND, response.toString())
                        }
                    } else {
                        setIsError(Observable.DEACTIVATE_BRAND, "Error Loading In")
                    }
                }
            })
        }
    }
    enum class Observable {
        DELETE_BRAND,
        EDIT_BRAND,
        ACTIVATE_BRAND,
        DEACTIVATE_BRAND,
        ADD_BRAND
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.EDIT_BRAND -> brandActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_BRAND -> brandActionObservable.postValue(Resource.loading(null))
            Observable.ADD_BRAND -> brandActionObservable.postValue(Resource.loading(null))
            Observable.ACTIVATE_BRAND -> brandActionObservable.postValue(Resource.loading(null))
            Observable.DEACTIVATE_BRAND -> brandActionObservable.postValue(Resource.loading(null))
        }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.EDIT_BRAND-> brandActionObservable.postValue(Resource.success(data as BrandData))
            Observable.DELETE_BRAND-> brandActionObservable.postValue(Resource.success(data as BrandData))
            Observable.ADD_BRAND-> brandActionObservable.postValue(Resource.success(data as BrandData))
            Observable.ACTIVATE_BRAND-> brandActionObservable.postValue(Resource.success(data as BrandData))
            Observable.DEACTIVATE_BRAND-> brandActionObservable.postValue(Resource.success(data as BrandData))
        }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.EDIT_BRAND-> brandActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_BRAND-> brandActionObservable.postValue(Resource.error(message, null))
            Observable.ADD_BRAND-> brandActionObservable.postValue(Resource.error(message, null))
            Observable.ACTIVATE_BRAND-> brandActionObservable.postValue(Resource.error(message, null))
            Observable.DEACTIVATE_BRAND-> brandActionObservable.postValue(Resource.error(message, null))
        }
    }
}