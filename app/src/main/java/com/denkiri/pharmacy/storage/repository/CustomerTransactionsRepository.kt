package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.salesReport.CustomerTransactionsData
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

class CustomerTransactionsRepository (application: Application) {
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    val salesObservable = MutableLiveData<Resource<CustomerTransactionsData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
    }
    fun getCustomerTransactions(name: String) {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            customerTransactions(name)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun customerTransactions(name:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).customerTransactions(name)
            call.enqueue(object : Callback<CustomerTransactionsData> {
                override fun onFailure(call: Call<CustomerTransactionsData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<CustomerTransactionsData>?, response: Response<CustomerTransactionsData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                /*   if (response.body()!!.totalsales != null) {
                                       saveTotalSales(response.body()!!.totalsales)
                                   } else {
                                       saveTotalSales(TotalSales(0.0))
                                   }
                                   if (response.body()!!.totalsalescost != null) {
                                       saveTotalCost(response.body()!!.totalsalescost)
                                   } else {
                                       saveTotalCost(TotalCost(0.0))
                                   }
                                   if (response.body()!!.totalprofit != null) {
                                       saveTotalProfit(response.body()!!.totalprofit)
                                   } else {
                                       saveTotalProfit(TotalProfit(0.0))
                                   }
                                   if (response.body()!!.totalbalance != null) {
                                       saveTotalSalesBalance(response.body()!!.totalbalance)
                                   } else {
                                       saveTotalSalesBalance(TotalSalesBalance(0.0))
                                   }


                                 */
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
        salesObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: CustomerTransactionsData) {
        salesObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        salesObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }

}