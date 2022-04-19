package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivableData
import com.denkiri.pharmacy.models.reports.accountReceivable.TotalBalance
import com.denkiri.pharmacy.models.reports.collectionReport.CollectionReportData
import com.denkiri.pharmacy.models.reports.collectionReport.TotalCollection
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.daos.ProfileDao
import com.denkiri.pharmacy.storage.daos.TotalBalanceDao
import com.denkiri.pharmacy.storage.daos.TotalCollectionDao
import com.denkiri.pharmacy.ui.reports.CollectionReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionRepository (application: Application){
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    private val totalCollectionDao: TotalCollectionDao
    val collectionObservable = MutableLiveData<Resource<CollectionReportData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
        totalCollectionDao=db.totalCollectionDao()
    }
    fun getCollection() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun saveTotalCollection(totalCollection: TotalCollection?) {
        if (totalCollection!=null) {
            totalCollectionDao.delete()
            totalCollectionDao.insertTotalCollection(totalCollection)
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).collectionReport()
            call.enqueue(object : Callback<CollectionReportData> {
                override fun onFailure(call: Call<CollectionReportData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<CollectionReportData>?, response: Response<CollectionReportData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalcollection != null) {
                                    saveTotalCollection(response.body()!!.totalcollection)
                                } else {
                                    saveTotalCollection(TotalCollection(0.0))
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
        collectionObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: CollectionReportData) {
        collectionObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        collectionObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    fun getTotalCollection(): LiveData<TotalCollection> {
        return totalCollectionDao.getTotalCollection()
    }
}