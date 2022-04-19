package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.customer.CustomerData
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
class CustomerActionRepository (application: Application){
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val customerActionObservable = MutableLiveData<Resource<CustomerData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext

    }
    fun addCustomer(firstName:String,middleName:String,lastName:String,address:String,contact:String) {
        setIsLoading(Observable.ADD_CUSTOMER)

        if (NetworkUtils.isConnected(context)) {
            execute(firstName, middleName, lastName, address,contact)
        } else {
            setIsError(Observable.ADD_CUSTOMER, context.getString(R.string.no_connection))
        }

    }
    fun updateCustomer(firstName:String,middleName:String,lastName:String,address:String,contact:String,cid: String,code: String,name: String) {
        setIsLoading(Observable.EDIT_CUSTOMER)

        if (NetworkUtils.isConnected(context)) {
            editCustomer(firstName, middleName, lastName, address,contact,cid,code,name)
        } else {
            setIsError(Observable.EDIT_CUSTOMER, context.getString(R.string.no_connection))
        }

    }
    fun delete(cid:String){
        setIsLoading(Observable.EDIT_CUSTOMER)

        if (NetworkUtils.isConnected(context)) {
            delete(cid)
        } else {
            setIsError(Observable.EDIT_CUSTOMER, context.getString(R.string.no_connection))
        }

    }

    fun execute(firstName:String,middleName:String,lastName:String,address:String,contact:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addCustomer(firstName, middleName, lastName, address, contact)
            call.enqueue(object : Callback<CustomerData> {
                override fun onFailure(call: Call<CustomerData>?, t: Throwable?) {
                    setIsError(Observable.ADD_CUSTOMER, t.toString())
                }
                override fun onResponse(call: Call<CustomerData>?, response: Response<CustomerData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_CUSTOMER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_CUSTOMER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_CUSTOMER, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_CUSTOMER, "Error Loading In")
                    }
                }
            })
        }

    }
    fun editCustomer(firstName:String,middleName:String,lastName:String,address:String,contact:String,cid:String,code:String,name:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).editCustomer(firstName, middleName, lastName, address, contact,cid,code,name)
            call.enqueue(object : Callback<CustomerData> {
                override fun onFailure(call: Call<CustomerData>?, t: Throwable?) {
                    setIsError(Observable.EDIT_CUSTOMER, t.toString())
                }
                override fun onResponse(call: Call<CustomerData>?, response: Response<CustomerData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.EDIT_CUSTOMER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.EDIT_CUSTOMER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.EDIT_CUSTOMER, response.toString())
                        }
                    } else {
                        setIsError(Observable.EDIT_CUSTOMER, "Error Loading In")
                    }
                }
            })
        }

    }
    fun deleteCustomer( customerId: Int,customerName:String){

        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteCustomer( customerId.toString(),customerName
            )
            call.enqueue(object : Callback<CustomerData> {
                override fun onFailure(call: Call<CustomerData>?, t: Throwable?) {
                    setIsError(Observable.DELETE_CUSTOMER, t.toString())
                }
                override fun onResponse(call: Call<CustomerData>?, response: Response<CustomerData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DELETE_CUSTOMER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_CUSTOMER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_CUSTOMER, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_CUSTOMER, "Error Loading In")
                    }
                }
            })
        }
    }
    fun activateCustomer( customerId: Int){

        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).activateCustomer( customerId.toString()
            )
            call.enqueue(object : Callback<CustomerData> {
                override fun onFailure(call: Call<CustomerData>?, t: Throwable?) {
                    setIsError(Observable.ACTIVATE_CUSTOMER, t.toString())
                }
                override fun onResponse(call: Call<CustomerData>?, response: Response<CustomerData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ACTIVATE_CUSTOMER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ACTIVATE_CUSTOMER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ACTIVATE_CUSTOMER, response.toString())
                        }
                    } else {
                        setIsError(Observable.ACTIVATE_CUSTOMER, "Error Loading In")
                    }
                }
            })
        }
    }

    fun deactivateCustomer( customerId: Int){

        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deactivateCustomer( customerId.toString()
            )
            call.enqueue(object : Callback<CustomerData> {
                override fun onFailure(call: Call<CustomerData>?, t: Throwable?) {
                    setIsError(Observable.DEACTIVATE_CUSTOMER, t.toString())
                }
                override fun onResponse(call: Call<CustomerData>?, response: Response<CustomerData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DEACTIVATE_CUSTOMER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DEACTIVATE_CUSTOMER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DEACTIVATE_CUSTOMER, response.toString())
                        }
                    } else {
                        setIsError(Observable.DEACTIVATE_CUSTOMER, "Error Loading In")
                    }
                }
            })
        }
    }



    enum class Observable {
        DELETE_CUSTOMER,
        EDIT_CUSTOMER,
        ACTIVATE_CUSTOMER,
        DEACTIVATE_CUSTOMER,
        ADD_CUSTOMER
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }

    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.EDIT_CUSTOMER -> customerActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_CUSTOMER -> customerActionObservable.postValue(Resource.loading(null))
            Observable.ACTIVATE_CUSTOMER -> customerActionObservable.postValue(Resource.loading(null))
            Observable.DEACTIVATE_CUSTOMER -> customerActionObservable.postValue(Resource.loading(null))
            Observable.ADD_CUSTOMER -> customerActionObservable.postValue(Resource.loading(null))
        }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.EDIT_CUSTOMER -> customerActionObservable.postValue(Resource.success(data as CustomerData))
            Observable.DELETE_CUSTOMER -> customerActionObservable.postValue(Resource.success(data as CustomerData))
            Observable.ADD_CUSTOMER -> customerActionObservable.postValue(Resource.success(data as CustomerData))
            Observable.ACTIVATE_CUSTOMER -> customerActionObservable.postValue(Resource.success(data as CustomerData))
            Observable.DEACTIVATE_CUSTOMER -> customerActionObservable.postValue(Resource.success(data as CustomerData))
        }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.EDIT_CUSTOMER -> customerActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_CUSTOMER -> customerActionObservable.postValue(Resource.error(message, null))
            Observable.ADD_CUSTOMER -> customerActionObservable.postValue(Resource.error(message, null))
            Observable.ACTIVATE_CUSTOMER -> customerActionObservable.postValue(Resource.error(message, null))
            Observable.DEACTIVATE_CUSTOMER -> customerActionObservable.postValue(Resource.error(message, null))
        }
    }

}