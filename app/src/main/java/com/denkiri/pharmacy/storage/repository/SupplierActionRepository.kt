package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.supplier.Supplier
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
class SupplierActionRepository (application: Application){
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val supplierActionObservable = MutableLiveData<Resource<SupplierData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext

    }
    fun addSupplier(supplierName:String,supplierAddress:String,supplierContact:String,contactPerson:String) {
        setIsLoading(Observable.ADD_SUPPLIER)

        if (NetworkUtils.isConnected(context)) {
            execute(supplierName,supplierAddress,supplierContact,contactPerson)
        } else {
            setIsError(Observable.ADD_SUPPLIER, context.getString(R.string.no_connection))
        }
    }
    fun editSupplier(supplierName:String,supplierAddress:String,supplierContact:String,contactPerson:String,supplierId:String) {
        setIsLoading(Observable.EDIT_SUPPLIER)

        if (NetworkUtils.isConnected(context)) {
            updateSupplier(supplierName,supplierAddress,supplierContact,contactPerson,supplierId)
        } else {
            setIsError(Observable.EDIT_SUPPLIER, context.getString(R.string.no_connection))
        }
    }
    fun deleteSupplier(supplierId:String) {
        setIsLoading(Observable.DELETE_SUPPLIER)

        if (NetworkUtils.isConnected(context)) {
            delete(supplierId)
        } else {
            setIsError(Observable.DELETE_SUPPLIER, context.getString(R.string.no_connection))
        }
    }
    fun activateSupplier(supplierId:String) {
        setIsLoading(Observable.ACTIVATE_SUPPLIER)

        if (NetworkUtils.isConnected(context)) {
            activate(supplierId)
        } else {
            setIsError(Observable.ACTIVATE_SUPPLIER, context.getString(R.string.no_connection))
        }
    }
    fun deactivateSupplier(supplierId:String) {
        setIsLoading(Observable.DEACTIVATE_SUPPLIER)

        if (NetworkUtils.isConnected(context)) {
            deactivate(supplierId)
        } else {
            setIsError(Observable.DEACTIVATE_SUPPLIER, context.getString(R.string.no_connection))
        }
    }
    fun execute(name:String,address:String,contact:String,cperson:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addSupplier(name,address,contact,cperson)
            call.enqueue(object : Callback<SupplierData> {
                override fun onFailure(call: Call<SupplierData>?, t: Throwable?) {
                    setIsError( Observable.ADD_SUPPLIER, t.toString())
                }
                override fun onResponse(call: Call<SupplierData>?, response: Response<SupplierData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_SUPPLIER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_SUPPLIER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_SUPPLIER, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_SUPPLIER, "Error Loading In")
                    }
                }
            })
        }

    }
    fun updateSupplier(name:String,address:String,contact:String,cperson:String,memi:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).editSupplier(name,address,contact,cperson,memi)
            call.enqueue(object : Callback<SupplierData> {
                override fun onFailure(call: Call<SupplierData>?, t: Throwable?) {
                    setIsError( Observable.EDIT_SUPPLIER, t.toString())
                }
                override fun onResponse(call: Call<SupplierData>?, response: Response<SupplierData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.EDIT_SUPPLIER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.EDIT_SUPPLIER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.EDIT_SUPPLIER, response.toString())
                        }
                    } else {
                        setIsError(Observable.EDIT_SUPPLIER, "Error Loading In")
                    }
                }
            })
        }

    }
    fun delete(memi:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteSupplier(memi)
            call.enqueue(object : Callback<SupplierData> {
                override fun onFailure(call: Call<SupplierData>?, t: Throwable?) {
                    setIsError( Observable.DELETE_SUPPLIER, t.toString())
                }
                override fun onResponse(call: Call<SupplierData>?, response: Response<SupplierData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DELETE_SUPPLIER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_SUPPLIER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_SUPPLIER, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_SUPPLIER, "Error Loading In")
                    }
                }
            })
        }

    }
    fun activate(memi:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).activateSupplier(memi)
            call.enqueue(object : Callback<SupplierData> {
                override fun onFailure(call: Call<SupplierData>?, t: Throwable?) {
                    setIsError( Observable. ACTIVATE_SUPPLIER, t.toString())
                }
                override fun onResponse(call: Call<SupplierData>?, response: Response<SupplierData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable. ACTIVATE_SUPPLIER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable. ACTIVATE_SUPPLIER,it) }
                            }
                        }
                        else {
                            setIsError(Observable. ACTIVATE_SUPPLIER, response.toString())
                        }
                    } else {
                        setIsError(Observable. ACTIVATE_SUPPLIER, "Error Loading In")
                    }
                }
            })
        }

    }
    fun deactivate(memi:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deactivateSupplier(memi)
            call.enqueue(object : Callback<SupplierData> {
                override fun onFailure(call: Call<SupplierData>?, t: Throwable?) {
                    setIsError( Observable.  DEACTIVATE_SUPPLIER, t.toString())
                }
                override fun onResponse(call: Call<SupplierData>?, response: Response<SupplierData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.  DEACTIVATE_SUPPLIER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.  DEACTIVATE_SUPPLIER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.  DEACTIVATE_SUPPLIER, response.toString())
                        }
                    } else {
                        setIsError(Observable.  DEACTIVATE_SUPPLIER, "Error Loading In")
                    }
                }
            })
        }

    }
    enum class Observable {
        DELETE_SUPPLIER,
        EDIT_SUPPLIER,
        ACTIVATE_SUPPLIER,
        DEACTIVATE_SUPPLIER,
        ADD_SUPPLIER
    }

    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.ADD_SUPPLIER -> supplierActionObservable.postValue(Resource.loading(null))
            Observable.EDIT_SUPPLIER -> supplierActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_SUPPLIER -> supplierActionObservable.postValue(Resource.loading(null))
            Observable.ACTIVATE_SUPPLIER -> supplierActionObservable.postValue(Resource.loading(null))
            Observable.DEACTIVATE_SUPPLIER -> supplierActionObservable.postValue(Resource.loading(null))
        }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.ADD_SUPPLIER -> supplierActionObservable.postValue(Resource.success(data as SupplierData))
            Observable.EDIT_SUPPLIER -> supplierActionObservable.postValue(Resource.success(data as SupplierData))
            Observable.DELETE_SUPPLIER -> supplierActionObservable.postValue(Resource.success(data as SupplierData))
            Observable.ACTIVATE_SUPPLIER -> supplierActionObservable.postValue(Resource.success(data as SupplierData))
            Observable.DEACTIVATE_SUPPLIER -> supplierActionObservable.postValue(Resource.success(data as SupplierData))
        }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.ADD_SUPPLIER-> supplierActionObservable.postValue(Resource.error(message, null))
            Observable.EDIT_SUPPLIER-> supplierActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_SUPPLIER-> supplierActionObservable.postValue(Resource.error(message, null))
            Observable.ACTIVATE_SUPPLIER-> supplierActionObservable.postValue(Resource.error(message, null))
            Observable.DEACTIVATE_SUPPLIER-> supplierActionObservable.postValue(Resource.error(message, null))
        }
    }


}