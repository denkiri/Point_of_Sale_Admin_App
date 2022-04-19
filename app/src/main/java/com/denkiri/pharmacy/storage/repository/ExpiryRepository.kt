package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivableData
import com.denkiri.pharmacy.models.reports.collectionReport.TotalCollection
import com.denkiri.pharmacy.models.reports.expiredproducts.ExpiryReport
import com.denkiri.pharmacy.models.reports.expiredproducts.ExpiryReportData
import com.denkiri.pharmacy.models.reports.expiredproducts.TotalLose
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.daos.ProfileDao
import com.denkiri.pharmacy.storage.daos.TotalLoseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExpiryRepository  (application: Application){
    private val context: Context
    private val profileDao: ProfileDao
    private val totalLoseDao:TotalLoseDao
    private val db: PharmacyDatabase
    val expiryObservable = MutableLiveData<Resource<ExpiryReportData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
        totalLoseDao=db.totalLoseDao()
    }
    fun getExpiryReport() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun saveTotalLose(totalLose: TotalLose?) {
        if (totalLose!=null) {
            totalLoseDao.delete()
            totalLoseDao.insertTotalLose(totalLose)
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).expiryReport()
            call.enqueue(object : Callback<ExpiryReportData> {
                override fun onFailure(call: Call<ExpiryReportData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<ExpiryReportData>?, response: Response<ExpiryReportData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totallose != null) {
                                    saveTotalLose(response.body()!!.totallose)
                                } else {
                                    saveTotalLose(TotalLose(0.0))
                                }

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
        expiryObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: ExpiryReportData) {
        expiryObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        expiryObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    fun getTotalLose(): LiveData<TotalLose> {
        return totalLoseDao.getTotalLose()
    }
}