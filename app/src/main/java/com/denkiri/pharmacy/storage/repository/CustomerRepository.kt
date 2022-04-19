package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.customer.Customer
import com.denkiri.pharmacy.models.customer.CustomerData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerRepository (application: Application) {
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    val customerObservable = MutableLiveData<Resource<CustomerData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
    }
    fun getCustomers() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).customers()
            call.enqueue(object : Callback<CustomerData> {
                override fun onFailure(call: Call<CustomerData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<CustomerData>?, response: Response<CustomerData>?) {
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
        customerObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: CustomerData) {
        customerObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        customerObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }

}