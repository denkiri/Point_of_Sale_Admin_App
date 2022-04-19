package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.receipt.ReceiptData
import com.denkiri.pharmacy.models.reports.mpesareport.MpesaReportData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.PosApiRequestService
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceiptRepository (application: Application){
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    val receiptObservable = MutableLiveData<Resource<ReceiptData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
    }
    fun getReceipts(code: String) {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute(code)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }

    private fun execute(code:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = PosApiRequestService.getService("").receipts(code)
            call.enqueue(object : Callback<ReceiptData> {
                override fun onFailure(call: Call<ReceiptData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<ReceiptData>?, response: Response<ReceiptData>?) {
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
        receiptObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: ReceiptData) {
        receiptObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        receiptObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}