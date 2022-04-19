package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.invoice.InvoiceData
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

class InvoiceActionRepository (application: Application) {
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val invoiceActionObservable = MutableLiveData<Resource<InvoiceData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext

    }
    fun addPayment(name:String,invoice:String,totalAmount:String,amount:String,remarks:String,balance:String) {
        setIsLoading(Observable.ADD_PAYMENT)

        if (NetworkUtils.isConnected(context)) {
            execute(name, invoice, totalAmount, amount, remarks,balance)
        } else {
            setIsError(Observable.ADD_PAYMENT, context.getString(R.string.no_connection))
        }

    }
    fun execute(name:String,invoice:String,totalAmount:String,amount:String,remarks:String,balance: String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addPayment(name, invoice, totalAmount, amount, remarks,balance)
            call.enqueue(object : Callback<InvoiceData> {
                override fun onFailure(call: Call<InvoiceData>?, t: Throwable?) {
                    setIsError(Observable.ADD_PAYMENT, t.toString())
                }
                override fun onResponse(call: Call<InvoiceData>?, response: Response<InvoiceData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_PAYMENT,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_PAYMENT,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_PAYMENT, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_PAYMENT, "Error Loading In")
                    }
                }
            })
        }

    }

    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }

    enum class Observable {
        ADD_PAYMENT
    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.ADD_PAYMENT -> invoiceActionObservable.postValue(Resource.loading(null))

        }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.ADD_PAYMENT -> invoiceActionObservable.postValue(Resource.success(data as InvoiceData))

        }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.ADD_PAYMENT -> invoiceActionObservable.postValue(Resource.error(message, null))

        }
    }

}