package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivableData
import com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReport
import com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReportData
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

class InventoryRepository(application: Application) {
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    val inventoryObservable = MutableLiveData<Resource<InventoryReportData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
    }
    fun inventoryReport() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).inventoryReport()
            call.enqueue(object : Callback<InventoryReportData> {
                override fun onFailure(call: Call<InventoryReportData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<InventoryReportData>?, response: Response<InventoryReportData>?) {
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
        inventoryObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: InventoryReportData) {
        inventoryObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        inventoryObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}