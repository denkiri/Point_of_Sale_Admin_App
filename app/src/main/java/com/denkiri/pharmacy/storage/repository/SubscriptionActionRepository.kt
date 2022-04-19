package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.payment.PaymentResponseData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.PosApiRequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class SubscriptionActionRepository (application: Application){
    val subscriptionActionObservable = MutableLiveData<Resource<PaymentResponseData>>()
    private val db: PharmacyDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    private val context: Context
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        context =application.applicationContext
    }
    fun subscribe(amount:String,phone:String,code:String) {
        setIsLoading(Observable.SUBSCRIBE)

        if (NetworkUtils.isConnected(context)) {
            execute(amount,phone,code)
        } else {
            setIsError(Observable.SUBSCRIBE, context.getString(R.string.no_connection))
        }
    }
    fun completeSub(amount:String,phone:String,code:String) {
        setIsLoading(Observable.COMPLETE_SUBSCRIPTION)
        if (NetworkUtils.isConnected(context)) {
            completeSubscription(amount,phone,code)
        } else {
            setIsError(Observable.COMPLETE_SUBSCRIPTION, context.getString(R.string.no_connection))
        }
    }
    fun execute(amount:String,phone:String,code:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = PosApiRequestService.getService("").subscribe(amount,phone,code)
            call.enqueue(object : Callback<PaymentResponseData> {
                override fun onFailure(call: Call<PaymentResponseData>?, t: Throwable?) {
                    setIsError(Observable.SUBSCRIBE, t.toString())
                }
                override fun onResponse(call: Call<PaymentResponseData>?, response: Response<PaymentResponseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccessful(Observable.SUBSCRIBE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.SUBSCRIBE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.SUBSCRIBE, response.toString())
                        }
                    } else {
                        setIsError(Observable.SUBSCRIBE, "Error Loading In")
                    }
                }
            })
        }

    }
    fun completeSubscription(amount:String,phone:String,code:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = PosApiRequestService.getService("").completePayment(amount,phone,code)
            call.enqueue(object : Callback<PaymentResponseData> {
                override fun onFailure(call: Call<PaymentResponseData>?, t: Throwable?) {
                    setIsError(Observable.COMPLETE_SUBSCRIPTION, t.toString())
                }
                override fun onResponse(call: Call<PaymentResponseData>?, response: Response<PaymentResponseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccessful(Observable. COMPLETE_SUBSCRIPTION,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable. COMPLETE_SUBSCRIPTION,it) }
                            }
                        }
                        else {
                            setIsError(Observable. COMPLETE_SUBSCRIPTION, response.toString())
                        }
                    } else {
                        setIsError(Observable. COMPLETE_SUBSCRIPTION, "Error Loading In")
                    }
                }
            })
        }

    }


    enum class Observable {
        SUBSCRIBE,
        COMPLETE_SUBSCRIPTION,
        CHECK_SUBSCRIPTION


    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.SUBSCRIBE -> subscriptionActionObservable.postValue(Resource.loading(null))
            Observable.COMPLETE_SUBSCRIPTION -> subscriptionActionObservable.postValue(Resource.loading(null))
            Observable.CHECK_SUBSCRIPTION-> subscriptionActionObservable.postValue(Resource.loading(null))

        }
    }
    private fun <T> setIsSuccessful(observable: Observable, data: T?) {
        when (observable) {
            Observable.SUBSCRIBE -> subscriptionActionObservable.postValue(Resource.success(data as PaymentResponseData))
            Observable.COMPLETE_SUBSCRIPTION -> subscriptionActionObservable.postValue(Resource.success(data as PaymentResponseData))
            Observable. CHECK_SUBSCRIPTION -> subscriptionActionObservable.postValue(Resource.success(data as PaymentResponseData))

        }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.SUBSCRIBE -> subscriptionActionObservable.postValue(Resource.error(message, null))
            Observable.COMPLETE_SUBSCRIPTION -> subscriptionActionObservable.postValue(Resource.error(message, null))
            Observable.CHECK_SUBSCRIPTION  -> subscriptionActionObservable.postValue(Resource.error(message, null))

        }
    }
}