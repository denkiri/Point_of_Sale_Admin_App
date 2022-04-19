package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.subscription.Subscription
import com.denkiri.pharmacy.models.subscription.SubscriptionData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.PosApiRequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.storage.daos.SubscriptionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SubscriptionRepository (application: Application) {
    val subscriptionObservable = MutableLiveData<Resource<SubscriptionData>>()
    private val subscriptionDao: SubscriptionDao
    private val db: PharmacyDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    private val context: Context
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        subscriptionDao=db.subscriptionDao()
        context =application.applicationContext
    }
    fun getSubscription(parameters: SubscriptionData){
        setIsLoading()
        if (NetworkUtils.isConnected(context)){
            executeSubscription(parameters)
        }


        else{
            setIsError(context.getString(R.string.no_connection))
        }
    }

    private fun executeSubscription(parameters: SubscriptionData){
        GlobalScope.launch(context= Dispatchers.Main){
            val call= PosApiRequestService.getService("").getCustomerSubscription(parameters.subscription?.code)
            call.enqueue(object: retrofit2.Callback<SubscriptionData> {
                override fun onFailure(call:retrofit2. Call<SubscriptionData>?, t:Throwable?){
                    setIsError(t.toString())
                }
                override fun onResponse(call: retrofit2.Call<SubscriptionData>?, response: retrofit2.Response<SubscriptionData>?) {



                    //To change body of created functions use File | Settings | File Templates.
                    if(response!=null){
                        if (response.isSuccessful){
                            if (response.body()?.status ==1 &&!response.body()?.error!!){
                                setIsSuccesful(response.body()!!)
                            }
                            else{
                                response.body()?.message?.let{setIsError(it)}

                            }

                        }
                        else{
                            setIsError(response.toString())
                        }

                    }
                    else{
                        setIsError("Error Loggin In")
                    }
                }
            })

        }
    }
    private fun setIsLoading(){
        subscriptionObservable.postValue(Resource.loading(null))
    }
    private fun setIsSuccesful(parameters: SubscriptionData){
        subscriptionObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String){
        subscriptionObservable.postValue(Resource.error(message, null))
    }
    fun saveSubscription(data: SubscriptionData){
        subscriptionDao.delete()
        data.subscription?.let { subscriptionDao.insertSubscription(it) }

    }
    fun getSubscription(): LiveData<Subscription> {
        return subscriptionDao.getSubscription()
    }
    fun fetchSubscription(): Subscription {
        return subscriptionDao.fetch()
    }


}