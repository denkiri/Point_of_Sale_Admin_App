package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.oauth.Oauth
import com.denkiri.pharmacy.models.oauth.Profile
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.storage.daos.ProfileDao
import com.google.android.gms.common.api.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.security.auth.callback.Callback
class SignInRepository(application: Application) {
    val signInObservable = MutableLiveData<Resource<Oauth>>()
    private val profileDao: ProfileDao
    private val db:PharmacyDatabase
    private val preferenceManager:PreferenceManager= PreferenceManager(application)
    private val context: Context
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        profileDao=db.profileDao()
        context =application.applicationContext
    }
    fun signIn(parameters:Oauth){
        setIsLoading()
        if (NetworkUtils.isConnected(context)){
            executeSignIn(parameters)
        }


        else{
            setIsError(context.getString(R.string.no_connection))
        }
    }

    private fun executeSignIn(parameters:Oauth){
        GlobalScope.launch(context=Dispatchers.Main){
            val call= RequestService.getService("").signIn(parameters.profile?.email,parameters.profile?.password)
            call.enqueue(object: retrofit2.Callback<Oauth> {
                override fun onFailure(call:retrofit2. Call<Oauth>?,t:Throwable?){
                    setIsError(t.toString())
                }
                override fun onResponse(call: retrofit2.Call<Oauth>?, response: retrofit2.Response<Oauth>?) {



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
        signInObservable.postValue(Resource.loading(null))
    }
    private fun setIsSuccesful(parameters: Oauth){
        signInObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String){
        signInObservable.postValue(Resource.error(message, null))
    }
    fun saveProfile(data:Oauth){
        profileDao.delete()
        data.profile?.let { profileDao.insertProfile(it) }
        preferenceManager.saveProfile(data)
        preferenceManager.setLoginStatus(1)

    }
    fun getProfile(): LiveData<Profile> {
        return profileDao.getProfile()
    }
    fun fetchProfile(): Profile {
        return profileDao.fetch()
    }


}